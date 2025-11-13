/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed address about a {@link IPv4}.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#NETWORK_ADAPTERS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressConfig {
	@JsonProperty("Address")
	private String address;
	@JsonProperty("SubnetMask")
	private String subnetMask;

	/**
	 * Retrieves {@link #address}
	 *
	 * @return value of {@link #address}
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * Sets {@link #address} value
	 *
	 * @param address new value of {@link #address}
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * Retrieves {@link #subnetMask}
	 *
	 * @return value of {@link #subnetMask}
	 */
	public String getSubnetMask() {
		return subnetMask;
	}

	/**
	 * Sets {@link #subnetMask} value
	 *
	 * @param subnetMask new value of {@link #subnetMask}
	 */
	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}
}
