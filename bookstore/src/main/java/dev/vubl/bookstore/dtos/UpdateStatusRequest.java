package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.OrderStatus;
import lombok.Builder;

@Builder
public record UpdateStatusRequest(Integer orderId, OrderStatus status) {}
