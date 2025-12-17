package dev.vubl.bookstore.dtos;

import java.util.List;

public record CategoryUpdateRequest(Integer id, String categoryName, List<Integer> children) {}
