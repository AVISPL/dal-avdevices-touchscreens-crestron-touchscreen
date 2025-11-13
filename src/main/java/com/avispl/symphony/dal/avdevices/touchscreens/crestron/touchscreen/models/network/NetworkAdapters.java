/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.Adapters;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.DnsSettings;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.IPv6;

/**
 * Represents detailed network adapters about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#NETWORK_ADAPTERS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NetworkAdapters {
	@JsonProperty("Adapters")
	private Adapters adapters;
	@JsonProperty("DnsSettings")
	private DnsSettings dnsSettings;
	@JsonProperty("HostName")
	private String hostName;
	@JsonProperty("IPv6")
	private IPv6 iPv6;

	/**
	 * Retrieves {@link #adapters}
	 *
	 * @return value of {@link #adapters}
	 */
	public Adapters getAdapters() {
		return adapters;
	}

	/**
	 * Sets {@link #adapters} value
	 *
	 * @param adapters new value of {@link #adapters}
	 */
	public void setAdapters(Adapters adapters) {
		this.adapters = adapters;
	}

	/**
	 * Retrieves {@link #dnsSettings}
	 *
	 * @return value of {@link #dnsSettings}
	 */
	public DnsSettings getDnsSettings() {
		return dnsSettings;
	}

	/**
	 * Sets {@link #dnsSettings} value
	 *
	 * @param dnsSettings new value of {@link #dnsSettings}
	 */
	public void setDnsSettings(DnsSettings dnsSettings) {
		this.dnsSettings = dnsSettings;
	}

	/**
	 * Retrieves {@link #hostName}
	 *
	 * @return value of {@link #hostName}
	 */
	public String getHostName() {
		return hostName;
	}

	/**
	 * Sets {@link #hostName} value
	 *
	 * @param hostName new value of {@link #hostName}
	 */
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/**
	 * Retrieves {@link #iPv6}
	 *
	 * @return value of {@link #iPv6}
	 */
	public IPv6 getIPv6() {
		return iPv6;
	}

	/**
	 * Sets {@link #iPv6} value
	 *
	 * @param iPv6 new value of {@link #iPv6}
	 */
	public void setIPv6(IPv6 iPv6) {
		this.iPv6 = iPv6;
	}
}
