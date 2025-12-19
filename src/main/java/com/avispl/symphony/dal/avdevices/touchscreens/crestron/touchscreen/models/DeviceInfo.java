/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed information about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#DEVICE_INFO}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@NoArgsConstructor
public class DeviceInfo {
	@JsonProperty("DeviceVersion")
	private String deviceVersion;
	@JsonProperty("Category")
	private String category;
	@JsonProperty("DeviceId")
	private String deviceId;
	@JsonProperty("Devicekey")
	private String deviceKey;
	@JsonProperty("MacAddress")
	private String macAddress;
	@JsonProperty("Manufacturer")
	private String manufacturer;
	@JsonProperty("Model")
	private String model;
	@JsonProperty("ModelId")
	private String modelId;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("RebootReason")
	private String rebootReason;
	@JsonProperty("SerialNumber")
	private String serialNumber;
	@JsonProperty("Version")
	private String version;
}