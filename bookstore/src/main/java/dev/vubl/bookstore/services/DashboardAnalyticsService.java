package dev.vubl.bookstore.services;

import dev.vubl.bookstore.dtos.CategoryTrendChartDTO;
import dev.vubl.bookstore.dtos.DashboardAnalyticsResponse;
import dev.vubl.bookstore.dtos.DateRangeResult;
import dev.vubl.bookstore.dtos.RevenueChartDTO;
import dev.vubl.bookstore.entities.DateRange;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
                  COALESCE(SUM(revenue) FILTER (WHERE order_status IN ('PENDING', 'PAID')), 0) AS total_revenue,
                  ROUND (COALESCE(
                      (SUM(revenue)
                      FILTER (WHERE order_status IN ('PENDING', 'PAID'))
                      )::numeric /
                      NULLIF(SUM(order_count) FILTER (WHERE order_status IN ('PENDING', 'PAID')), 0),
                      0
                  ), 0) AS aov,
                  COALESCE(SUM(total_items_sold) FILTER (WHERE order_status IN ('PENDING', 'PAID')), 0) AS total_items_sold,
                  COALESCE(SUM(order_count) FILTER (WHERE order_status IN ('PENDING', 'PAID')), 0) AS total_orders_count,
                  COALESCE(SUM(order_count) FILTER (WHERE order_status IN ('CANCELLED')), 0) AS cancelled_orders,
                  COALESCE(SUM(order_count) FILTER (WHERE order_status IN ('PENDING')), 0) AS pending_orders,
                  COALESCE(SUM(order_count) FILTER (WHERE order_status IN ('PAID')), 0) AS paid_orders
              FROM mv_order_dashboard
              WHERE order_date BETWEEN ? AND ?
              """;
      return jdbcTemplate.queryForObject(
          sql,
          (rs, rowNum) ->
              DashboardAnalyticsResponse.builder()
                  .totalRevenue(rs.getBigDecimal("total_revenue"))
                  .aov(rs.getBigDecimal("aov"))
                  .totalItemsSold(rs.getLong("total_items_sold"))
                  .totalOrdersCount(rs.getLong("total_orders_count"))
                  .paidOrders(rs.getLong("paid_orders"))
                  .cancelledOrders(rs.getLong("cancelled_orders"))
                  .pendingOrders(rs.getLong("pending_orders"))
                  .revenueChartData(getRevenueChartData(resolvedStartDate, resolvedEndDate))
                  .categoryTrendChartData(
                      getCategoryTrendChartData(resolvedStartDate, resolvedEndDate))
                  .build(),
          resolvedStartDate,
          resolvedEndDate);
    }

    String sql =
        """
                WITH current_period AS (
                    SELECT *
                    FROM mv_order_dashboard
                    WHERE order_date BETWEEN ? AND ?
                ),
                previous_period AS (
                    SELECT *
                    FROM mv_order_dashboard
                    WHERE order_date BETWEEN ? AND ?
                )
                SELECT
                    -- CURRENT
                    (
                        SELECT COALESCE(
                            SUM(revenue) FILTER (WHERE order_status IN ('PENDING', 'PAID')),
                            0
                        )
                        FROM current_period
                    ) AS total_revenue,
                    (
                        SELECT COALESCE(
                            SUM(order_count) FILTER (WHERE order_status IN ('PENDING', 'PAID')),
                            0
                        )
                        FROM current_period
                    ) AS total_orders_count,
                    ROUND(
                        COALESCE(
                            (
                                SELECT
                                    SUM(revenue) FILTER (WHERE order_status IN ('PENDING', 'PAID'))::numeric
                                    /
                                    NULLIF(
                                        SUM(order_count) FILTER (WHERE order_status IN ('PENDING', 'PAID')),
                                        0
                                    )
                                FROM current_period
                            ),
                            0
                        ),
                        0
                    ) AS aov,
                    (
                        SELECT COALESCE(
                            SUM(total_items_sold) FILTER (WHERE order_status IN ('PENDING', 'PAID')),
                            0
                        )
                        FROM current_period
                    ) AS total_items_sold,
                    (
                      SELECT COALESCE(SUM(order_count) FILTER (WHERE order_status IN ('CANCELLED')), 0)
                      FROM current_period
                    ) AS cancelled_orders,
                    (
                      SELECT COALESCE(SUM(order_count) FILTER (WHERE order_status IN ('PENDING')), 0)
                      FROM current_period
                    ) AS pending_orders,
                    (
                      SELECT COALESCE(SUM(order_count) FILTER (WHERE order_status IN ('PAID')), 0)
                      FROM current_period
                    ) AS paid_orders,
                    -- PREVIOUS
                    (
                        SELECT COALESCE(
                            SUM(revenue) FILTER (WHERE order_status IN ('PENDING', 'PAID')),
                            0
                        )
                        FROM previous_period
                    ) AS previous_total_revenue,
                    (
                        SELECT COALESCE(
                            SUM(order_count) FILTER (WHERE order_status IN ('PENDING', 'PAID')),
                            0
                        )
                        FROM previous_period
                    ) AS previous_total_orders_count,
                    ROUND(
                        COALESCE(
                            (
                                SELECT
                                    SUM(revenue) FILTER (WHERE order_status IN ('PENDING', 'PAID'))::numeric
                                    /
                                    NULLIF(
                                        SUM(order_count) FILTER (WHERE order_status IN ('PENDING', 'PAID')),
                                        0
                                    )
                                FROM previous_period
                            ),
                            0
                        ),
                        0
                    ) AS previous_aov,
                    (
                        SELECT COALESCE(
                            SUM(total_items_sold) FILTER (WHERE order_status IN ('PENDING', 'PAID')),
                            0
                        )
                        FROM previous_period
                    ) AS previous_total_items_sold;
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
                .paidOrders(rs.getLong("paid_orders"))
                .cancelledOrders(rs.getLong("cancelled_orders"))
                .pendingOrders(rs.getLong("pending_orders"))
                .revenueChartData(getRevenueChartData(resolvedStartDate, resolvedEndDate))
                .categoryTrendChartData(
                    getCategoryTrendChartData(resolvedStartDate, resolvedEndDate))
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
        case TODAY -> new DateRangeResult(today, today, today.minusDays(1), today.minusDays(1));
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

  private RevenueChartDTO getRevenueChartData(LocalDate startDate, LocalDate endDate) {
    String sql =
        """
                    WITH dates AS (
                        SELECT generate_series(
                            ?::date,
                            ?::date,
                            interval '1 day'
                        )::date AS order_date
                    )
                    SELECT
                        d.order_date,
                        COALESCE(SUM(m.revenue), 0) AS revenue,
                        COALESCE(SUM(m.order_count), 0) AS orders
                    FROM dates d
                    LEFT JOIN mv_order_dashboard m
                        ON d.order_date = m.order_date
                        AND m.order_status IN ('PAID', 'PENDING')
                    GROUP BY d.order_date
                    ORDER BY d.order_date;
                    """;
    List<String> labels = new ArrayList<>();
    List<BigDecimal> revenue = new ArrayList<>();
    List<Long> orders = new ArrayList<>();

    jdbcTemplate.query(
        sql,
        rs -> {
          labels.add(rs.getDate("order_date").toLocalDate().toString());
          revenue.add(rs.getBigDecimal("revenue"));
          orders.add(rs.getLong("orders"));
        },
        startDate,
        endDate);

    return RevenueChartDTO.builder().labels(labels).revenue(revenue).orders(orders).build();
  }

  private CategoryTrendChartDTO getCategoryTrendChartData(LocalDate startDate, LocalDate endDate) {
    String sql =
        """
            SELECT
                category_name,
                SUM(books_sold) AS books_sold
            FROM mv_books_sold_by_category
            WHERE order_date BETWEEN ? AND ?
            GROUP BY category_name
            ORDER BY books_sold DESC;
            """;

    List<String> labels = new ArrayList<>();
    List<Long> soldCount = new ArrayList<>();

    jdbcTemplate.query(
        sql,
        rs -> {
          labels.add(rs.getString("category_name"));
          soldCount.add(rs.getLong("books_sold"));
        },
        startDate,
        endDate);

    return CategoryTrendChartDTO.builder().labels(labels).soldCount(soldCount).build();
  }
}
