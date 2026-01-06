package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.AdminDashboardDTO;
import dev.vubl.bookstore.dtos.dashboard.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
  private final OrderService orderService;
  private final ApplicationUserService userService;
  private final BookService bookService;

  public AdminDashboardDTO getDashboardData() {
    OrderMetricsDTO orderMetricsDTO = orderService.getOrderMetrics();
    RevenueMetricsDTO revenueMetricsDTO = null;
    UserMetricsDTO userMetricsDTO = userService.getUserMetrics();
    InventoryMetricsDTO inventoryMetricsDTO = null;
    CatalogHealthDTO catalogHealthDTO = null;
    EngagementMetricsDTO engagementMetricsDTO = null;
    return AdminDashboardDTO.builder()
        .orderMetricsDTO(orderMetricsDTO)
        //            .revenueMetricsDTO(revenueMetricsDTO)
        .userMetricsDTO(userMetricsDTO)
        //            .inventoryMetricsDTO(inventoryMetricsDTO)
        //            .catalogHealthDTO(catalogHealthDTO)
        //            .engagementMetricsDTO(engagementMetricsDTO)
        .build();
  }
}
