package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Coupon;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Integer> {
  Optional<Coupon> findByCodeAndIsActiveTrue(String code);

  List<Coupon> findAllByIsActiveTrueAndMinOrderAmountLessThanEqual(BigDecimal amount);

  @Query(
      """
  SELECT c FROM Coupon c
  WHERE c.isActive = true
   AND c.minOrderAmount <= :itemsTotal
   AND c.validFrom <= :now
   AND c.validUntil >= :now
  """)
  List<Coupon> findValidCoupons(
      @Param("itemsTotal") BigDecimal amount, @Param("now") LocalDateTime now);
}
