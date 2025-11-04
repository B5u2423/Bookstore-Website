package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.services.ApplicationUserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
  private final ApplicationUserService userService;

  @GetMapping("/users/all")
  public ResponseEntity<List<ApplicationUser>> getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.readAllUsers());
  }
}
