package dev.vubl.bookstore.dtos.dashboard;

import lombok.Builder;

@Builder
public record CatalogHealthDTO(
    Long booksWithoutCategories,
    Long booksWithoutCollections,
    Long booksWithoutStock,
    Long booksWithoutCoverImage,
    Long booksAddedThisMonth,
    Long inactiveBooks,
    Long totalCategories,
    Long totalCollections,
    Long booksPerCategory
    //    String topCategoryBySales,
    //    String topCollectionBySales
    ) {}
