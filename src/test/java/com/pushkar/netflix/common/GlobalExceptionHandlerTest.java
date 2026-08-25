package com.pushkar.netflix.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import com.pushkar.netflix.exception.DuplicateResourceException;
import com.pushkar.netflix.exception.InvalidCredentialsException;
import com.pushkar.netflix.exception.ResourceNotFoundException;

class GlobalExceptionHandlerTest {

	private GlobalExceptionHandler handler;

	@BeforeEach
	void setUp() {
		handler = new GlobalExceptionHandler();
	}

	@Test
	void shouldMapResourceNotFoundToNotFound() {
		ProblemDetail problem = handler.handleNotFound(new ResourceNotFoundException("Movie not found with id: 42"));

		assertThat(problem.getStatus()).isEqualTo(404);
		assertThat(problem.getTitle()).isEqualTo(HttpStatus.NOT_FOUND.getReasonPhrase());
		assertThat(problem.getDetail()).isEqualTo("Movie not found with id: 42");
	}

	@Test
	void shouldMapDuplicateResourceToConflict() {
		ProblemDetail problem = handler
			.handleDuplicate(new DuplicateResourceException("User already registered with email: pushkar@example.com"));

		assertThat(problem.getStatus()).isEqualTo(409);
		assertThat(problem.getDetail()).contains("pushkar@example.com");
	}

	@Test
	void shouldMapInvalidCredentialsToUnauthorized() {
		ProblemDetail problem = handler
			.handleInvalidCredentials(new InvalidCredentialsException("Invalid password for email: pushkar@example.com"));

		assertThat(problem.getStatus()).isEqualTo(401);
	}

	@Test
	void shouldCollectFieldValidationErrors() {
		MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
		BindingResult bindingResult = mock(BindingResult.class);
		when(ex.getBindingResult()).thenReturn(bindingResult);
		when(bindingResult.getFieldErrors())
			.thenReturn(List.of(new FieldError("movieRequest", "title", "title is required")));

		ProblemDetail problem = handler.handleValidation(ex);

		assertThat(problem.getStatus()).isEqualTo(400);
		assertThat(problem.getProperties()).containsKey("errors");
		assertThat(problem.getDetail()).isEqualTo("Validation failed for the request");
	}

}