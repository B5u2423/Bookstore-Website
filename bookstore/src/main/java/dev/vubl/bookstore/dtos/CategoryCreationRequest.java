package dev.vubl.bookstore.dtos;

import java.util.List;

public record CategoryCreationRequest(String categoryName, List<Integer> children) {}
