package com.pushkar.netflix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AuthRequest(

		@NotBlank(message = "email is required")
		@Email(message = "email must be a valid address")
		String email,

		@NotBlank(message = "password is required")
		@Size(min = 6, message = "password must be at least 6 characters")
		String password) {
}