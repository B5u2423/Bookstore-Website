package dev.vubl.bookstore.dtos;

import java.time.LocalDate;

public record DateRangeResult(
    LocalDate startDate, LocalDate endDate, LocalDate prevStartDate, LocalDate prevEndDate) {}
