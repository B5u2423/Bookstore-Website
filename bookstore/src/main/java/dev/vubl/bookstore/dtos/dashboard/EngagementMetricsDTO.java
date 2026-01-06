package dev.vubl.bookstore.dtos.dashboard;

import lombok.Builder;

@Builder
public record EngagementMetricsDTO(
    Long activeCarts, Double cartAbandonmentRate, Double ordersPerCustomer) {}
