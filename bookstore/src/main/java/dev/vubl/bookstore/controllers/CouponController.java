package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.ApplyCouponRequest;
import dev.vubl.bookstore.services.CouponService;
import java.math.BigDecimal;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
  private final CouponService couponService;

  @PostMapping("/apply")
  public ResponseEntity<?> applyCoupon(@RequestBody ApplyCouponRequest request) {
    BigDecimal finalAmount = couponService.applyCoupon(request.couponCode(), request.orderAmount());

    return ResponseEntity.ok(Map.of("finalAmount", finalAmount));
  }
}
