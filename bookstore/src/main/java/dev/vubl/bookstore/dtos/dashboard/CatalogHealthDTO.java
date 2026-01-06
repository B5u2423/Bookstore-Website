package dev.vubl.bookstore.dtos.dashboard;

public record CatalogHealthDTO(
    Long booksWithoutCategories,
    Long booksWithoutCollections,
    Long booksWithoutStock,
    Long booksWithoutCoverImage,
    Long booksAddedThisMonth,
    Long inactiveBooks,
    Long totalCategories,
    Long totalCollections,
    Long booksPerCategory,
    String topCategoryBySales,
    String topCollectionBySales) {}
