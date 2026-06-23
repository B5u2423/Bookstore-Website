package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;

public record ApplyCouponRequest(String couponCode, BigDecimal itemsTotal) {}
