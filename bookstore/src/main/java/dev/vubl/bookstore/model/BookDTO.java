package dev.vubl.bookstore.model;

import java.math.BigDecimal;

public record BookDTO(
    Long id,
    String isbn,
    String title,
    String description,
    BigDecimal price,
    int inStock,
    String image) {}
