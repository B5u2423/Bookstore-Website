package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.UpdateProfileRequest;
import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
@RequiredArgsConstructor
public class CustomerController {
  private final AuthService authService;
  private final ApplicationUserService applicationUserService;

  @GetMapping("/account")
  public ResponseEntity<ApplicationUser> getUserAccountDetail(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    return ResponseEntity.status(HttpStatus.OK).body(authService.readUserFromToken(token));
  }

  @PutMapping("/profile")
  public ResponseEntity<String> updateUserProfile(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody @Valid UpdateProfileRequest payload) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            applicationUserService.updateUserProfileInfo(
                authService.readUserFromToken(token), payload));
  }
}
