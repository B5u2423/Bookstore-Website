package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Builder;

@Builder
public record CouponDTO(
    Integer id,
    @NotBlank(message = "Mã giảm giá không được bỏ trống") String code,
    @NotNull(message = "Loại mã không được bỏ trống") DiscountType discountType,
    @NotNull(message = "Mức giảm không được bỏ trống") BigDecimal discountValue,
    @NotNull(message = "Giá trị đơn hàng tói thiểu không được bỏ trống") BigDecimal minOrderAmount,
    @NotNull(message = "Số lần dùng không được bỏ trống") Integer maxUses,
    Integer usedCount,
    @NotNull(message = "Ngày áp dụng không được bỏ trống") LocalDateTime validFrom,
    @NotNull(message = "Hạn sử dụng không được bỏ trống") LocalDateTime validUntil,
    Boolean isActive) {}
