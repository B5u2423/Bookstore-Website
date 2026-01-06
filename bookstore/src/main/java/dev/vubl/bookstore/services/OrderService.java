package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.ShippingInfoDTO;
import dev.vubl.bookstore.dtos.dashboard.OrderMetricsDTO;
import dev.vubl.bookstore.dtos.dashboard.RevenueMetricsDTO;
import dev.vubl.bookstore.entities.*;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.exceptions.EmptyCartException;
import dev.vubl.bookstore.exceptions.OutOfStockException;
import dev.vubl.bookstore.repos.BookRepo;
import dev.vubl.bookstore.repos.CartRepo;
import dev.vubl.bookstore.repos.OrderRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
    if (cart.getItems().isEmpty()) {
      throw new EmptyCartException("Cart is empty");
    }

    Order order = new Order();
    order.setItems(new ArrayList<>());
    BigDecimal itemsTotal = BigDecimal.ZERO;

    for (CartItem ci : cart.getItems()) {
      // get book to update
      Book b =
          bookRepo
              .findByIdForUpdate(ci.getBook().getId())
              .orElseThrow(BookDoesNotExistException::new);
      OrderItem oi =
          OrderItem.builder()
              .order(order)
              .book(b)
              .titleAtPurchase(b.getTitle())
              .isbn(b.getIsbn())
              .quantity(ci.getQuantity())
              .priceAtPurchase(b.getPrice())
              .build();

      // update in stock
      if (b.getInStock() < ci.getQuantity()) {
        throw new OutOfStockException(
            "Book with id::%d is not enough in stock".formatted(b.getId()));
      }
      Integer inStock = b.getInStock() - ci.getQuantity();
      b.setInStock(inStock);
      bookRepo.save(b);

      // add to order
      order.getItems().add(oi);
      itemsTotal =
          itemsTotal.add(oi.getPriceAtPurchase().multiply(BigDecimal.valueOf(oi.getQuantity())));
    }
    // apply coupon
    // TODO: handle coupon concurrency
    if (shippingInfo.couponCode() != null && !shippingInfo.couponCode().isEmpty()) {
      itemsTotal = couponService.applyCoupon(shippingInfo.couponCode(), itemsTotal);
    }

    BigDecimal orderTotal = itemsTotal.add(shippingInfo.shippingFee());
    // order meta data
    order.setPaymentMethod(shippingInfo.paymentMethod());
    order.setOrderDate(LocalDate.now());
    order.setNote(shippingInfo.info());
    order.setOrderStatus(OrderStatus.PENDING);
    order.setShippingFee(shippingInfo.shippingFee());
    if (shippingInfo.itemsTotal().compareTo(itemsTotal) != 0) {
      throw new IllegalStateException("Item totals is not the same");
    }
    order.setItemsTotal(itemsTotal);
    if (shippingInfo.orderTotal().compareTo(orderTotal) != 0) {
      throw new IllegalStateException("Order total is not the same !!!!");
    }
    order.setOrderTotal(orderTotal);

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

  public RevenueMetricsDTO getRevenueMetrics() {
    return orderRepo.getRevenueMetrics();
  }
}
