package dev.vubl.bookstore.dtos;

import lombok.Builder;

@Builder
public record RegistrationResponseDTO(String email, String password) {}
