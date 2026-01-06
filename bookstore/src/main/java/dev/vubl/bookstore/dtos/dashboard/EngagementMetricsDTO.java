package dev.vubl.bookstore.dtos.dashboard;

import lombok.Builder;

@Builder
public record EngagementMetricsDTO(
    Long activeCarts,
    Long abandonedCarts,
    Double cartAbandonmentRate,
    Long repeatCustomers,
    Double ordersPerCustomer) {}
