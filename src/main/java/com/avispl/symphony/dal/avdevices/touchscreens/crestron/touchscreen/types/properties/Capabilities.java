/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties;

import lombok.Getter;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseProperty;

/**
 * Represents the capabilities properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@Getter
public enum Capabilities implements BaseProperty {
	CONFIG_FILE_UPLOAD_SUPPORTED("ConfigFileUploadSupported"),
	LOG_FILE_UPLOAD_SUPPORTED("LogFileUploadSupported"),
	//	Port config
	PC_NUMBER_OF_DM_INPUT("PortDMInputCount"),
	PC_NUMBER_OF_ETHERNET_ADAPTERS("PortEthernetAdapterCount"),
	PC_NUMBER_OF_HDMI_INPUTS("PortHDMIInputCount"),
	PC_NUMBER_OF_HDMI_OUTPUTS("PortHDMIOutputCount");

	private final String name;

	Capabilities(String name) {
		this.name = name;
	}
}
