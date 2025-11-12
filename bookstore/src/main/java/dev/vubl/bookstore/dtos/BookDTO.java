package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.Author;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record BookDTO(
    String isbn,
    String title,
    List<Author> authors,
    String description,
    BigDecimal price,
    int inStock) {}
