package dev.vubl.bookstore.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record ResetPasswordRequest(
    @NotNull(message = "Email không được bỏ trống") @Email(message = "Email không đúng định dạng")
        String email) {}
