package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record RegistrationResponse(String email, String firstName, String lastName) {}
