package com.brahim.book_network_api.controller;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class RegistrationRequest {

    @NotEmpty(message = "firstname is required")
    @NotBlank(message = "firstname is required")
    private String firstname;

    @NotEmpty(message = "lastname is required")
    @NotBlank(message = "lastname is required")
    private String lastname;

    @Email(message = "Email is not valid")
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;
}
