/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed information about a device.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#DEVICE_INFO}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceInfo {
	@JsonProperty("BuildDate")
	private String buildDate;
	@JsonProperty("DeviceVersion")
	private String deviceVersion;
	@JsonProperty("ModelId")
	private String modelId;
	@JsonProperty("Name")
	private String name;
	@JsonProperty("PufVersion")
	private String pufVersion;
	@JsonProperty("SerialNumber")
	private String serialNumber;

	/**
	 * Retrieves {@link #buildDate}
	 *
	 * @return value of {@link #buildDate}
	 */
	public String getBuildDate() {
		return buildDate;
	}

	/**
	 * Sets {@link #buildDate} value
	 *
	 * @param buildDate new value of {@link #buildDate}
	 */
	public void setBuildDate(String buildDate) {
		this.buildDate = buildDate;
	}

	/**
	 * Retrieves {@link #deviceVersion}
	 *
	 * @return value of {@link #deviceVersion}
	 */
	public String getDeviceVersion() {
		return deviceVersion;
	}

	/**
	 * Sets {@link #deviceVersion} value
	 *
	 * @param deviceVersion new value of {@link #deviceVersion}
	 */
	public void setDeviceVersion(String deviceVersion) {
		this.deviceVersion = deviceVersion;
	}

	/**
	 * Retrieves {@link #modelId}
	 *
	 * @return value of {@link #modelId}
	 */
	public String getModelId() {
		return modelId;
	}

	/**
	 * Sets {@link #modelId} value
	 *
	 * @param modelId new value of {@link #modelId}
	 */
	public void setModelId(String modelId) {
		this.modelId = modelId;
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
	 * Retrieves {@link #pufVersion}
	 *
	 * @return value of {@link #pufVersion}
	 */
	public String getPufVersion() {
		return pufVersion;
	}

	/**
	 * Sets {@link #pufVersion} value
	 *
	 * @param pufVersion new value of {@link #pufVersion}
	 */
	public void setPufVersion(String pufVersion) {
		this.pufVersion = pufVersion;
	}

	/**
	 * Retrieves {@link #serialNumber}
	 *
	 * @return value of {@link #serialNumber}
	 */
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * Sets {@link #serialNumber} value
	 *
	 * @param serialNumber new value of {@link #serialNumber}
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
}