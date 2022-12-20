package kitty.site.data.controller;

import kitty.site.data.KittyData;
import kitty.site.data.KittyDataRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/")
public class KittyDataController {

    @Autowired
    KittyDataRepository kittyDataRepository;

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
}
