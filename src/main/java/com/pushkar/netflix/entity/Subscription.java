package com.pushkar.netflix.entity;

import java.time.LocalDate;

public class Subscription {

	private Long id;
	private String email;
	private SubscriptionPlan plan;
	private LocalDate startedOn;
	private boolean active;

	public Subscription() {
	}

	public Subscription(Long id, String email, SubscriptionPlan plan, LocalDate startedOn, boolean active) {
		this.id = id;
		this.email = email;
		this.plan = plan;
		this.startedOn = startedOn;
		this.active = active;
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

	public SubscriptionPlan getPlan() {
		return plan;
	}

	public void setPlan(SubscriptionPlan plan) {
		this.plan = plan;
	}

	public LocalDate getStartedOn() {
		return startedOn;
	}

	public void setStartedOn(LocalDate startedOn) {
		this.startedOn = startedOn;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}