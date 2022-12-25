package kitty.site.data.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static kitty.site.data.util.Util.getMillis;

@Data
public class KittyEvent extends ArrayList<KittyData> implements Comparable<ArrayList<KittyData>> {

  private static Long eventDuration(List<KittyData> events){
     return Math.abs(getMillis(events.get(0).getTimestamp()) - getMillis(events.get(events.size() - 1).getTimestamp()));
  }

  public KittyEvent(Collection<? extends KittyData> c) {
    super(c);
  }

  @Override
  public int compareTo(ArrayList<KittyData> that) {


    Long thisDuration = eventDuration(this);
    Long thatDuration = eventDuration(that);

    if(thisDuration == thatDuration){
      return 0;
    }

    return thisDuration < thatDuration ? -1 : 1;


  }
}
