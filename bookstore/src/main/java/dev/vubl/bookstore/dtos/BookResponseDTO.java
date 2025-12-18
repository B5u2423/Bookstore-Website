package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record BookResponseDTO(
    Integer id,
    String isbn,
    String title,
    String description,
    String author,
    BigDecimal price,
    Integer inStock,
    Integer categoryId,
    String categoryName,
    Integer publishYear,
    String publisher,
    String urlSlug,
    Integer pageCount,
    String imageUrl,
    MultipartFile imageFile) {}
