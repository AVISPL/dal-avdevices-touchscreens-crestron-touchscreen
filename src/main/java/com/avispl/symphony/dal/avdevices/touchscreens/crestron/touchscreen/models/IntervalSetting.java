/*
 * Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;

/**
 * Manages request timeout settings to control when a request can be executed again after a defined interval.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public class IntervalSetting {
	private final long intervalMs;
	private long validRetrievalTimestamp;

	public IntervalSetting() {
		this.intervalMs = Constant.DEFAULT_INTERVAL_MS;
		this.validRetrievalTimestamp = System.currentTimeMillis();
	}

	public IntervalSetting(long intervalMs) {
		this.intervalMs = Math.max(Constant.DEFAULT_INTERVAL_MS, intervalMs);
		this.validRetrievalTimestamp = System.currentTimeMillis() + this.intervalMs;
	}

	/**
	 * Retrieves {@link #intervalMs}
	 *
	 * @return value of {@link #intervalMs}
	 */
	public long getIntervalMs() {
		return intervalMs;
	}

	/**
	 * Checks whether the request is valid to execute at the current time.
	 * <p>
	 * - If the current time is before {@code validRetrievalTimestamp}, returns {@code false}.<br>
	 * - If the current time is greater or equal, returns {@code true} and updates
	 * {@code validRetrievalTimestamp} to {@code now + retrievalTimeout}.
	 *
	 * @return {@code true} if the request can be executed, {@code false} otherwise
	 */
	public boolean isValid() {
		boolean isValid = System.currentTimeMillis() >= this.validRetrievalTimestamp;
		if (isValid) {
			this.validRetrievalTimestamp = System.currentTimeMillis() + this.intervalMs;
		}
		return isValid;
	}

	/**
	 * Returns a message indicating how long remains until the next retrieval becomes available.
	 * <br/>
	 * Example output: "Next availability in 12 seconds."
	 *
	 * @return a message describing how many seconds remain until the next retrieval is allowed.
	 */
	public String getNextAvailabilityInfo() {
		long seconds = Math.max((this.validRetrievalTimestamp - System.currentTimeMillis()) / 1000, 0);
		return String.format("Next availability in %s seconds.", seconds);
	}
}
