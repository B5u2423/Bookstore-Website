package dev.vubl.bookstore.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

  @GetMapping("/commune")
  public ResponseEntity<?> getCommunes(@RequestParam String province) {
    String url = "https://provinces.open-api.vn/api/v2/w/?province=" + province;

    try {
      Object result = restTemplate.getForObject(url, Object.class);
      return ResponseEntity.ok(result);
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
          .body("Failed to fetch API: " + e.getMessage());
    }
  }
}
