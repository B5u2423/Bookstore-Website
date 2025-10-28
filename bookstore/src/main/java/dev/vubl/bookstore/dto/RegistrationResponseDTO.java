package dev.vubl.bookstore.dto;

import lombok.Builder;

@Builder
public record RegistrationResponseDTO(String email, String password) {}
