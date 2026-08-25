package com.pushkar.netflix.filter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Order(0)
public class RequestLoggingFilter extends OncePerRequestFilter {

	private final AtomicLong counter = new AtomicLong();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		long requestNumber = counter.incrementAndGet();
		Instant startedAt = Instant.now();

		try {
			chain.doFilter(request, response);
		}
		finally {
			long tookMillis = Duration.between(startedAt, Instant.now()).toMillis();
			System.out.printf("#%d %s %s -> %d (%d ms)%n",
					requestNumber,
					request.getMethod(),
					request.getRequestURI(),
					response.getStatus(),
					tookMillis);
		}
	}

}