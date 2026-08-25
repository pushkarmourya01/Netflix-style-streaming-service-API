package com.pushkar.netflix.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.pushkar.netflix.dto.MovieRequest;
import com.pushkar.netflix.dto.MovieResponse;
import com.pushkar.netflix.entity.Genre;
import com.pushkar.netflix.entity.Movie;
import com.pushkar.netflix.exception.ResourceNotFoundException;
import com.pushkar.netflix.repository.MovieRepository;

@Service
public class MovieService {

	private final MovieRepository movieRepository;

	public MovieService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public MovieResponse create(MovieRequest request) {
		Movie movie = new Movie();
		apply(movie, request);
		return toResponse(movieRepository.save(movie));
	}

	public List<MovieResponse> findAll() {
		return movieRepository.findAll().stream().map(this::toResponse).toList();
	}

	public List<MovieResponse> findByGenre(Genre genre) {
		return movieRepository.findAll().stream()
				.filter(movie -> movie.getGenre() == genre)
				.map(this::toResponse)
				.toList();
	}

	public MovieResponse findById(Long id) {
		return toResponse(requireMovie(id));
	}

	public MovieResponse update(Long id, MovieRequest request) {
		Movie movie = requireMovie(id);
		apply(movie, request);
		return toResponse(movieRepository.save(movie));
	}

	public void delete(Long id) {
		if (!movieRepository.existsById(id)) {
			throw new ResourceNotFoundException("Movie not found with id: " + id);
		}
		movieRepository.deleteById(id);
	}

	private Movie requireMovie(Long id) {
		return movieRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id));
	}

	private void apply(Movie movie, MovieRequest request) {
		movie.setTitle(request.title());
		movie.setDescription(request.description());
		movie.setReleaseYear(request.releaseYear());
		movie.setGenre(request.genre());
		movie.setRating(request.rating());
		movie.setDurationMinutes(request.durationMinutes());
		movie.setReleaseDate(request.releaseDate());
	}

	private MovieResponse toResponse(Movie movie) {
		return new MovieResponse(
				movie.getId(),
				movie.getTitle(),
				movie.getDescription(),
				movie.getReleaseYear(),
				movie.getGenre(),
				movie.getRating(),
				movie.getDurationMinutes(),
				movie.getReleaseDate());
	}

}