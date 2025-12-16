package dev.vubl.bookstore.services;

import dev.vubl.bookstore.entities.*;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.repos.BookRepo;
import dev.vubl.bookstore.repos.CartRepo;
import dev.vubl.bookstore.repos.OrderRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
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

  public Order checkout(String token, ShippingInfoDTO shippingInfo) {
    Cart cart = cartService.getActiveCartByUser(token);
    ApplicationUser user = authService.readUserFromToken(token);

    Order order = new Order();
    order.setItems(new ArrayList<>());
    BigDecimal total = BigDecimal.ZERO;

    for (CartItem ci : cart.getItems()) {
      Book ciBook = ci.getBook();
      OrderItem oi =
          OrderItem.builder()
              .order(order)
              .book(ciBook)
              .titleAtPurchase(ciBook.getTitle())
              .isbn(ciBook.getIsbn())
              .quantity(ci.getQuantity())
              .priceAtPurchase(ciBook.getPrice())
              .build();

      // get book to update
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
      total = total.add(oi.getPriceAtPurchase().multiply(BigDecimal.valueOf(oi.getQuantity())));
    }
    // order meta data
    order.setPaymentMethod(shippingInfo.paymentMethod());
    order.setOrderDate(LocalDate.now());
    order.setNote(shippingInfo.info());
    order.setOrderStatus(OrderStatus.PENDING);
    if (shippingInfo.amount().compareTo(total) != 0) {
      throw new IllegalStateException("Order total is not the same !!!!");
    }
    // set address
    order.setTotalAmount(shippingInfo.amount());
    order.setCity(shippingInfo.communeName());
    order.setCommune(shippingInfo.cityName());
    order.setStreet(shippingInfo.street());
    // set user
    order.setFirstName(shippingInfo.firstName());
    order.setLastName(shippingInfo.lastName());
    order.setEmail(shippingInfo.email());
    order.setPhoneNumber(shippingInfo.phone());

    // save order
    Order savedOrder = orderRepo.save(order);

    // change cart status
    cart.setCartStatus(CartStatus.CHECKED_OUT);
    cartRepo.save(cart);

    return savedOrder;
  }
}
