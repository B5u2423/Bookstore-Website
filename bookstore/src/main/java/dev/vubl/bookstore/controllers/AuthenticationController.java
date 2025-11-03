package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.LoginRequestDTO;
import dev.vubl.bookstore.dtos.LoginResponseDTO;
import dev.vubl.bookstore.dtos.RegistrationRequestDTO;
import dev.vubl.bookstore.dtos.RegistrationResponseDTO;
import dev.vubl.bookstore.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final UserService userService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponseDTO> userLogin(@RequestBody LoginRequestDTO request) {
    LoginResponseDTO resp = new LoginResponseDTO("this will be your token");
    return new ResponseEntity<>(resp, HttpStatus.OK);
  }

  @PostMapping("/register")
  public ResponseEntity<RegistrationResponseDTO> userRegister(
      @RequestBody RegistrationRequestDTO request) {
    return new ResponseEntity<>(HttpStatus.CREATED);
  }
}
