/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;

/**
 * Represents detailed port config about a {@link DeviceCapabilities}.
 * This model is used to deserialize the JSON response returned by {@link EndpointConstant#DEVICE_CAPABILITIES}
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortConfig {
	@JsonProperty("NumberOfDmInputs")
	private Integer numberOfDmInputs;
	@JsonProperty("NumberOfEthernetAdapters")
	private Integer numberOfEthernetAdapters;
	@JsonProperty("NumberOfHdmiInputs")
	private Integer numberOfHdmiInputs;
	@JsonProperty("NumberOfHdmiOutputs")
	private Integer numberOfHdmiOutputs;

	/**
	 * Retrieves {@link #numberOfDmInputs}
	 *
	 * @return value of {@link #numberOfDmInputs}
	 */
	public Integer getNumberOfDmInputs() {
		return numberOfDmInputs;
	}

	/**
	 * Sets {@link #numberOfDmInputs} value
	 *
	 * @param numberOfDmInputs new value of {@link #numberOfDmInputs}
	 */
	public void setNumberOfDmInputs(Integer numberOfDmInputs) {
		this.numberOfDmInputs = numberOfDmInputs;
	}

	/**
	 * Retrieves {@link #numberOfEthernetAdapters}
	 *
	 * @return value of {@link #numberOfEthernetAdapters}
	 */
	public Integer getNumberOfEthernetAdapters() {
		return numberOfEthernetAdapters;
	}

	/**
	 * Sets {@link #numberOfEthernetAdapters} value
	 *
	 * @param numberOfEthernetAdapters new value of {@link #numberOfEthernetAdapters}
	 */
	public void setNumberOfEthernetAdapters(Integer numberOfEthernetAdapters) {
		this.numberOfEthernetAdapters = numberOfEthernetAdapters;
	}

	/**
	 * Retrieves {@link #numberOfHdmiInputs}
	 *
	 * @return value of {@link #numberOfHdmiInputs}
	 */
	public Integer getNumberOfHdmiInputs() {
		return numberOfHdmiInputs;
	}

	/**
	 * Sets {@link #numberOfHdmiInputs} value
	 *
	 * @param numberOfHdmiInputs new value of {@link #numberOfHdmiInputs}
	 */
	public void setNumberOfHdmiInputs(Integer numberOfHdmiInputs) {
		this.numberOfHdmiInputs = numberOfHdmiInputs;
	}

	/**
	 * Retrieves {@link #numberOfHdmiOutputs}
	 *
	 * @return value of {@link #numberOfHdmiOutputs}
	 */
	public Integer getNumberOfHdmiOutputs() {
		return numberOfHdmiOutputs;
	}

	/**
	 * Sets {@link #numberOfHdmiOutputs} value
	 *
	 * @param numberOfHdmiOutputs new value of {@link #numberOfHdmiOutputs}
	 */
	public void setNumberOfHdmiOutputs(Integer numberOfHdmiOutputs) {
		this.numberOfHdmiOutputs = numberOfHdmiOutputs;
	}
}
