package dev.vubl.bookstore.dtos;

import java.util.List;
import lombok.Builder;

@Builder
public record CategoryTrendChartDTO(List<String> labels, List<Long> soldCount) {}
