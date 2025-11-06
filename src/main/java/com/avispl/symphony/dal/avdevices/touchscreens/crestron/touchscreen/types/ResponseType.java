/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types;

import com.fasterxml.jackson.databind.JsonNode;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceInfo;

public enum ResponseType {
	DEVICE_INFO(DeviceInfo.class);

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
			default -> {
				return root;
			}
		}
	}
}
