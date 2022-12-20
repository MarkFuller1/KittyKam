package kitty.site.data.controller;

import my.site.ithoughtilearned.model.GeoData;
import my.site.ithoughtilearned.service.GeoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/kittydata")
public class GeoDataController {

    @Autowired
    KittyDataService kittyDataService;

    @GetMapping
    public ResponseEntity<String> getAllGeoData() {
      return new ResponseEntity<String>();
  }

}
