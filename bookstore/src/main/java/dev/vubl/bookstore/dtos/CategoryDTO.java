package dev.vubl.bookstore.dtos;

import java.util.List;
import lombok.Builder;

@Builder
public record CategoryDTO(
    Integer id,
    String categoryName,
    String categorySlug,
    Integer parent,
    List<CategoryDTO> children) {}
