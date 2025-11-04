package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record LoginRequest(String email, String password) {}
