package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dto.LoginResponseDTO;
import dev.vubl.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
  private final UserService userService;

  @Autowired
  public AuthenticationController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping
  public ResponseEntity<LoginResponseDTO> userLogin() {
    return null;
  }
}
