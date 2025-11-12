/*
 * Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved.
 */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseProperty;

/**
 * Represents general properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public enum General implements BaseProperty {
	CATEGORY("Category"),
	DEVICE_ID("DeviceID"),
	DEVICE_KEY("DeviceKey"),
	FIRMWARE_VERSION("FirmwareVersion"),
	MAC_ADDRESS("MACAddress"),
	MANUFACTURER("Manufacturer"),
	MODEL("Model"),
	NAME("Name"),
	PRODUCT_ID("ProductID"),
	REBOOT_REASON("RebootReason"),
	SERIAL_NUMBER("SerialNumber"),
	VERSION("Version");

	private final String name;

	General(String name) {
		this.name = name;
	}

	/**
	 * Retrieves {@link #name}
	 *
	 * @return value of {@link #name}
	 */
	@Override
	public String getName() {
		return name;
	}
}
