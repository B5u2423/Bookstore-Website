package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.*;
import dev.vubl.bookstore.exceptions.RevalidateTokenException;
import dev.vubl.bookstore.exceptions.UnableToRegisterApplicationUserException;
import dev.vubl.bookstore.services.ApplicationUserService;
import dev.vubl.bookstore.services.AuthService;
import dev.vubl.bookstore.services.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {
  private final ApplicationUserService userService;
  private final AuthService authService;
  private final TokenService tokenService;

  @PostMapping("/login")
  public ResponseEntity<LoginResponse> userLogin(
      @RequestBody LoginRequest body, HttpServletResponse response, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(authService.logInUser(body, request, response));
  }

  @PostMapping("/admin/login")
  public ResponseEntity<LoginResponse> adminLogin(
      @RequestBody LoginRequest body, HttpServletResponse response, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(authService.logInUser(body, request, response));
  }

  @PostMapping("/register")
  public ResponseEntity<RegistrationResponse> userRegister(
      @RequestBody @Valid RegistrationRequest request) {
    return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
  }

  @GetMapping("/refresh")
  public ResponseEntity<LoginResponse> refreshJwtAccessToken(@RequestBody RefreshRequest body) {
    return ResponseEntity.status(HttpStatus.OK).body(tokenService.refreshJwt(body.refreshToken()));
  }

  @DeleteMapping("/logout")
  public ResponseEntity<String> logOutUser(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    authService.logOutUser(token);
    return ResponseEntity.status(HttpStatus.OK).body("User logged out");
  }

  @ExceptionHandler({RevalidateTokenException.class})
  public ResponseEntity<String> revalidateTokenExceptionHandler() {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body("Refresh token does not exist or is expired! Please re-authenticate!");
  }

  @ExceptionHandler({UnableToRegisterApplicationUserException.class})
  public ResponseEntity<String> unableToRegisterApplicationUserException() {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Tài khoản đã tồn tại với email");
  }

  @ExceptionHandler({MethodArgumentNotValidException.class})
  public ResponseEntity<String> methodArgumentNotValidException() {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body("Các trường dữ liệu không được bỏ trống");
  }
}
