package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.services.CloudinaryService;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
@RequiredArgsConstructor
public class CloudinaryController {
  private final CloudinaryService imageService;

  @PostMapping("/upload")
  public ResponseEntity<?> upload(@RequestParam("image") MultipartFile file) {
    try {
      String url = imageService.uploadImage(file);
      return ResponseEntity.ok(Map.of("url", url));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body("Upload failed");
    }
  }
}
