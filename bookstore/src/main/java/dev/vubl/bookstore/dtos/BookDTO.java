package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record BookDTO(
    String isbn, String title, String description, BigDecimal price, int inStock) {}
