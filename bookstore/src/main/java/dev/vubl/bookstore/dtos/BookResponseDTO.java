package dev.vubl.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record BookResponseDTO(
    Integer id,
    String isbn,
    @NotBlank(message = "Tên sản phẩm không được bỏ trống") String title,
    String description,
    @NotBlank(message = "Tên tác giả không được bỏ trống") String author,
    BigDecimal price,
    Integer inStock,
    Integer categoryId,
    String categoryName,
    Integer publishYear,
    String publisher,
    String urlSlug,
    Integer pageCount,
    String imageUrl) {}
