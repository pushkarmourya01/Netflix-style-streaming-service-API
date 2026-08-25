package com.pushkar.netflix.dto;

import com.pushkar.netflix.entity.SubscriptionPlan;

import jakarta.validation.constraints.NotNull;

public record SubscriptionRequest(@NotNull(message = "plan is required") SubscriptionPlan plan) {
}