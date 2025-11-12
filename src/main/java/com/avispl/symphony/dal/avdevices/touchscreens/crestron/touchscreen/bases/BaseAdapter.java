/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * The template for all network adapter.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseAdapter {
	@JsonProperty("DomainName")
	private String domainName;
	@JsonProperty("LinkStatus")
	private Boolean linkStatus;
	@JsonProperty("MacAddress")
	private String macAddress;

	/**
	 * Retrieves {@link #domainName}
	 *
	 * @return value of {@link #domainName}
	 */
	public String getDomainName() {
		return domainName;
	}

	/**
	 * Sets {@link #domainName} value
	 *
	 * @param domainName new value of {@link #domainName}
	 */
	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	/**
	 * Retrieves {@link #linkStatus}
	 *
	 * @return value of {@link #linkStatus}
	 */
	public Boolean getLinkStatus() {
		return linkStatus;
	}

	/**
	 * Sets {@link #linkStatus} value
	 *
	 * @param linkStatus new value of {@link #linkStatus}
	 */
	public void setLinkStatus(Boolean linkStatus) {
		this.linkStatus = linkStatus;
	}

	/**
	 * Retrieves {@link #macAddress}
	 *
	 * @return value of {@link #macAddress}
	 */
	public String getMacAddress() {
		return macAddress;
	}

	/**
	 * Sets {@link #macAddress} value
	 *
	 * @param macAddress new value of {@link #macAddress}
	 */
	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
}
