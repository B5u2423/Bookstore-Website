package dev.vubl.bookstore.controllers;

import dev.vubl.bookstore.dtos.CallBackDTO;
import dev.vubl.bookstore.dtos.ShippingInfoDTO;
import dev.vubl.bookstore.entities.Order;
import dev.vubl.bookstore.services.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
  private final OrderService orderService;

  @PostMapping("/create-order")
  @PreAuthorize("hasRole('ROLE_CUSTOMER')")
  public ResponseEntity<Order> placeOrder(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody ShippingInfoDTO shippingInfo) {
    return ResponseEntity.ok().body(orderService.checkout(token, shippingInfo));
  }

  @PostMapping("/update-status")
  @PreAuthorize("hasRole('ROLE_CUSTOMER')")
  public ResponseEntity<String> updateOrderStatus(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token,
      @RequestBody CallBackDTO callBackDTO) {
    return ResponseEntity.ok()
        .body(
            orderService.updateOrderStatus(
                token, callBackDTO.vnpTxnRef(), callBackDTO.isCancelled()));
  }

  @GetMapping
  public PagedModel<Order> getAllOrdersPaginated(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "5") int size,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "order", defaultValue = "asc") String order) {
    return new PagedModel<>(orderService.getAllOrdersPaginated(page, size, sortBy, order));
  }

  @GetMapping("/user")
  public PagedModel<Order> getOrdersByEmail(
      @RequestParam(value = "page", defaultValue = "0") int page,
      @RequestParam(value = "size", defaultValue = "10") int size,
      @RequestParam(value = "sortBy", defaultValue = "id") String sortBy,
      @RequestParam(value = "order", defaultValue = "asc") String order,
      @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
    return new PagedModel<>(orderService.getOrdersByEmail(page, size, sortBy, order, token));
  }
}
