/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.NetworkAdapters;

/**
 * Represents detailed adapters about a {@link NetworkAdapters}.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#NETWORK_ADAPTERS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Adapters {
	@JsonProperty("EthernetLan")
	private LanAdapter ethernetLan;

	@JsonProperty("Wifi")
	private WifiAdapter wifi;

	/**
	 * Retrieves {@link #ethernetLan}
	 *
	 * @return value of {@link #ethernetLan}
	 */
	public LanAdapter getEthernetLan() {
		return ethernetLan;
	}

	/**
	 * Sets {@link #ethernetLan} value
	 *
	 * @param ethernetLan new value of {@link #ethernetLan}
	 */
	public void setEthernetLan(LanAdapter ethernetLan) {
		this.ethernetLan = ethernetLan;
	}

	/**
	 * Retrieves {@link #wifi}
	 *
	 * @return value of {@link #wifi}
	 */
	public WifiAdapter getWifi() {
		return wifi;
	}

	/**
	 * Sets {@link #wifi} value
	 *
	 * @param wifi new value of {@link #wifi}
	 */
	public void setWifi(WifiAdapter wifi) {
		this.wifi = wifi;
	}
}
