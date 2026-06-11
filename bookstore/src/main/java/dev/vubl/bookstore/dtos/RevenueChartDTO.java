package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record RevenueChartDTO(List<String> labels, List<BigDecimal> revenue, List<Long> orders) {}
