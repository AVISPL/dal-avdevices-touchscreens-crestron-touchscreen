/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay;

/**
 * Defines different display edge for {@link DeviceDisplay}.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@Getter
public enum DisplayEdge {
	TOP("Top"),
	BOTTOM("Bottom"),
	LEFT("Left"),
	RIGHT("Right");

	private final String value;

	DisplayEdge(String value) {
		this.value = value;
	}

	public static List<String> getValues() {
		return Arrays.stream(values()).map(DisplayEdge::getValue).toList();
	}
}
