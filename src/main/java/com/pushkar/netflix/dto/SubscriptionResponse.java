package com.pushkar.netflix.dto;

import java.time.LocalDate;

import com.pushkar.netflix.entity.SubscriptionPlan;

public record SubscriptionResponse(
		Long id,
		String email,
		SubscriptionPlan plan,
		int priceInRupees,
		int screenCount,
		LocalDate startedOn,
		boolean active) {
}