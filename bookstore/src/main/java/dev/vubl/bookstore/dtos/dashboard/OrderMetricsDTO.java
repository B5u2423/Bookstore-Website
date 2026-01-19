package dev.vubl.bookstore.dtos.dashboard;

import lombok.Builder;

@Builder
public record OrderMetricsDTO(
    Long totalOrders,
    Long ordersToday,
    Long ordersThisMonth,
    Long pendingOrders,
    Long completedOrders,
    Long cancelledOrders) {}
