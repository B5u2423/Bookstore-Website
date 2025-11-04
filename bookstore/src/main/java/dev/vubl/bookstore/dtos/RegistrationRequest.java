package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.UserType;
import lombok.Builder;

@Builder
public record RegistrationRequest(
    UserType userType, String firstName, String lastName, String email, String password) {}
