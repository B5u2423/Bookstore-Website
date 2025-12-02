package dev.vubl.bookstore.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateProfileRequest(
    @NotBlank(message = "Trường không được bỏ trống") String firstName,
    @NotBlank(message = "Trường không được bỏ trống") String lastName,
    @NotBlank(message = "Trường không được bỏ trống") String email,
    @NotBlank(message = "Trường không được bỏ trống") String phoneNumber) {}
