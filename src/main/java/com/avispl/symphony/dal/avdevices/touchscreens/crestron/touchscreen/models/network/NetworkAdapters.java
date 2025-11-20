/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.Adapters;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.IPv4;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.IPv6;

/**
 * Represents detailed network adapters about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#NETWORK_ADAPTERS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class NetworkAdapters {
	@JsonProperty("Adapters")
	private Adapters adapters;
	@JsonProperty("DnsSettings")
	private DnsSettings dnsSettings;
	@JsonProperty("HostName")
	private String hostName;
	@JsonProperty("IPv6")
	private IPv6 iPv6;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	public static class DnsSettings {
		@JsonProperty("IPv4")
		private IPv4 iPv4;
	}
}
