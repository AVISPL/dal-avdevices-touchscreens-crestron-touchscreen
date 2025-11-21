/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseProperty;

/**
 * Represents system version properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public enum SystemVersions implements BaseProperty {
	VERSION("Version");

	private final String name;

	SystemVersions(String name) {
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
