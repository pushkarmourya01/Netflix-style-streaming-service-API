package com.pushkar.netflix.entity;

public enum SubscriptionPlan {

	BASIC(199, 1),
	PREMIUM(499, 4),
	FAMILY(799, 6);

	private final int priceInRupees;
	private final int screenCount;

	SubscriptionPlan(int priceInRupees, int screenCount) {
		this.priceInRupees = priceInRupees;
		this.screenCount = screenCount;
	}

	public int getPriceInRupees() {
		return priceInRupees;
	}

	public int getScreenCount() {
		return screenCount;
	}

}