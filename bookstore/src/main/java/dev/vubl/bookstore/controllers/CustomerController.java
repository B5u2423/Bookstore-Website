package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.AccountDetailDTO;
import dev.vubl.bookstore.dtos.AddressDTO;
import dev.vubl.bookstore.dtos.UpdateProfileRequest;
import dev.vubl.bookstore.entities.CustomerAddressInfo;
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
  public ResponseEntity<AccountDetailDTO> getUserAccountDetail(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    return ResponseEntity.status(HttpStatus.OK)
        .body(applicationUserService.getAccountDetail(authService.readUserFromToken(token)));
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

  @PostMapping("/add-address")
  public ResponseEntity<CustomerAddressInfo> addAddressInfo(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestBody AddressDTO payload) {

    return ResponseEntity.ok()
        .body(applicationUserService.addAddressInfo(authService.readUserFromToken(token), payload));
  }

  @PutMapping("/update-address")
  public ResponseEntity<CustomerAddressInfo> updateAddress(
      @RequestBody AddressDTO payload, @RequestParam(value = "id") Integer addressId) {
    return ResponseEntity.ok().body(applicationUserService.updateAddressInfo(addressId, payload));
  }

  @DeleteMapping("/remove-address")
  public ResponseEntity<String> removeAddress(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestParam(value = "id") Integer addressId) {
    return ResponseEntity.ok()
        .body(
            applicationUserService.removeAddressInfo(
                authService.readUserFromToken(token), addressId));
  }
}
