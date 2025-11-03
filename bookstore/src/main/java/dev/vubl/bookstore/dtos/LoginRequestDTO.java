package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record LoginRequestDTO(String email, String password) {}
