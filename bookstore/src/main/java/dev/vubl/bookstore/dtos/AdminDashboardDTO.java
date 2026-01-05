package dev.vubl.bookstore.dtos;

import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;

@Builder
public record AdminDashboardDTO(
    // orders
    Long totalOrders,
    Long ordersToday,
    Long ordersThisMonth,
    Long pendingOrders,
    Long completedOrders,
    Long cancelledOrders,

    // revenue
    BigDecimal totalRevenue,
    BigDecimal revenueToday,
    BigDecimal revenueThisMonth,
    BigDecimal averageOrderValue, // AOV = totalRevenue / totalOrders

    // users
    Long totalUsers,
    Long customersCount,
    Long staffCount,
    Long adminsCount,
    Long newUsersToday,
    Long newUsersThisMonth,

    // book and inventory
    Long totalBooks,
    Long activeBooks,
    Long outOfStockBooks,
    Long lowStockBooks,
    List<String> topSellingBooks,
    List<String> leastSellingBooks,

    // categories and collections
    Long totalCategories,
    Long totalCollections,
    Long booksPerCategory,
    String topCategoryBySales,
    String topCollectionBySales,

    // cart metrics
    Long activeCarts,
    Long abandonedCarts,
    Double cartAbandonmentRate, // abandonedCarts / totalCarts

    // engagement
    Long repeatCustomers,
    Double ordersPerCustomer,

    // order lifecycle
    Long ordersPending,
    Long ordersPaid,
    Long ordersShipped,
    Long ordersDelivered,
    Long averageOrderProcessingTime, // in minutes or seconds
    Long ordersAwaitingPayment,
    Long ordersAwaitingShipment,

    // content and catalog health
    Long booksWithoutCategories,
    Long booksWithoutCollections,
    Long booksWithoutStock,
    Long booksWithoutCoverImage,
    Long booksAddedThisMonth,
    Long inactiveBooks) {}
