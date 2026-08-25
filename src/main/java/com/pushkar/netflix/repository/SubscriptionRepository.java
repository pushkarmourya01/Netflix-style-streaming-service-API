package com.pushkar.netflix.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.pushkar.netflix.entity.Subscription;

@Repository
public class SubscriptionRepository {

	private final Map<Long, Subscription> store = new ConcurrentHashMap<>();
	private final Map<String, Long> indexByEmail = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(1);

	public Subscription save(Subscription subscription) {
		subscription.setId(sequence.getAndIncrement());
		store.put(subscription.getId(), subscription);
		indexByEmail.put(subscription.getEmail().toLowerCase(), subscription.getId());
		return subscription;
	}

	public Optional<Subscription> findByEmail(String email) {
		return Optional.ofNullable(indexByEmail.get(email.toLowerCase()))
				.map(store::get);
	}

}