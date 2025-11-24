package dev.vubl.bookstore.services;

import dev.vubl.bookstore.entities.*;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.repos.BookRepo;
import dev.vubl.bookstore.repos.CartRepo;
import dev.vubl.bookstore.repos.OrderRepo;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepo orderRepo;
  private final CartService cartService;
  private final AuthService authService;
  private final CartRepo cartRepo;
  private final BookRepo bookRepo;

  public Order checkout(String token) {
    Cart cart = cartService.getActiveCartByUser(token);
    ApplicationUser user = authService.readUserFromToken(token);

    Order order = new Order();
    order.setCustomer(user);
    order.setItems(new ArrayList<>());

    for (CartItem ci : cart.getItems()) {
      OrderItem oi =
          OrderItem.builder()
              .order(order)
              .book(ci.getBook())
              .quantity(ci.getQuantity())
              .price(ci.getBook().getPrice())
              .build();
      Optional<Book> b = bookRepo.findById(ci.getBook().getId());
      if (b.isEmpty()) {
        throw new BookDoesNotExistException();
      }
      // update in stock
      Integer inStock = b.get().getInStock() - ci.getQuantity();
      b.get().setInStock(inStock);
      bookRepo.save(b.get());

      // add to order
      order.getItems().add(oi);
    }

    // save order
    Order savedOrder = orderRepo.save(order);

    // change cart status
    cart.setCartStatus(CartStatus.CHECKED_OUT);
    cartRepo.save(cart);

    return savedOrder;
  }
}
