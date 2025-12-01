package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record LoginResponse(String token, String refresh) {}
