/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.NetworkAdapters;

/**
 * Represents detailed ipv6 about a {@link NetworkAdapters}.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#NETWORK_ADAPTERS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPv6 {
	@JsonProperty("IsSupported")
	private Boolean isSupported;

	/**
	 * Retrieves {@link #isSupported}
	 *
	 * @return value of {@link #isSupported}
	 */
	public Boolean getSupported() {
		return isSupported;
	}

	/**
	 * Sets {@link #isSupported} value
	 *
	 * @param supported new value of {@link #isSupported}
	 */
	public void setSupported(Boolean supported) {
		isSupported = supported;
	}
}
