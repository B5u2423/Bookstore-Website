package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record CouponAppliedDTO(BigDecimal appliedItemsTotal, BigDecimal discountValue) {}
