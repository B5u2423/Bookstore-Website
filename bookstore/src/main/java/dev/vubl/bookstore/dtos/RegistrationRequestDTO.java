package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record RegistrationRequestDTO(
    String firstName, String lastName, String email, String password) {}
