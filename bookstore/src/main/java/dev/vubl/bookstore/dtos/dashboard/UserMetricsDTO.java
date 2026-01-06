package dev.vubl.bookstore.dtos.dashboard;

import lombok.Builder;

@Builder
public record UserMetricsDTO(
    Long totalUsers,
    Long customersCount,
    Long staffCount,
    Long adminsCount,
    Long newUsersToday,
    Long newUsersThisMonth) {}
