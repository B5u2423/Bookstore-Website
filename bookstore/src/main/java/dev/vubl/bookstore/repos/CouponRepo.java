package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Coupon;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Integer> {
  Optional<Coupon> findByCodeAndIsActiveTrue(String code);
}
