package com.pushkar.netflix.entity;

import java.time.LocalDateTime;

public class User {

	private Long id;
	private String email;
	private String password;
	private LocalDateTime registeredAt;

	public User() {
	}

	public User(Long id, String email, String password, LocalDateTime registeredAt) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.registeredAt = registeredAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LocalDateTime getRegisteredAt() {
		return registeredAt;
	}

	public void setRegisteredAt(LocalDateTime registeredAt) {
		this.registeredAt = registeredAt;
	}

}