package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.CouponAppliedDTO;
import dev.vubl.bookstore.dtos.ShippingInfoDTO;
import dev.vubl.bookstore.entities.*;
import dev.vubl.bookstore.exceptions.BookDoesNotExistException;
import dev.vubl.bookstore.exceptions.EmptyCartException;
import dev.vubl.bookstore.exceptions.OutOfStockException;
import dev.vubl.bookstore.mappers.BookMapper;
import dev.vubl.bookstore.mappers.OrderMapper;
import dev.vubl.bookstore.repos.BookRepo;
import dev.vubl.bookstore.repos.CartRepo;
import dev.vubl.bookstore.repos.OrderRepo;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class OrderService {
  private final OrderRepo orderRepo;
  private final CartService cartService;
  private final AuthService authService;
  private final CouponService couponService;
  private final CartRepo cartRepo;
  private final BookRepo bookRepo;

  private static final BigDecimal HANOI_SHIPPING_FEE = BigDecimal.valueOf(25000);
  private static final BigDecimal OTHERS_SHIPPING_FEE = BigDecimal.valueOf(50000);
  private static final BigDecimal FREE_SHIP_REQ = BigDecimal.valueOf(500000);

  public Order checkout(String token, ShippingInfoDTO shippingInfo) {
    Cart cart = cartService.getActiveCartByUser(token);
    if (cart.getItems().isEmpty()) {
      throw new EmptyCartException("Cart is empty");
    }

    // set order data
    Order o = OrderMapper.INSTANCE.toOrder(shippingInfo);
    o.setItems(new ArrayList<>());
    o.setOrderDate(LocalDate.now());
    o.setOrderStatus(OrderStatus.PENDING);

    BigDecimal tmpItemsTotal = BigDecimal.ZERO;
    BigDecimal tmpOrderTotal = BigDecimal.ZERO;
    BigDecimal tmpShippingFee = getShippingFee(shippingInfo.isFreeShip(), shippingInfo.cityId());

    for (CartItem ci : cart.getItems()) {
      // get book to update
      Book b =
          bookRepo
              .findByIdForUpdate(ci.getBook().getId())
              .orElseThrow(BookDoesNotExistException::new);

      // set order item data
      OrderItem oi = BookMapper.INSTANCE.toOrderItemEntity(b);
      oi.setQuantity(ci.getQuantity());
      oi.setOrder(o);

      // update in stock
      if (b.getInStock() < ci.getQuantity()) {
        throw new OutOfStockException(
            "Book with id::%d is not enough in stock".formatted(b.getId()));
      }
      Integer inStock = b.getInStock() - ci.getQuantity();
      b.setInStock(inStock);
      bookRepo.save(b);

      // calc total items value
      o.getItems().add(oi);
      tmpItemsTotal =
          tmpItemsTotal.add(oi.getPriceAtPurchase().multiply(BigDecimal.valueOf(oi.getQuantity())));
    }
    // apply coupon
    // TODO: handle coupon concurrency
    // TODO: add discountValue column to orders table
    if (shippingInfo.couponCode() != null && !shippingInfo.couponCode().isEmpty()) {
      CouponAppliedDTO applied =
          couponService.applyCoupon(shippingInfo.couponCode(), tmpItemsTotal);
      tmpItemsTotal = applied.appliedItemsTotal();
    }

    log.info("Validating items total...");
    if (shippingInfo.itemsTotal().compareTo(tmpItemsTotal) != 0) {
      throw new IllegalStateException("Mismatch Items total!");
    }

    tmpOrderTotal = tmpOrderTotal.add(tmpItemsTotal).add(tmpShippingFee);
    log.info("Validating order total...");
    if (shippingInfo.orderTotal().compareTo(tmpOrderTotal) != 0) {
      throw new IllegalStateException("Mismatch Order total!!!!");
    }

    // save order
    log.info("Saving order...");
    Order savedOrder = orderRepo.save(o);

    // change cart status
    log.info("Updating cart status...");
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

  public Page<Order> getOrdersByEmail(
      int page, int size, String sortBy, String order, String token) {
    ApplicationUser u = authService.readUserFromToken(token);
    List<String> allowed = List.of("id");
    if (!allowed.contains(sortBy)) {
      throw new IllegalArgumentException("Invalid sort field: %s".formatted(sortBy));
    }

    Sort sort = order.equals("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
    // if size is -1, fetch EVERYTHING unpaged
    Pageable pageable = (size == -1) ? Pageable.unpaged(sort) : PageRequest.of(page, size, sort);
    return orderRepo.findAllByEmail(u.getEmail(), pageable);
  }

  public String updateOrderStatus(String token, String vnpTxnRef, Boolean isCancelled) {
    // service method only for online order update.
    Optional<Order> o = orderRepo.findByVnpTxnRef(vnpTxnRef);
    if (o.isEmpty()) {
      return "Order with transaction ID does not exist";
    }
    ApplicationUser u = authService.readUserFromToken(token);
    if (!u.getEmail().equalsIgnoreCase(o.get().getEmail())) {
      return "Order does not match user";
    }
    if (isCancelled) {
      o.get().setOrderStatus(OrderStatus.CANCELLED);
    } else {
      // online banking success
      o.get().setOrderStatus(OrderStatus.PAID);
    }
    return "Status updated";
  }

  public String updateOrderStatusById(Integer orderId, OrderStatus status) {
    Optional<Order> o = orderRepo.findById(orderId);
    if (o.isEmpty()) {
      return "Order with ID %d does not exist".formatted(orderId);
    }
    o.get().setOrderStatus(status);
    return "Status updated";
  }

  public Map<String, BigDecimal> getShippingFeeInfo() {
    return Map.of(
        "HANOI",
        HANOI_SHIPPING_FEE,
        "OTHERS",
        OTHERS_SHIPPING_FEE,
        "FREE",
        BigDecimal.ZERO,
        "FREE_SHIP_REQ",
        FREE_SHIP_REQ);
  }

  // NOTE: This should have better eval but this is good for now
  // Shipping fee is based on city.
  // cityId == 1 >> Hanoi. cityId != 1 >> Others
  private BigDecimal getShippingFee(boolean isFreeShip, Integer cityId) {
    return isFreeShip
        ? BigDecimal.ZERO
        : cityId.equals(1) ? HANOI_SHIPPING_FEE : OTHERS_SHIPPING_FEE;
  }
}
