package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.AddToCartRequest;
import dev.vubl.bookstore.entities.Cart;
import dev.vubl.bookstore.services.CartService;
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
  public ResponseEntity<Cart> getUserCart(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    return ResponseEntity.ok().body(cartService.getActiveCartByUser(token));
  }

  @PostMapping("/add")
  public ResponseEntity<String> addToCart(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody AddToCartRequest payload) {
    cartService.addToCart(payload, token);
    return ResponseEntity.ok().body("Item added");
  }
}
