package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record ExchangeCodeRequest(String exchangeCode) {}
