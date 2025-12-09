package dev.vubl.bookstore.services;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ShippingInfoDTO(BigDecimal amount, String city, String commune, String street) {}
