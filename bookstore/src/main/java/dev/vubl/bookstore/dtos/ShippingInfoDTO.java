package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.PaymentMethod;
import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record ShippingInfoDTO(
    String name,
    String email,
    String phone,
    PaymentMethod paymentMethod,
    BigDecimal itemsTotal,
    BigDecimal shippingFee,
    BigDecimal orderTotal,
    String cityName,
    Integer cityId,
    Integer communeId,
    String communeName,
    String street,
    String info,
    String couponCode,
    String vnpTxnRef) {}
