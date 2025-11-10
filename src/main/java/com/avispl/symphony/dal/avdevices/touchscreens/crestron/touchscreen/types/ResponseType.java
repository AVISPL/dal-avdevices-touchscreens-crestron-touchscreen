/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types;

import com.fasterxml.jackson.databind.JsonNode;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceCapabilities;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceInfo;

/**
 * Defines different response types and their associated model classes.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public enum ResponseType {
	DEVICE_INFO(DeviceInfo.class),
	DEVICE_CAPABILITIES(DeviceCapabilities.class);

	private final Class<?> clazz;

	ResponseType(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Retrieves {@link #clazz}
	 *
	 * @return value of {@link #clazz}
	 */
	public Class<?> getClazz() {
		return clazz;
	}

	public JsonNode getPaths(JsonNode jsonNode) {
		JsonNode root = jsonNode.path("Device");
		switch (this) {
			case DEVICE_INFO -> {
				return root.path("DeviceInfo");
			}
			case DEVICE_CAPABILITIES -> {
				return root.path("DeviceCapabilities");
			}
			default -> {
				return root;
			}
		}
	}
}
