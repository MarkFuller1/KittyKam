package kitty.site.data.service;

import kitty.site.data.repository.KittyDataRepository;
import kitty.site.data.model.Bar;
import kitty.site.data.model.BarGroup;
import kitty.site.data.model.KittyData;
import kitty.site.data.model.KittyEvent;
import kitty.site.data.model.Line;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.util.Pair;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static kitty.site.data.util.Util.getMillis;

@Service
@Slf4j
@AllArgsConstructor
public class KittyProcessingService {

  @Autowired
  KittyDataRepository kittyDataRepository;

  public final static Long GROUPING_MARGIN = TimeUnit.MINUTES.toMillis(10);

  public Bar buildBarChartOneWeek() {
    List<KittyData> weekData = kittyDataRepository.findByTimestampGreaterThanEqual(LocalDateTime.now().minusDays(7L));

    List<List<KittyData>> grouped = collate(weekData);

    // bar charts will be the length of the event
    List<BarGroup> b = grouped.stream().map(group -> {
      BarGroup g = new BarGroup();
      g.put(group.get(0).getTimestamp().toString(),
          getMillis(group.get(group.size() - 1).getTimestamp()) - getMillis(group.get(0).getTimestamp()));
      return g;
    }).collect(Collectors.toList());

    return new Bar(b);
  }

  public Line buildLineChartOneWeek() {
    List<KittyData> weekData = kittyDataRepository.findByTimestampGreaterThanEqual(LocalDateTime.now().minusDays(7));

    List<List<KittyData>> grouped = collate(weekData);

    return null;

  }

  public List<Optional<KittyEvent>> getLongestAndShortest() {
    List<KittyData> weekData = kittyDataRepository.findByTimestampGreaterThanEqual(LocalDateTime.now().minusDays(7));

    List<List<KittyData>> grouped = collate(weekData);

    Optional<KittyEvent> longest = grouped.parallelStream()
        .map(element -> new KittyEvent(element))
        .max(KittyEvent::compareTo);
    Optional<KittyEvent> shortest = grouped.parallelStream()
        .map(element -> new KittyEvent(element))
        .min(KittyEvent::compareTo);

    return List.of(shortest, longest);

  }

  public static List<List<KittyData>> collate(List<KittyData> data) {

    Collections.sort(data);

    log.info(data.toString());

    KittyData groupingVal = data.get(0);
    int groupingIndex = 0;

    List<List<KittyData>> groups = new ArrayList<>();

    for (KittyData datum : data) {
      if (datum.isWithinMarginCompare(datum, groupingVal, datum::isWithinMargin)) {
        addToGroup(datum, groupingIndex, groups);
      } else {
        groupingIndex = groupingIndex + 1;
        groupingVal = datum;
        addToGroup(datum, groupingIndex, groups);
      }
    }

    for (List<KittyData> group : groups) {
      for (KittyData k : group) {
        System.out.print(k + ",");
      }
      System.out.println();
    }
    return groups;
  }

  static <K> void addToGroup(K data, int index, List<List<K>> groups) {
    if (groups.size() <= index) {
      groups.add(new ArrayList<>());
    }
    List<K> list = groups.get(index);

    if (list == null) {
      list = new ArrayList<>();
    }

    list.add(data);
  }

}
