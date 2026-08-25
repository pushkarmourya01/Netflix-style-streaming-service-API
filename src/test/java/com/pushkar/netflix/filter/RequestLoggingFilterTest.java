package com.pushkar.netflix.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import java.io.IOException;

import jakarta.servlet.ServletException;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class RequestLoggingFilterTest {
    private RequestLoggingFilter filter;
    @BeforeEach
    void setUp() {
        filter = new RequestLoggingFilter();
    }
    @Test
    void shouldLogRequest() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("GET", "/api/movies");
        request.addHeader("User-Agent", "TestClient/1.0");
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        filter.doFilterInternal(request, response, chain);
        assertThat(response.getStatus()).isEqualTo(200);
    }
    @Test
    void shouldLogPostRequest() throws IOException, ServletException {
        MockHttpServletRequest request = new MockHttpServletRequest("POST", "/api/movies");
        request.setContent("{\"title\":\"Test\"}".getBytes());
        MockHttpServletResponse response = new MockHttpServletResponse();
        MockFilterChain chain = new MockFilterChain();
        filter.doFilterInternal(request, response, chain);
        assertThat(response.getStatus()).isEqualTo(200);
    }
}