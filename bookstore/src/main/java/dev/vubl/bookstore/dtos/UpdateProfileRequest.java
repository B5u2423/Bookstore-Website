package dev.vubl.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateProfileRequest(
    @NotBlank(message = "Trường không được bỏ trống") String name,
    @NotBlank(message = "Trường không được bỏ trống") String email,
    String phoneNumber) {}
