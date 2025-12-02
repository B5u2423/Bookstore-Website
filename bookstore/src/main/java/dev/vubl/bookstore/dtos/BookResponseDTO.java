package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record BookResponseDTO(
    Integer id,
    String isbn,
    String title,
    String description,
    String author,
    BigDecimal price,
    Integer inStock,
    Integer publishYear,
    String publisher,
    Integer pageCount,
    String imageUrl) {}
