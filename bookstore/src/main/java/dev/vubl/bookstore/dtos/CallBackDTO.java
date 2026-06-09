package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record CallBackDTO(String vnpTxnRef, Boolean isCancelled) {}
