package dev.vubl.bookstore.dtos.dashboard;

import java.util.List;
import lombok.Builder;

@Builder
public record InventoryMetricsDTO(
    Long totalBooks,
    Long activeBooks,
    Long outOfStockBooks,
    Long lowStockBooks,
    List<String> topSellingBooks,
    List<String> leastSellingBooks) {}
