package com.pushkar.netflix.dto;

import java.time.LocalDate;

import com.pushkar.netflix.entity.Genre;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record MovieRequest(

		@NotBlank(message = "title is required")
		@Size(max = 150, message = "title must be at most 150 characters")
		String title,

		@NotBlank(message = "description is required")
		@Size(max = 1000, message = "description must be at most 1000 characters")
		String description,

		@Min(value = 1888, message = "releaseYear must be a valid year")
		@Max(value = 2100, message = "releaseYear must be a valid year")
		int releaseYear,

		@NotNull(message = "genre is required")
		Genre genre,

		@DecimalMin(value = "0.0", message = "rating cannot be negative")
		@DecimalMax(value = "10.0", message = "rating cannot exceed 10")
		double rating,

		@Min(value = 1, message = "durationMinutes must be positive")
		int durationMinutes,

		@NotNull(message = "releaseDate is required")
		LocalDate releaseDate) {
}