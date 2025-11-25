package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.ApplicationUser;
import dev.vubl.bookstore.entities.Cart;
import dev.vubl.bookstore.entities.CartStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepo extends JpaRepository<Cart, Integer> {
  Optional<Cart> findCartByUser(ApplicationUser user);

  Optional<Cart> findCartByUserAndCartStatus(ApplicationUser user, CartStatus status);
}
