package dev.vubl.bookstore.dtos;

import java.util.List;
import lombok.Builder;

@Builder
public record LandingBookCollectionResponse(
    String collectionName, String collectionSlug, List<BookResponseDTO> list) {}
