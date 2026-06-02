package dev.vubl.bookstore.dtos.dashboard;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public record RevenueMetricsDTO(
    BigDecimal totalRevenue,
    BigDecimal revenueToday,
    BigDecimal revenueThisMonth,
    BigDecimal averageOrderValue // AOV = totalRevenue / totalOrders
    ) {}
