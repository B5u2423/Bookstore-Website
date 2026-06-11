package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record DashboardAnalyticsResponse(
    BigDecimal totalRevenue,
    BigDecimal aov,
    Long totalItemsSold,
    Long totalOrdersCount,
    BigDecimal prevTotalRevenue,
    BigDecimal prevAov,
    Long prevTotalItemsSold,
    Long prevTotalOrdersCount,
    Long cancelledOrders,
    Long pendingOrders,
    Long paidOrders,
    RevenueChartDTO revenueChartData,
    CategoryTrendChartDTO categoryTrendChartData) {}
