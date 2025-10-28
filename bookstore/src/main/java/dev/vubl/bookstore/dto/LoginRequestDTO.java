package dev.vubl.bookstore.dto;

import lombok.Builder;

@Builder
public record LoginRequestDTO(String email, String password) {}
