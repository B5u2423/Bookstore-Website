package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.ShippingInfoDTO;
import dev.vubl.bookstore.dtos.dashboard.OrderMetricsDTO;
import dev.vubl.bookstore.entities.*;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.repos.BookRepo;
import dev.vubl.bookstore.repos.CartRepo;
import dev.vubl.bookstore.repos.OrderRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepo orderRepo;
  private final CartService cartService;
  private final AuthService authService;
  private final CouponService couponService;
  private final CartRepo cartRepo;
  private final BookRepo bookRepo;

  public Order checkout(String token, ShippingInfoDTO shippingInfo) {
    Cart cart = cartService.getActiveCartByUser(token);
    ApplicationUser user = authService.readUserFromToken(token);

    Order order = new Order();
    order.setItems(new ArrayList<>());
    BigDecimal itemsTotal = BigDecimal.ZERO;
    BigDecimal orderTotal = BigDecimal.ZERO;

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
      itemsTotal =
          itemsTotal.add(oi.getPriceAtPurchase().multiply(BigDecimal.valueOf(oi.getQuantity())));
    }
    // apply coupon
    if (shippingInfo.couponCode() != null && !shippingInfo.couponCode().isEmpty()) {
      itemsTotal = couponService.applyCoupon(shippingInfo.couponCode(), itemsTotal);
    }
    orderTotal = itemsTotal.add(shippingInfo.shippingFee());
    // order meta data
    order.setPaymentMethod(shippingInfo.paymentMethod());
    order.setOrderDate(LocalDate.now());
    order.setNote(shippingInfo.info());
    order.setOrderStatus(OrderStatus.PENDING);
    order.setShippingFee(shippingInfo.shippingFee());
    if (shippingInfo.itemsTotal().compareTo(itemsTotal) != 0) {
      throw new IllegalStateException("Item totals is not the same");
    }
    order.setItemsTotal(shippingInfo.itemsTotal());
    if (shippingInfo.orderTotal().compareTo(orderTotal) != 0) {
      throw new IllegalStateException("Order total is not the same !!!!");
    }
    order.setOrderTotal(shippingInfo.orderTotal());

    // set address
    order.setCity(shippingInfo.cityName());
    order.setCommune(shippingInfo.communeName());
    order.setStreet(shippingInfo.street());
    // set user
    order.setName(shippingInfo.name());
    order.setEmail(shippingInfo.email());
    order.setPhoneNumber(shippingInfo.phone());

    // save order
    Order savedOrder = orderRepo.save(order);

    // change cart status
    cart.setCartStatus(CartStatus.CHECKED_OUT);
    cartRepo.save(cart);

    return savedOrder;
  }

  public Page<Order> getAllOrdersPaginated(int page, int size, String sortBy, String order) {
    List<String> allowed = List.of("id");
    if (!allowed.contains(sortBy)) {
      throw new IllegalArgumentException("Invalid sort field: %s".formatted(sortBy));
    }

    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    Pageable pageable = PageRequest.of(page, size, sort);
    return orderRepo.findAll(pageable);
  }

  public OrderMetricsDTO getOrderMetrics() {
    return orderRepo.getOrderMetrics();
  }
}
