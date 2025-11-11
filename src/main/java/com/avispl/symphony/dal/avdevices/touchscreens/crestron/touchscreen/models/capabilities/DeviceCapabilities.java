/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.capabilities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed capabilities about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#DEVICE_CAPABILITIES}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceCapabilities {
	@JsonProperty("IsConfigFileUploadSupported")
	private Boolean isConfigFileUploadSupported;
	@JsonProperty("IsLogFileUploadSupported")
	private Boolean isLogFileUploadSupported;
	@JsonProperty("PortConfig")
	private PortConfig portConfig;

	/**
	 * Retrieves {@link #isConfigFileUploadSupported}
	 *
	 * @return value of {@link #isConfigFileUploadSupported}
	 */
	public Boolean getConfigFileUploadSupported() {
		return isConfigFileUploadSupported;
	}

	/**
	 * Sets {@link #isConfigFileUploadSupported} value
	 *
	 * @param configFileUploadSupported new value of {@link #isConfigFileUploadSupported}
	 */
	public void setConfigFileUploadSupported(Boolean configFileUploadSupported) {
		isConfigFileUploadSupported = configFileUploadSupported;
	}

	/**
	 * Retrieves {@link #isLogFileUploadSupported}
	 *
	 * @return value of {@link #isLogFileUploadSupported}
	 */
	public Boolean getLogFileUploadSupported() {
		return isLogFileUploadSupported;
	}

	/**
	 * Sets {@link #isLogFileUploadSupported} value
	 *
	 * @param logFileUploadSupported new value of {@link #isLogFileUploadSupported}
	 */
	public void setLogFileUploadSupported(Boolean logFileUploadSupported) {
		isLogFileUploadSupported = logFileUploadSupported;
	}

	/**
	 * Retrieves {@link #portConfig}
	 *
	 * @return value of {@link #portConfig}
	 */
	public PortConfig getPortConfig() {
		return portConfig;
	}

	/**
	 * Sets {@link #portConfig} value
	 *
	 * @param portConfig new value of {@link #portConfig}
	 */
	public void setPortConfig(PortConfig portConfig) {
		this.portConfig = portConfig;
	}
}
