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
	BUILD_DATE("BuildDate"),
	FIRMWARE_VERSION("FirmwareVersion"),
	NAME("Name"),
	PRODUCT_ID("ProductID"),
	PUF_VERSION("PUFVersion"),
	SERIAL_NUMBER("SerialNumber");

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
