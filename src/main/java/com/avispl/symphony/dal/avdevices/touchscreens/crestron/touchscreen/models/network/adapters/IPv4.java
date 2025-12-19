/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed IPv4 about a {@link Adapters}.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#NETWORK_ADAPTERS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
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
}
