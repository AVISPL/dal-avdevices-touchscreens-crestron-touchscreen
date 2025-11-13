/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseAdapter;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed LAN adapter about a {@link Adapters}.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#NETWORK_ADAPTERS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LanAdapter extends BaseAdapter {
	@JsonProperty("IPv4")
	private IPv4 iPv4;

	/**
	 * Retrieves {@link #iPv4}
	 *
	 * @return value of {@link #iPv4}
	 */
	public IPv4 getIPv4() {
		return iPv4;
	}

	/**
	 * Sets {@link #iPv4} value
	 *
	 * @param iPv4 new value of {@link #iPv4}
	 */
	public void setiPv4(IPv4 iPv4) {
		this.iPv4 = iPv4;
	}
}
