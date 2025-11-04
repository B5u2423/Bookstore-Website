package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.ApplicationUser;
import lombok.Builder;

@Builder
public record LoginResponse(ApplicationUser user, String token, String refresh) {}
