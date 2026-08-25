package com.pushkar.netflix.dto;

import java.time.LocalDate;

import com.pushkar.netflix.entity.Genre;

public record MovieResponse(
		Long id,
		String title,
		String description,
		int releaseYear,
		Genre genre,
		double rating,
		int durationMinutes,
		LocalDate releaseDate) {
}