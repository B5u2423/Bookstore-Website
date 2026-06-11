package dev.vubl.bookstore.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record RevenueChartDTO(
       List<String> labels,
       List<BigDecimal> revenue,
       List<Long> orders
) {
}
