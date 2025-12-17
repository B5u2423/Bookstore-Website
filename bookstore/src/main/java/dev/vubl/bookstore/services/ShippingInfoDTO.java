package dev.vubl.bookstore.services;

import dev.vubl.bookstore.entities.PaymentMethod;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ShippingInfoDTO(
    String firstName,
    String lastName,
    String email,
    String phone,
    PaymentMethod paymentMethod,
    BigDecimal amount,
    String cityName,
    Integer cityId,
    Integer communeId,
    String communeName,
    String street,
    String info) {}
