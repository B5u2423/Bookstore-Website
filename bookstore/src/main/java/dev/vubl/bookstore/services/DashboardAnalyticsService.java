package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.DashboardAnalyticsResponse;
import dev.vubl.bookstore.dtos.DateRangeResult;
import dev.vubl.bookstore.entities.DateRange;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardAnalyticsService {
  private final JdbcTemplate jdbcTemplate;

  public DashboardAnalyticsResponse getAnalytics(
      DateRange range, LocalDate startDate, LocalDate endDate) {
    DateRangeResult resolved = resolveDateRange(range, startDate, endDate);
    LocalDate resolvedStartDate = resolved.startDate();
    LocalDate resolvedEndDate = resolved.endDate();

    String sql =
        """
        SELECT
            COALESCE(SUM(revenue), 0) AS total_revenue,
            ROUND (COALESCE(
                SUM(revenue)::numeric /
                NULLIF(SUM(order_count), 0),
                0
            ), 0) AS aov,
            COALESCE(SUM(total_items_sold), 0) AS total_items_sold,
            COALESCE(SUM(order_count), 0) AS total_orders_count
        FROM mv_order_dashboard
        WHERE order_date BETWEEN ? AND ?
        AND order_status IN ('PENDING', 'PAID')
        """;
    return jdbcTemplate.queryForObject(
        sql,
        (rs, rowNum) ->
            DashboardAnalyticsResponse.builder()
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .aov(rs.getBigDecimal("aov"))
                .totalItemsSold(rs.getLong("total_items_sold"))
                .totalOrdersCount(rs.getLong("total_orders_count"))
                .build(),
        resolvedStartDate,
        resolvedEndDate);
  }

  private DateRangeResult resolveDateRange(
      DateRange range, LocalDate startDate, LocalDate endDate) {

    if (range != DateRange.NONE) {

      LocalDate today = LocalDate.now();

      return switch (range) {
        case LAST_7_DAYS -> new DateRangeResult(today.minusDays(6), today);

        case LAST_30_DAYS -> new DateRangeResult(today.minusDays(29), today);

        default -> throw new IllegalArgumentException("Unsupported range: " + range);
      };
    }

    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("startDate and endDate are required when range is NONE");
    }

    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("startDate must be before endDate");
    }

    return new DateRangeResult(startDate, endDate);
  }
}
