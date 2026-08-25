package com.pushkar.netflix.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pushkar.netflix.dto.MovieRequest;
import com.pushkar.netflix.dto.MovieResponse;
import com.pushkar.netflix.entity.Genre;
import com.pushkar.netflix.exception.ResourceNotFoundException;
import com.pushkar.netflix.repository.MovieRepository;

class MovieServiceTest {

	private MovieRepository movieRepository;

	private MovieService movieService;

	@BeforeEach
	void setUp() {
		movieRepository = new MovieRepository();
		movieService = new MovieService(movieRepository);
	}

	private MovieRequest request(String title, Genre genre) {
		return new MovieRequest(title, "A mind-bending thriller", 2010, genre, 8.8, 148, LocalDate.of(2010, 7, 16));
	}

	@Test
	void shouldCreateMovieAndAssignId() {
		MovieResponse created = movieService.create(request("Inception", Genre.SCI_FI));

		assertThat(created.id()).isNotNull();
		assertThat(created.title()).isEqualTo("Inception");
		assertThat(created.genre()).isEqualTo(Genre.SCI_FI);
	}

	@Test
	void shouldFindAllMovies() {
		movieService.create(request("Inception", Genre.SCI_FI));
		movieService.create(request("The Dark Knight", Genre.ACTION));

		assertThat(movieService.findAll()).hasSize(2);
	}

	@Test
	void shouldFindMovieById() {
		MovieResponse created = movieService.create(request("Inception", Genre.SCI_FI));

		assertThat(movieService.findById(created.id()).title()).isEqualTo("Inception");
	}

	@Test
	void shouldThrowWhenMovieIsMissing() {
		assertThatThrownBy(() -> movieService.findById(999L)).isInstanceOf(ResourceNotFoundException.class);
	}

	@Test
	void shouldUpdateExistingMovie() {
		MovieResponse created = movieService.create(request("Inception", Genre.SCI_FI));

		MovieResponse updated = movieService.update(created.id(), request("Inception 2", Genre.THRILLER));

		assertThat(updated.title()).isEqualTo("Inception 2");
		assertThat(updated.genre()).isEqualTo(Genre.THRILLER);
	}

	@Test
	void shouldFilterMoviesByGenre() {
		movieService.create(request("Inception", Genre.SCI_FI));
		movieService.create(request("The Dark Knight", Genre.ACTION));

		assertThat(movieService.findByGenre(Genre.SCI_FI)).hasSize(1);
	}

	@Test
	void shouldDeleteMovie() {
		MovieResponse created = movieService.create(request("Inception", Genre.SCI_FI));

		movieService.delete(created.id());

		assertThat(movieRepository.findById(created.id())).isEmpty();
	}

	@Test
	void shouldThrowWhenDeletingMissingMovie() {
		assertThatThrownBy(() -> movieService.delete(999L)).isInstanceOf(ResourceNotFoundException.class);
	}

}