package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record CartItemDto(
    String bookAuthor,
    Integer quantity,
    Integer bookId,
    String bookSlug,
    String bookTitle,
    String bookPrice,
    String bookImage) {}
