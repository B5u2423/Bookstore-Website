package dev.vubl.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record CollectionDTO(
    Integer id,
    @NotBlank(message = "Tên bộ sưu tập không được bỏ trống") String collectionName,
    String collectionSlug) {}
