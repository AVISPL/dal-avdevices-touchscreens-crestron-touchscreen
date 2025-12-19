/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed display about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#DISPLAY}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class DeviceDisplay {
	@JsonProperty("Audio")
	private Audio audio;
	@JsonProperty("CurrentState")
	private String currentState;
	@JsonProperty("IsLocalSetupAccessEnabled")
	private Boolean isLocalSetupAccessEnabled;
	@JsonProperty("Lcd")
	private Lcd lcd;
	@JsonProperty("VirtualButtons")
	private VirtualButtons virtualButtons;

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Data
	public static class Audio {
		@JsonProperty("BeepVolume")
		private Integer beepVolume;
		@JsonProperty("IsBeepEnabled")
		private Boolean isBeepEnabled;
		@JsonProperty("IsMediaMuted")
		private Boolean isMediaMuted;
		@JsonProperty("IsMuted")
		private Boolean isMuted;
		@JsonProperty("MediaVolume")
		private Integer mediaVolume;
		@JsonProperty("Volume")
		private Integer volume;
	}

	@JsonIgnoreProperties(ignoreUnknown = true)
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Data
	public static class VirtualButtons {
		@JsonProperty("AutoHideTimeOutSeconds")
		private Integer autoHideTimeOutSeconds;
		@JsonProperty("DisplayEdge")
		private String displayEdge;
		@JsonProperty("IsShowDuringStandbyEnabled")
		private Boolean isShowDuringStandbyEnabled;
		@JsonProperty("IsShowOnWakeEnabled")
		private Boolean isShowOnWakeEnabled;
	}
}
