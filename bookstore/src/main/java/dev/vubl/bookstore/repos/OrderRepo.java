package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Order;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
  Page<Order> findAllByEmail(String email, Pageable pageable);

  Optional<Order> findByVnpTxnRef(String vnpTxnRef);
}
