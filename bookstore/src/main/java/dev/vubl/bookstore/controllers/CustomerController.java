package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
  private final AuthService authService;

  @GetMapping("/account")
  public ResponseEntity<ApplicationUser> getUserAccountDetail(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.readUserFromToken(token));
  }
}
