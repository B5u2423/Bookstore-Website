package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.Author;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record BookResponseDTO(
    Integer id,
    String isbn,
    String title,
    String description,
    List<Author> authors,
    BigDecimal price,
    Integer inStock,
    String productCode,
    Integer publishYear,
    String language,
    Integer weightGrams,
    String dimensions,
    Integer pageCount,
    String format,
    String imageUrl) {}
