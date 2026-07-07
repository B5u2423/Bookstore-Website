package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.CouponAppliedDTO;
import dev.vubl.bookstore.dtos.CouponDTO;
import dev.vubl.bookstore.entities.Coupon;
import dev.vubl.bookstore.entities.DiscountType;
import dev.vubl.bookstore.mappers.CouponMapper;
import dev.vubl.bookstore.repos.CouponRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CouponService {
  private final CouponRepo couponRepo;

  public CouponAppliedDTO applyCoupon(String code, BigDecimal orderAmount) {
    Coupon coupon =
        couponRepo
            .findByCodeAndIsActiveTrue(code)
            .orElseThrow(() -> new IllegalArgumentException("Invalid coupon"));

    validateCoupon(coupon, orderAmount);

    BigDecimal discount = calculateDiscount(coupon, orderAmount);

    // increment count applied at checkout flow

    BigDecimal appliedTotal = orderAmount.subtract(discount);

    return CouponAppliedDTO.builder()
        .appliedItemsTotal(
            appliedTotal.compareTo(BigDecimal.ZERO) < 0 ? BigDecimal.ZERO : appliedTotal)
        .discountValue(discount)
        .build();
  }

  public Page<CouponDTO> getAllCouponsPaginated(int page, int size, String sortBy, String order) {
    List<String> allowed = List.of("id");
    if (!allowed.contains(sortBy)) {
      throw new IllegalArgumentException("Invalid sort field: %s".formatted(sortBy));
    }

    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return couponRepo.findAll(pageable).map(CouponMapper.INSTANCE::toDto);
  }

  public Coupon updateCoupon(CouponDTO payload) {
    Coupon c =
        couponRepo
            .findById(payload.id())
            .orElseThrow(
                () -> new IllegalArgumentException("Update OP: Coupon ID must not be null"));
    c.setIsActive(payload.isActive());
    c.setCode(payload.code());
    c.setDiscountType(payload.discountType());
    c.setDiscountValue(payload.discountValue());
    c.setMinOrderAmount(payload.minOrderAmount());
    c.setMinOrderAmount(payload.minOrderAmount());
    c.setMaxUses(payload.maxUses());
    c.setValidFrom(payload.validFrom());
    c.setValidUntil(payload.validUntil());
    c.setUpdateTimeStamp(Instant.now());

    return couponRepo.save(c);
  }

  public Coupon addNewCoupon(CouponDTO payload) {
    Coupon c = CouponMapper.INSTANCE.toEntity(payload);
    return couponRepo.save(c);
  }

  public void deleteById(Integer id) {
    if (couponRepo.existsById(id)) {
      couponRepo.deleteById(id);
      log.info("[{}] Coupon with id:{} deleted", this.getClass().getName(), id);
    }
  }

  public List<CouponDTO> getApplicableCoupons(BigDecimal itemsTotal) {
    return couponRepo.findValidCoupons(itemsTotal, LocalDateTime.now()).stream()
        .map(CouponMapper.INSTANCE::toDto)
        .toList();
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
    if (coupon.getDiscountType() == DiscountType.PERCENT) {
      return orderAmount
          .multiply(coupon.getDiscountValue())
          .divide(BigDecimal.valueOf(100), 0, RoundingMode.HALF_DOWN);
    }
    return coupon.getDiscountValue();
  }
}
