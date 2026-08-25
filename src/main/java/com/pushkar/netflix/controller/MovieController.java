package com.pushkar.netflix.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pushkar.netflix.dto.MovieRequest;
import com.pushkar.netflix.dto.MovieResponse;
import com.pushkar.netflix.entity.Genre;
import com.pushkar.netflix.service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies")
public class MovieController {

	private final MovieService movieService;

	public MovieController(MovieService movieService) {
		this.movieService = movieService;
	}

	@PostMapping
	public ResponseEntity<MovieResponse> create(@Valid @RequestBody MovieRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(movieService.create(request));
	}

	@GetMapping
	public ResponseEntity<List<MovieResponse>> findAll(@RequestParam(required = false) Genre genre) {
		List<MovieResponse> movies = genre == null ? movieService.findAll() : movieService.findByGenre(genre);
		return ResponseEntity.ok(movies);
	}

	@GetMapping("/{id}")
	public ResponseEntity<MovieResponse> findById(@PathVariable Long id) {
		return ResponseEntity.ok(movieService.findById(id));
	}

	@PutMapping("/{id}")
	public ResponseEntity<MovieResponse> update(@PathVariable Long id, @Valid @RequestBody MovieRequest request) {
		return ResponseEntity.ok(movieService.update(id, request));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		movieService.delete(id);
		return ResponseEntity.noContent().build();
	}

}