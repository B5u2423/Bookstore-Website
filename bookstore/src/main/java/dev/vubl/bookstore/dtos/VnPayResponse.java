package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record VnPayResponse(String code, String message, String paymentUrl) {}
