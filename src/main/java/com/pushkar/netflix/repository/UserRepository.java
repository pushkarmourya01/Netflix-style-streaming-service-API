package com.pushkar.netflix.repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Repository;

import com.pushkar.netflix.entity.User;

@Repository
public class UserRepository {

	private final Map<Long, User> store = new ConcurrentHashMap<>();
	private final AtomicLong sequence = new AtomicLong(1);

	public User save(User user) {
		user.setId(sequence.getAndIncrement());
		store.put(user.getId(), user);
		return user;
	}

	public Optional<User> findByEmail(String email) {
		return store.values().stream()
				.filter(user -> user.getEmail().equalsIgnoreCase(email))
				.findFirst();
	}

	public boolean existsByEmail(String email) {
		return findByEmail(email).isPresent();
	}

}