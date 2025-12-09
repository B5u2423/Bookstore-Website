package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/admin")
@RestController
@RequiredArgsConstructor
public class AdminController {
  private final ApplicationUserService userService;
  private final BookService bookService;

  @GetMapping("/users/all")
  public ResponseEntity<List<ApplicationUser>> getAllUsers() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.readAllUsers());
  }
}
