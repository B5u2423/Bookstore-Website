package dev.vubl.bookstore.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record BookDTO(
    Long id,
    String isbn,
    String title,
    String description,
    BigDecimal price,
    int inStock,
    String image) {}
