/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed capabilities about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#DEVICE_CAPABILITIES}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
public class DeviceCapabilities {
	@JsonProperty("IsConfigFileUploadSupported")
	private Boolean isConfigFileUploadSupported;
	@JsonProperty("IsLogFileUploadSupported")
	private Boolean isLogFileUploadSupported;
	@JsonProperty("PortConfig")
	private PortConfig portConfig;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@Getter
	public static class PortConfig {
		@JsonProperty("NumberOfDmInputs")
		private Integer numberOfDmInputs;
		@JsonProperty("NumberOfEthernetAdapters")
		private Integer numberOfEthernetAdapters;
		@JsonProperty("NumberOfHdmiInputs")
		private Integer numberOfHdmiInputs;
		@JsonProperty("NumberOfHdmiOutputs")
		private Integer numberOfHdmiOutputs;
	}
}
