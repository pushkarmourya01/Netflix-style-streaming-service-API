package com.pushkar.netflix.service;

import java.time.LocalDate;

import org.springframework.stereotype.Service;

import com.pushkar.netflix.dto.SubscriptionRequest;
import com.pushkar.netflix.dto.SubscriptionResponse;
import com.pushkar.netflix.entity.Subscription;
import com.pushkar.netflix.exception.ResourceNotFoundException;
import com.pushkar.netflix.repository.SubscriptionRepository;

@Service
public class SubscriptionService {

	private final SubscriptionRepository subscriptionRepository;

	public SubscriptionService(SubscriptionRepository subscriptionRepository) {
		this.subscriptionRepository = subscriptionRepository;
	}

	public SubscriptionResponse subscribe(String email, SubscriptionRequest request) {
		Subscription subscription = subscriptionRepository.findByEmail(email)
				.orElseGet(() -> newSubscription(email));

		subscription.setPlan(request.plan());
		subscription.setActive(true);
		subscription.setStartedOn(LocalDate.now());

		return toResponse(subscriptionRepository.save(subscription));
	}

	public SubscriptionResponse currentPlan(String email) {
		Subscription subscription = subscriptionRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("No subscription found for email: " + email));

		return toResponse(subscription);
	}

	public SubscriptionResponse cancel(String email) {
		Subscription subscription = subscriptionRepository.findByEmail(email)
				.orElseThrow(() -> new ResourceNotFoundException("No subscription found for email: " + email));

		subscription.setActive(false);
		return toResponse(subscription);
	}

	private Subscription newSubscription(String email) {
		Subscription subscription = new Subscription();
		subscription.setEmail(email.toLowerCase());
		return subscription;
	}

	private SubscriptionResponse toResponse(Subscription subscription) {
		return new SubscriptionResponse(
				subscription.getId(),
				subscription.getEmail(),
				subscription.getPlan(),
				subscription.getPlan().getPriceInRupees(),
				subscription.getPlan().getScreenCount(),
				subscription.getStartedOn(),
				subscription.isActive());
	}

}