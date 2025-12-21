package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.ApplyCouponRequest;
import dev.vubl.bookstore.dtos.CouponDTO;
import dev.vubl.bookstore.entities.Coupon;
import dev.vubl.bookstore.services.CouponService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
  private final CouponService couponService;

  @GetMapping
  public ResponseEntity<List<Coupon>> getAllCoupons() {
    return ResponseEntity.ok().body(couponService.getAllCoupons());
  }

  @PostMapping("/add")
  public ResponseEntity<Coupon> addNewCoupon(@Valid @RequestBody CouponDTO payload) {
    return ResponseEntity.ok().body(couponService.addNewCoupon(payload));
  }

  @PutMapping("/update")
  public ResponseEntity<String> updateCoupon(@RequestBody CouponDTO payload) {
    return ResponseEntity.ok().body("Ok");
  }

  @DeleteMapping("/deletee")
  public ResponseEntity<String> deleteCoupon(@RequestParam(value = "id") Integer id) {
    couponService.deleteById(id);
    return ResponseEntity.ok().body("Ok");
  }

  @PostMapping("/apply")
  public ResponseEntity<?> applyCoupon(@RequestBody ApplyCouponRequest request) {
    BigDecimal finalAmount = couponService.applyCoupon(request.couponCode(), request.orderAmount());

    return ResponseEntity.ok(Map.of("finalAmount", finalAmount));
  }
}
