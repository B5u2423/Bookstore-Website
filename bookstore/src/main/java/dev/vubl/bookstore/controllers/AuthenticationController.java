package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.LoginRequestDTO;
import dev.vubl.bookstore.dtos.LoginResponseDTO;
import dev.vubl.bookstore.dtos.RegistrationRequest;
import dev.vubl.bookstore.dtos.RegistrationResponse;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final ApplicationUserService userService;
  private final AuthService authService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> userLogin(@RequestBody LoginRequestDTO request) {
    LoginResponseDTO resp = new LoginResponseDTO("this will be your token");
    return new ResponseEntity<>(resp, HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<RegistrationResponse> userRegister(
      @RequestBody RegistrationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
  }
}
