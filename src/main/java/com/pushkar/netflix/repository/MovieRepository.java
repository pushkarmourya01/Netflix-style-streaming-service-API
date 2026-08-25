package com.pushkar.netflix.repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.pushkar.netflix.entity.Movie;

@Repository
public class MovieRepository {

	private final Map<Long, Movie> store = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(1);

	public Movie save(Movie movie) {
		movie.setId(sequence.getAndIncrement());
		store.put(movie.getId(), movie);
		return movie;
	}

	public Optional<Movie> findById(Long id) {
		return Optional.ofNullable(store.get(id));
	}

	public List<Movie> findAll() {
		return store.values().stream()
				.sorted((first, second) -> Long.compare(first.getId(), second.getId()))
				.toList();
	}

	public boolean existsById(Long id) {
		return store.containsKey(id);
	}

	public void deleteById(Long id) {
		store.remove(id);
	}

}