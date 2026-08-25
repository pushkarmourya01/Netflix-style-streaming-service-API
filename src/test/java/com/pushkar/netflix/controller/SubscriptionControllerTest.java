package com.pushkar.netflix.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.pushkar.netflix.dto.SubscriptionRequest;
import com.pushkar.netflix.dto.SubscriptionResponse;
import com.pushkar.netflix.entity.SubscriptionPlan;
import com.pushkar.netflix.service.SubscriptionService;

@ExtendWith(MockitoExtension.class)
class SubscriptionControllerTest {

	private static final String EMAIL = "pushkar@example.com";

	@Mock
	private SubscriptionService subscriptionService;

	private SubscriptionController subscriptionController;

	@BeforeEach
	void setUp() {
		subscriptionController = new SubscriptionController(subscriptionService);
	}

	private static SubscriptionResponse response(SubscriptionPlan plan, boolean active) {
		return new SubscriptionResponse(1L, EMAIL, plan, plan.getPriceInRupees(), plan.getScreenCount(), LocalDate.now(), active);
	}

	@Test
	void shouldSubscribeUsingAuthenticatedEmail() {
		SubscriptionRequest request = new SubscriptionRequest(SubscriptionPlan.PREMIUM);
		when(subscriptionService.subscribe(EMAIL, request)).thenReturn(response(SubscriptionPlan.PREMIUM, true));

		ResponseEntity<SubscriptionResponse> result = subscriptionController.subscribe(EMAIL, request);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().plan()).isEqualTo(SubscriptionPlan.PREMIUM);
		assertThat(result.getBody().active()).isTrue();
	}

	@Test
	void shouldReturnCurrentPlan() {
		when(subscriptionService.currentPlan(EMAIL)).thenReturn(response(SubscriptionPlan.BASIC, true));

		ResponseEntity<SubscriptionResponse> result = subscriptionController.currentPlan(EMAIL);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().priceInRupees()).isEqualTo(199);
	}

	@Test
	void shouldCancelSubscription() {
		when(subscriptionService.cancel(EMAIL)).thenReturn(response(SubscriptionPlan.FAMILY, false));

		ResponseEntity<SubscriptionResponse> result = subscriptionController.cancel(EMAIL);

		assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(result.getBody().active()).isFalse();
	}

}