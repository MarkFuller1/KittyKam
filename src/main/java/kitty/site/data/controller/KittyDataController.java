package kitty.site.data.controller;

import kitty.site.data.KittyDataRepository;
import kitty.site.data.model.Bar;
import kitty.site.data.model.KittyData;
import kitty.site.data.service.KittyProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/")
public class KittyDataController {

  @Autowired
  KittyDataRepository kittyDataRepository;

  @Autowired
  KittyProcessingService kittyProcessingService;

  @GetMapping
  public ResponseEntity<String> saveData() {
    log.info("Recieved new message");
    var res = kittyDataRepository.save(new KittyData(UUID.randomUUID(), LocalDateTime.now()));
    return ResponseEntity.ok(res.toString());
  }

  @GetMapping("/lastWeek")
  public ResponseEntity<List<KittyData>> getLastWeek() {
    var now = LocalDateTime.now();
    return ResponseEntity.ok(kittyDataRepository.findByTimestampGreaterThanEqual(now.minusDays(7)));
  }

  @GetMapping("/lastWeek/bar")
  public ResponseEntity<Bar> getWeekbarChar() {
   return ResponseEntity.ok(kittyProcessingService.buildBarChartOneWeek());
  }

  @DeleteMapping("/deleteData")
  public ResponseEntity<String> deleteData(){
    kittyDataRepository.deleteAll();
    return ResponseEntity.ok("deleted");
  }
  
}
