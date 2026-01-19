package dev.vubl.bookstore.dtos.dashboard;

public record CatalogHealthCountDTO(
    Long booksWithoutCategories,
    Long booksWithoutCollections,
    Long booksWithoutStock,
    Long booksWithoutCoverImage,
    Long booksAddedThisMonth,
    Long inactiveBooks,
    Long totalCategories,
    Long totalCollections,
    Long booksPerCategory) {}
