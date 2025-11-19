/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed system version about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#SYSTEM_VERSIONS}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
public class SystemVersion {
	@JsonProperty("Category")
	private String category;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("Version")
	private String version;
}
