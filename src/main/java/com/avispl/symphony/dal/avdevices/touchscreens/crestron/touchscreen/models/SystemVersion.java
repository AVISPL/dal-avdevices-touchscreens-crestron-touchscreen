/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed system version about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#SYSTEM_VERSIONS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SystemVersion {
	@JsonProperty("Category")
	private String category;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Version")
	private String version;

	/**
	 * Retrieves {@link #category}
	 *
	 * @return value of {@link #category}
	 */
	public String getCategory() {
		return category;
	}

	/**
	 * Sets {@link #category} value
	 *
	 * @param category new value of {@link #category}
	 */
	public void setCategory(String category) {
		this.category = category;
	}

	/**
	 * Retrieves {@link #name}
	 *
	 * @return value of {@link #name}
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets {@link #name} value
	 *
	 * @param name new value of {@link #name}
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@link #version}
	 *
	 * @return value of {@link #version}
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets {@link #version} value
	 *
	 * @param version new value of {@link #version}
	 */
	public void setVersion(String version) {
		this.version = version;
	}
}
