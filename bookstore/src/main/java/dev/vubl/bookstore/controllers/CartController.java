package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.AddToCartRequest;
import dev.vubl.bookstore.dtos.CartDto;
import dev.vubl.bookstore.services.CartService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {
  private final CartService cartService;

  @GetMapping
  public ResponseEntity<CartDto> getUserCart(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    return ResponseEntity.ok().body(cartService.getActiveUserCartToDto(token));
  }

  @PostMapping("/add")
  public ResponseEntity<String> addToCart(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody AddToCartRequest payload) {
    cartService.addToCart(payload, token);
    return ResponseEntity.ok().body("Item added");
  }

  @PostMapping("/sync")
  public ResponseEntity<String> syncCart(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody List<AddToCartRequest> payload) {
    cartService.syncCart(token, payload);
    return ResponseEntity.ok("ok");
  }

  @DeleteMapping("/remove")
  public ResponseEntity<String> removeItemFromCart(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token, @RequestParam Integer bookId) {
    cartService.removeItemFromCart(token, bookId);
    return ResponseEntity.ok("ok");
  }
}
