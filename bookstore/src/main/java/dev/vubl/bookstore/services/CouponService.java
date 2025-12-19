package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.CouponDTO;
import dev.vubl.bookstore.entities.Coupon;
import dev.vubl.bookstore.entities.DiscountType;
import dev.vubl.bookstore.repos.CouponRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {
  private final CouponRepo couponRepo;

  public BigDecimal applyCoupon(String code, BigDecimal orderAmount) {
    Coupon coupon =
        couponRepo
            .findByCodeAndIsActiveTrue(code)
            .orElseThrow(() -> new IllegalArgumentException("Invalid coupon"));

    validateCoupon(coupon, orderAmount);

    BigDecimal discount = calculateDiscount(coupon, orderAmount);

    coupon.setUsedCount(coupon.getUsedCount() + 1);
    couponRepo.save(coupon);

    return orderAmount.subtract(discount);
  }

  public List<Coupon> getAllCoupons() {
    return couponRepo.findAll();
  }

  public void updateCoupon(CouponDTO payload) {
    Coupon c =
        couponRepo
            .findById(payload.id())
            .orElseThrow(() -> new IllegalArgumentException("Coupon ID must not be null"));
    c.setActive(payload.isActive());
    c.setCode(payload.code());
    c.setDiscountType(payload.discountType());
    c.setDiscountValue(payload.discountValue());
    c.setMinOrderAmount(payload.minOrderAmount());
    c.setMinOrderAmount(payload.minOrderAmount());
    c.setMaxUses(payload.maxUses());
    c.setValidFrom(payload.validFrom());
    c.setValidUntil(payload.validUntil());

    couponRepo.save(c);
  }

  public Coupon addNewCoupon(CouponDTO payload) {
    Coupon c =
        Coupon.builder()
            .code(payload.code())
            .discountType(payload.discountType())
            .discountValue(payload.discountValue())
            .minOrderAmount(payload.minOrderAmount())
            .maxUses(payload.maxUses())
            .isActive(false)
            .validFrom(payload.validFrom())
            .validUntil(payload.validUntil())
            .usedCount(0)
            .build();
    return couponRepo.save(c);
  }

  private void validateCoupon(Coupon coupon, BigDecimal orderAmount) {
    LocalDateTime now = LocalDateTime.now();

    if (now.isBefore(coupon.getValidFrom()) || now.isAfter(coupon.getValidUntil()))
      throw new IllegalArgumentException("Coupon expired");

    if (coupon.getUsedCount() >= coupon.getMaxUses())
      throw new IllegalArgumentException("Coupon usage limit reached");

    if (orderAmount.compareTo(coupon.getMinOrderAmount()) < 0)
      throw new IllegalArgumentException("Minimum order not met");
  }

  private BigDecimal calculateDiscount(Coupon coupon, BigDecimal orderAmount) {
    if (coupon.getDiscountType() == DiscountType.PERCENTAGE) {
      return orderAmount.multiply(coupon.getDiscountValue()).divide(BigDecimal.valueOf(100));
    }
    return coupon.getDiscountValue();
  }
}
