package dev.vubl.bookstore.repos;

import dev.vubl.bookstore.dtos.dashboard.OrderMetricsDTO;
import dev.vubl.bookstore.dtos.dashboard.RevenueMetricsDTO;
import dev.vubl.bookstore.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, Integer> {
  @Query(
"""
    SELECT new dev.vubl.bookstore.dtos.dashboard.OrderMetricsDTO(
        COUNT(o),
        SUM(CASE WHEN o.orderDate = CURRENT_DATE THEN 1 ELSE 0 END),
        SUM(CASE WHEN EXTRACT(YEAR FROM o.orderDate) = EXTRACT(YEAR FROM CURRENT_DATE)
                  AND EXTRACT(MONTH FROM o.orderDate) = EXTRACT(MONTH FROM CURRENT_DATE)
            THEN 1 ELSE 0 END),
        SUM(CASE WHEN o.orderStatus = dev.vubl.bookstore.entities.OrderStatus.PENDING THEN 1 ELSE 0 END),
        SUM(CASE WHEN o.orderStatus = dev.vubl.bookstore.entities.OrderStatus.SHIPPED THEN 1 ELSE 0 END),
        SUM(CASE WHEN o.orderStatus = dev.vubl.bookstore.entities.OrderStatus.CANCELLED THEN 1 ELSE 0 END)
    )
    FROM Order o
""")
  OrderMetricsDTO getOrderMetrics();

  @Query(
"""
    SELECT new dev.vubl.bookstore.dtos.dashboard.RevenueMetricsDTO(
        COALESCE(SUM(o.orderTotal), 0),
        COALESCE(SUM(CASE WHEN o.orderDate = CURRENT_DATE THEN o.orderTotal ELSE 0 END), 0),
        COALESCE(SUM(CASE WHEN EXTRACT(YEAR FROM o.orderDate) = EXTRACT(YEAR FROM CURRENT_DATE)
                           AND EXTRACT(MONTH FROM o.orderDate) = EXTRACT(MONTH FROM CURRENT_DATE)
                  THEN o.orderTotal ELSE 0 END), 0),
        COALESCE(SUM(o.orderTotal) / NULLIF(COUNT(o),0), 0)
    )
    FROM Order o
""")
  RevenueMetricsDTO getRevenueMetrics();
}
