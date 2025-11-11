/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types;

import java.util.List;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceInfo;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.SystemVersion;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.capabilities.DeviceCapabilities;

/**
 * Defines different response types and their associated model classes.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public enum ResponseType {
	DEVICE_INFO(DeviceInfo.class),
	DEVICE_CAPABILITIES(DeviceCapabilities.class),
	SYSTEM_VERSIONS(SystemVersion.class);

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

	/**
	 * Extracts a specific sub-node from the given JSON tree based on the current {@code ResponseType}.
	 *
	 * @param jsonNode the root {@link JsonNode} representing the parsed JSON response.
	 * @return the extracted {@link JsonNode} corresponding to the current {@code ResponseType}.
	 */
	public JsonNode extractNode(JsonNode jsonNode) {
		JsonNode root = jsonNode.path("Device");
		switch (this) {
			case DEVICE_INFO -> {
				return root.path("DeviceInfo");
			}
			case DEVICE_CAPABILITIES -> {
				return root.path("DeviceCapabilities");
			}
			case SYSTEM_VERSIONS -> {
				return root.path("SystemVersions").path("Components");
			}
			default -> {
				return root;
			}
		}
	}

	/**
	 * Determines whether the response type represents a collection of items.
	 *
	 * @return {@code true} if this response type is a collection, {@code false} otherwise
	 */
	public boolean isCollection() {
		return SYSTEM_VERSIONS.equals(this);
	}

	/**
	 * Returns the Jackson {@link JavaType} representing a collection of the target class.
	 * <p>
	 * This method is intended for enum constants that represent list responses.
	 * If the current instance does not represent a collection, an {@link IllegalStateException} is thrown.
	 * </p>
	 *
	 * @param mapper the {@link ObjectMapper} used to construct the type reference
	 * @return a {@link JavaType} representing a {@link List} of the target class
	 * @throws IllegalStateException if this instance does not represent a collection response
	 */
	public JavaType getTypeRef(ObjectMapper mapper) {
		if (!this.isCollection()) {
			throw new IllegalStateException("This instance is not marked as a collection type");
		}
		return mapper.getTypeFactory().constructCollectionType(List.class, this.clazz);
	}
}
