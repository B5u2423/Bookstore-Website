package dev.vubl.bookstore.dtos;

import dev.vubl.bookstore.entities.UserType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record RegistrationRequest(
    UserType userType,
    @NotBlank(message = "Trường tên không được bỏ trống") String name,
    @NotBlank(message = "Trường email không được bỏ trống")
        @Email(message = "Email không đúng định dạng")
        String email,
    @NotBlank(message = "Mật khẩu không được bỏ trống") String password) {}
