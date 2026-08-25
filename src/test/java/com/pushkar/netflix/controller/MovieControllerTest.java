package com.pushkar.netflix.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pushkar.netflix.dto.MovieRequest;
import com.pushkar.netflix.dto.MovieResponse;
import com.pushkar.netflix.entity.Genre;
import com.pushkar.netflix.service.MovieService;

@ExtendWith(MockitoExtension.class)
class MovieControllerTest {

	@Mock
	private MovieService movieService;

	private MovieController movieController;

	@BeforeEach
	void setUp() {
		movieController = new MovieController(movieService);
	}

	private static MovieRequest request(String title, Genre genre) {
		return new MovieRequest(title, "A mind-bending thriller", 2010, genre, 8.8, 148, LocalDate.of(2010, 7, 16));
	}

	private static MovieResponse response(Long id, String title, Genre genre) {
		return new MovieResponse(id, title, "A mind-bending thriller", 2010, genre, 8.8, 148,
				LocalDate.of(2010, 7, 16));
	}

	@Test
	void shouldCreateMovieWithCreatedStatus() {
		MovieRequest request = request("Inception", Genre.SCI_FI);
		MovieResponse created = response(1L, "Inception", Genre.SCI_FI);
		when(movieService.create(request)).thenReturn(created);

		ResponseEntity<MovieResponse> result = movieController.create(request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(result.getBody()).isEqualTo(created);
	}

	@Test
	void shouldListAllMoviesWhenGenreIsAbsent() {
		when(movieService.findAll()).thenReturn(List.of(response(1L, "Inception", Genre.SCI_FI)));

		ResponseEntity<List<MovieResponse>> result = movieController.findAll(null);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody()).hasSize(1);
	}

	@Test
	void shouldListMoviesByGenre() {
		when(movieService.findByGenre(Genre.ACTION)).thenReturn(List.of(response(2L, "The Dark Knight", Genre.ACTION)));

		ResponseEntity<List<MovieResponse>> result = movieController.findAll(Genre.ACTION);

		assertThat(result.getBody()).hasSize(1);
		assertThat(result.getBody().get(0).title()).isEqualTo("The Dark Knight");
	}

	@Test
	void shouldFetchMovieById() {
		when(movieService.findById(1L)).thenReturn(response(1L, "Inception", Genre.SCI_FI));

		ResponseEntity<MovieResponse> result = movieController.findById(1L);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().title()).isEqualTo("Inception");
	}

	@Test
	void shouldUpdateMovie() {
		MovieRequest request = request("Inception 2", Genre.THRILLER);
		when(movieService.update(1L, request)).thenReturn(response(1L, "Inception 2", Genre.THRILLER));

		ResponseEntity<MovieResponse> result = movieController.update(1L, request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().title()).isEqualTo("Inception 2");
	}

	@Test
	void shouldDeleteMovie() {
		ResponseEntity<Void> result = movieController.delete(1L);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
		verify(movieService).delete(1L);
	}

}