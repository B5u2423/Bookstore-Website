package dev.vubl.bookstore.entities;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.*;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Coupon extends BaseEntity {

  @Column(unique = true, nullable = false)
  private String code; // e.g. SAVE10

  @Enumerated(EnumType.STRING)
  private DiscountType discountType; // PERCENTAGE or FIXED

  @Column(name = "discount_value")
  private BigDecimal discountValue; // 10 or 100

  @Column(name = "min_order_amount")
  private BigDecimal minOrderAmount;

  @Column(name = "max_uses")
  private Integer maxUses;

  @Column(name = "used_count")
  private Integer usedCount;

  @Column(name = "valid_from")
  private LocalDateTime validFrom;

  @Column(name = "valid_until")
  private LocalDateTime validUntil;

  @Column(name = "is_active")
  private boolean isActive;
}
