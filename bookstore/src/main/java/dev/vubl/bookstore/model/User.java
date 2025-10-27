package dev.vubl.bookstore.model;

public record User(
        String username,
        String password,
        String email
) {}
