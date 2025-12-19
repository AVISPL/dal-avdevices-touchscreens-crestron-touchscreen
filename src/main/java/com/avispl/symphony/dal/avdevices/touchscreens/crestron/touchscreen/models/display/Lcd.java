/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed lcd about a {@link DeviceDisplay}.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#DISPLAY}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class Lcd {
	@JsonProperty("AutoBrightness")
	private AutoBrightness autoBrightness;
	@JsonProperty("Brightness")
	private Integer brightness;
	@JsonProperty("Presets")
	private Presets presets;
	@JsonProperty("StandbyTimeoutMinutes")
	private Integer standbyTimeoutMinutes;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Data
	public static class AutoBrightness {
		@JsonProperty("IsEnabled")
		private Boolean isEnabled;
		@JsonProperty("ThresholdValue")
		private Integer thresholdValue;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Data
	public static class Presets {
		@JsonProperty("HighLevel")
		private Integer highLevel;
		@JsonProperty("LowLevel")
		private Integer lowLevel;
	}
}
