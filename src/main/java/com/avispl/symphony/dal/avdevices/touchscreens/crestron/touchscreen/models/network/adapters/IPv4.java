/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed IPv4 about a {@link Adapters}.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#NETWORK_ADAPTERS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class IPv4 {
	@JsonProperty("Addresses")
	private List<AddressConfig> addressConfigs;
	@JsonProperty("DefaultGateway")
	private String defaultGateway;
	@JsonProperty("IsDhcpEnabled")
	private Boolean isDhcpEnabled;
	@JsonProperty("DnsServers")
	private List<String> dnsServers;
	@JsonProperty("StaticDns")
	private List<String> staticDns;

	/**
	 * Retrieves {@link #addressConfigs}
	 *
	 * @return value of {@link #addressConfigs}
	 */
	public List<AddressConfig> getAddresses() {
		return addressConfigs;
	}

	/**
	 * Sets {@link #addressConfigs} value
	 *
	 * @param addressConfigs new value of {@link #addressConfigs}
	 */
	public void setAddresses(List<AddressConfig> addressConfigs) {
		this.addressConfigs = addressConfigs;
	}

	/**
	 * Retrieves {@link #defaultGateway}
	 *
	 * @return value of {@link #defaultGateway}
	 */
	public String getDefaultGateway() {
		return defaultGateway;
	}

	/**
	 * Sets {@link #defaultGateway} value
	 *
	 * @param defaultGateway new value of {@link #defaultGateway}
	 */
	public void setDefaultGateway(String defaultGateway) {
		this.defaultGateway = defaultGateway;
	}

	/**
	 * Retrieves {@link #isDhcpEnabled}
	 *
	 * @return value of {@link #isDhcpEnabled}
	 */
	public Boolean getDhcpEnabled() {
		return isDhcpEnabled;
	}

	/**
	 * Sets {@link #isDhcpEnabled} value
	 *
	 * @param dhcpEnabled new value of {@link #isDhcpEnabled}
	 */
	public void setDhcpEnabled(Boolean dhcpEnabled) {
		isDhcpEnabled = dhcpEnabled;
	}

	/**
	 * Retrieves {@link #dnsServers}
	 *
	 * @return value of {@link #dnsServers}
	 */
	public List<String> getDnsServers() {
		return dnsServers;
	}

	/**
	 * Sets {@link #dnsServers} value
	 *
	 * @param dnsServers new value of {@link #dnsServers}
	 */
	public void setDnsServers(List<String> dnsServers) {
		this.dnsServers = dnsServers;
	}

	/**
	 * Retrieves {@link #staticDns}
	 *
	 * @return value of {@link #staticDns}
	 */
	public List<String> getStaticDns() {
		return staticDns;
	}

	/**
	 * Sets {@link #staticDns} value
	 *
	 * @param staticDns new value of {@link #staticDns}
	 */
	public void setStaticDns(List<String> staticDns) {
		this.staticDns = staticDns;
	}
}
