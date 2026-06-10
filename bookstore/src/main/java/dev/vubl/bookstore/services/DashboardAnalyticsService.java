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
    LocalDate resolvedPrevStartDate = resolved.prevStartDate();
    LocalDate resolvedPrevEndDate = resolved.prevEndDate();

    if (resolved.prevEndDate() == LocalDate.MIN && resolved.prevStartDate() == LocalDate.MIN) {
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

    String sql =
        """
                WITH current_period AS (
                    SELECT *
                    FROM mv_order_dashboard
                    WHERE order_status IN ('PENDING', 'PAID')
                      AND order_date BETWEEN ? AND ?
                ),
                previous_period AS (
                    SELECT *
                    FROM mv_order_dashboard
                    WHERE order_status IN ('PENDING', 'PAID')
                      AND order_date BETWEEN ? AND ?
                )
                SELECT
                    -- CURRENT
                    (SELECT COALESCE(SUM(revenue), 0) FROM current_period) AS total_revenue,
                    (SELECT COALESCE(SUM(order_count), 0) FROM current_period) AS total_orders_count,
                    ROUND(
                        COALESCE(
                            (SELECT SUM(revenue) FROM current_period)::numeric /
                            NULLIF((SELECT SUM(order_count) FROM current_period), 0),
                        0),
                    0) AS aov,
                    (SELECT COALESCE(SUM(total_items_sold), 0) FROM current_period) AS total_items_sold,
                    -- PREVIOUS
                    (SELECT COALESCE(SUM(revenue), 0) FROM previous_period) AS previous_total_revenue,
                    (SELECT COALESCE(SUM(order_count), 0) FROM previous_period) AS previous_total_orders_count,
                    ROUND(
                        COALESCE(
                            (SELECT SUM(revenue) FROM previous_period)::numeric /
                            NULLIF((SELECT SUM(order_count) FROM previous_period), 0),
                        0),
                    0) AS previous_aov,
                    (SELECT COALESCE(SUM(total_items_sold), 0) FROM previous_period) AS previous_total_items_sold;
        """;
    return jdbcTemplate.queryForObject(
        sql,
        (rs, rowNum) ->
            DashboardAnalyticsResponse.builder()
                .totalRevenue(rs.getBigDecimal("total_revenue"))
                .aov(rs.getBigDecimal("aov"))
                .totalItemsSold(rs.getLong("total_items_sold"))
                .totalOrdersCount(rs.getLong("total_orders_count"))
                .prevTotalRevenue(rs.getBigDecimal("previous_total_revenue"))
                .prevAov(rs.getBigDecimal("previous_aov"))
                .prevTotalItemsSold(rs.getLong("previous_total_items_sold"))
                .prevTotalOrdersCount(rs.getLong("previous_total_orders_count"))
                .build(),
        resolvedStartDate,
        resolvedEndDate,
        resolvedPrevStartDate,
        resolvedPrevEndDate);
  }

  private DateRangeResult resolveDateRange(
      DateRange range, LocalDate startDate, LocalDate endDate) {

    if (range != DateRange.CUSTOM) {

      LocalDate today = LocalDate.now();

      return switch (range) {
        case TODAY -> new DateRangeResult(today, today, LocalDate.MIN, LocalDate.MIN);
        case LAST_7_DAYS ->
            new DateRangeResult(today.minusDays(6), today, LocalDate.MIN, LocalDate.MIN);
        case LAST_30_DAYS ->
            new DateRangeResult(today.minusDays(29), today, LocalDate.MIN, LocalDate.MIN);
        case THIS_WEEK -> {
          LocalDate thisWeekStart = today.with(java.time.DayOfWeek.MONDAY);
          LocalDate lastWeekStart = thisWeekStart.minusWeeks(1);
          LocalDate lastWeekEnd = thisWeekStart.minusDays(1);

          yield new DateRangeResult(thisWeekStart, today, lastWeekStart, lastWeekEnd);
        }
        case THIS_MONTH -> {
          LocalDate thisMonthStart =
              today.with(java.time.temporal.TemporalAdjusters.firstDayOfMonth());
          LocalDate lastMonthStart = thisMonthStart.minusMonths(1);
          LocalDate lastMonthEnd = thisMonthStart.minusDays(1);

          yield new DateRangeResult(thisMonthStart, today, lastMonthStart, lastMonthEnd);
        }
        default -> throw new IllegalArgumentException("Unsupported range: " + range);
      };
    }

    if (startDate == null || endDate == null) {
      throw new IllegalArgumentException("startDate and endDate are required when range is CUSTOM");
    }

    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("startDate must be before endDate");
    }

    return new DateRangeResult(startDate, endDate, LocalDate.MIN, LocalDate.MIN);
  }
}
