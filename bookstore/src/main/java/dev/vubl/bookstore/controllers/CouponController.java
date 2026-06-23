package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.ApplyCouponRequest;
import dev.vubl.bookstore.dtos.CouponAppliedDTO;
import dev.vubl.bookstore.dtos.CouponDTO;
import dev.vubl.bookstore.entities.Coupon;
import dev.vubl.bookstore.services.CouponService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
public class CouponController {
  private final CouponService couponService;

  @GetMapping
  public PagedModel<Coupon> getAllCoupons(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "order", defaultValue = "asc") String order) {
    return new PagedModel<>(couponService.getAllCoupons(page, size, sortBy, order));
  }

  @PostMapping("/add")
  public ResponseEntity<Coupon> addNewCoupon(@Valid @RequestBody CouponDTO payload) {
    return ResponseEntity.ok().body(couponService.addNewCoupon(payload));
  }

  @PutMapping("/update")
  public ResponseEntity<Coupon> updateCoupon(@RequestBody CouponDTO payload) {
    return ResponseEntity.ok().body(couponService.updateCoupon(payload));
  }

  @DeleteMapping("/delete")
  public ResponseEntity<String> deleteCoupon(@RequestParam(value = "id") Integer id) {
    couponService.deleteById(id);
    return ResponseEntity.ok().body("Ok");
  }

  @PostMapping("/apply")
  public ResponseEntity<CouponAppliedDTO> applyCoupon(@RequestBody ApplyCouponRequest request) {

    return ResponseEntity.ok(couponService.applyCoupon(request.couponCode(), request.itemsTotal()));
  }

  @GetMapping("/available")
  public ResponseEntity<List<CouponDTO>> getApplicableCouponsForOrder(
      @RequestParam BigDecimal itemsTotal) {

    return ResponseEntity.ok(couponService.getApplicableCoupons(itemsTotal));
  }
}
