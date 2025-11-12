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
	 * Retrieves {@link #deviceId}
	 *
	 * @return value of {@link #deviceId}
	 */
	public String getDeviceId() {
		return deviceId;
	}

	/**
	 * Sets {@link #deviceId} value
	 *
	 * @param deviceId new value of {@link #deviceId}
	 */
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	/**
	 * Retrieves {@link #deviceKey}
	 *
	 * @return value of {@link #deviceKey}
	 */
	public String getDeviceKey() {
		return deviceKey;
	}

	/**
	 * Sets {@link #deviceKey} value
	 *
	 * @param deviceKey new value of {@link #deviceKey}
	 */
	public void setDeviceKey(String deviceKey) {
		this.deviceKey = deviceKey;
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

	/**
	 * Retrieves {@link #manufacturer}
	 *
	 * @return value of {@link #manufacturer}
	 */
	public String getManufacturer() {
		return manufacturer;
	}

	/**
	 * Sets {@link #manufacturer} value
	 *
	 * @param manufacturer new value of {@link #manufacturer}
	 */
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	/**
	 * Retrieves {@link #model}
	 *
	 * @return value of {@link #model}
	 */
	public String getModel() {
		return model;
	}

	/**
	 * Sets {@link #model} value
	 *
	 * @param model new value of {@link #model}
	 */
	public void setModel(String model) {
		this.model = model;
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
	 * Retrieves {@link #rebootReason}
	 *
	 * @return value of {@link #rebootReason}
	 */
	public String getRebootReason() {
		return rebootReason;
	}

	/**
	 * Sets {@link #rebootReason} value
	 *
	 * @param rebootReason new value of {@link #rebootReason}
	 */
	public void setRebootReason(String rebootReason) {
		this.rebootReason = rebootReason;
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