package com.pushkar.netflix.config;

import java.time.LocalDate;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.pushkar.netflix.entity.Genre;
import com.pushkar.netflix.entity.Movie;
import com.pushkar.netflix.repository.MovieRepository;

@Component
public class SeedDataRunner implements ApplicationRunner {

	private final MovieRepository movieRepository;

	public SeedDataRunner(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	@Override
	public void run(ApplicationArguments args) {
		movie("Interstellar Voyage", "A crew travels through a wormhole to save humanity.", 2024, Genre.SCI_FI, 8.7,
				169, LocalDate.of(2024, 5, 3));
		movie("The Last Heist", "A retired thief is pulled back in for one final job.", 2023, Genre.THRILLER, 7.9,
				118, LocalDate.of(2023, 9, 15));
		movie("Sunday Kitchen", "Three siblings reopen their father's restaurant.", 2022, Genre.COMEDY, 6.8,
				101, LocalDate.of(2022, 1, 28));
		movie("Midnight Signal", "A radio operator hears a broadcast from tomorrow.", 2025, Genre.THRILLER, 8.2,
				127, LocalDate.of(2025, 2, 14));
	}

	private void movie(String title, String description, int year, Genre genre, double rating, int minutes,
			LocalDate releaseDate) {
		Movie movie = new Movie();
		movie.setTitle(title);
		movie.setDescription(description);
		movie.setReleaseYear(year);
		movie.setGenre(genre);
		movie.setRating(rating);
		movie.setDurationMinutes(minutes);
		movie.setReleaseDate(releaseDate);
		movieRepository.save(movie);
	}

}