package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.Commune;
import dev.vubl.bookstore.dtos.Province;
import dev.vubl.bookstore.services.ProvinceCommuneDataService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/proxy")
@RequiredArgsConstructor
public class ProxyController {
  private final RestTemplate restTemplate;
  private final ProvinceCommuneDataService dataService;

  @GetMapping("/province")
  public List<Province> getProvinces() {
    return dataService.getAllProvinces();
  }

  @GetMapping("/commune")
  public List<Commune> getCommunes(@RequestParam String province) {
    return dataService.getCommunesOfProvince(province);
  }
}
