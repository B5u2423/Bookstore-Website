package dev.vubl.bookstore.dtos;

import java.util.List;
import lombok.Builder;

@Builder
public record CartDto(String cartId, List<CartItemDto> items, Integer userId, String cartStatus) {}
