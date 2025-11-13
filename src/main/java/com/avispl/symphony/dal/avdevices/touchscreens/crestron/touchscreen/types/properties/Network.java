/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseProperty;

/**
 * Represents network adapter properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public enum Network implements BaseProperty {
	DNS_SERVERS("DNSServers"),
	HOSTNAME("Hostname"),
	IPV6_ENABLED("IPv6Enabled"),
	//	LAN
	LAN_DEFAULT_GATEWAY("LANDefaultGateway"),
	LAN_DHCP_ENABLED("LANDHCPEnabled"),
	LAN_DOMAIN_NAME("LANDomainName"),
	LAN_IP_ADDRESS("LANIPAddress"),
	LAN_LINK_ACTIVE("LANLinkActive"),
	LAN_SUBNET_MASK("LANSubnetMask"),
	//	WiFi
	WIFI_DOMAIN_NAME("WiFiDomainName"),
	WIFI_LINK_ACTIVE("WiFiLinkActive"),
	WIFI_MAC_ADDRESS("WiFiMACAddress");

	private final String name;

	Network(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@link #name}
	 *
	 * @return value of {@link #name}
	 */
	@Override
	public String getName() {
		return name;
	}
}
