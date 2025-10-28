package dev.vubl.bookstore.dto;

import lombok.Builder;

@Builder
public record RegistrationRequestDTO(
    String firstName, String lastName, String email, String password) {}
