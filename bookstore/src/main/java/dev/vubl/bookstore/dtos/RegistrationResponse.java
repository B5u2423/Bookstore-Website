package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.ApplicationUser;
import lombok.Builder;

@Builder
public record RegistrationResponse(ApplicationUser createdUser) {}
