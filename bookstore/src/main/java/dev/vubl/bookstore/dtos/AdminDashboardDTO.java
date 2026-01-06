package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.dtos.dashboard.*;
import lombok.Builder;

@Builder
public record AdminDashboardDTO(
    RevenueMetricsDTO revenueMetricsDTO,
    UserMetricsDTO userMetricsDTO,
    InventoryMetricsDTO inventoryMetricsDTO,
    CatalogHealthDTO catalogHealthDTO,
    EngagementMetricsDTO engagementMetricsDTO,
    OrderMetricsDTO orderMetricsDTO) {}
