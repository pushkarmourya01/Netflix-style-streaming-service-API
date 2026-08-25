package com.pushkar.netflix.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pushkar.netflix.dto.SubscriptionRequest;
import com.pushkar.netflix.dto.SubscriptionResponse;
import com.pushkar.netflix.filter.AuthFilter;
import com.pushkar.netflix.service.SubscriptionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/subscription")
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

	public SubscriptionController(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	@PostMapping
	public ResponseEntity<SubscriptionResponse> subscribe(
			@RequestAttribute(AuthFilter.EMAIL_ATTRIBUTE) String email,
			@Valid @RequestBody SubscriptionRequest request) {
		return ResponseEntity.ok(subscriptionService.subscribe(email, request));
	}

	@GetMapping
	public ResponseEntity<SubscriptionResponse> currentPlan(
			@RequestAttribute(AuthFilter.EMAIL_ATTRIBUTE) String email) {
		return ResponseEntity.ok(subscriptionService.currentPlan(email));
	}

	@DeleteMapping
	public ResponseEntity<SubscriptionResponse> cancel(
			@RequestAttribute(AuthFilter.EMAIL_ATTRIBUTE) String email) {
		return ResponseEntity.ok(subscriptionService.cancel(email));
	}

}