package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record RefreshRequest(String refreshToken) {}
