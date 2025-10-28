package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dto.LoginRequestDTO;
import dev.vubl.bookstore.dto.LoginResponseDTO;
import dev.vubl.bookstore.dto.RegistrationRequestDTO;
import dev.vubl.bookstore.dto.RegistrationResponseDTO;
import dev.vubl.bookstore.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
