package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record VnPayRequest(BigDecimal amount, String info) {}
