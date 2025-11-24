package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.entities.Book;
import dev.vubl.bookstore.entities.Cart;
import dev.vubl.bookstore.entities.CartItem;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepo extends JpaRepository<CartItem, Integer> {
  Optional<CartItem> findByBookAndCart(Book book, Cart cart);
}
