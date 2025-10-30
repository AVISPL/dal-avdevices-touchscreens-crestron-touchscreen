package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseProperty;

/**
 * Represents adapter metadata properties.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public enum AdapterMetadata implements BaseProperty {
	ADAPTER_BUILD_DATE("AdapterBuildDate", "adapter.build.date"),
	ADAPTER_UPTIME("AdapterUptime", "adapter.uptime"),
	ADAPTER_UPTIME_MIN("AdapterUptime(min)", "adapter.uptime"),
	ADAPTER_VERSION("AdapterVersion", "adapter.version"),
	ACTIVE_PROPERTY_GROUPS("ActivePropertyGroups", "adapter.active.property.groups");

	private final String name;
	private final String property;

	AdapterMetadata(String name, String property) {
		this.name = name;
		this.property = property;
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

	/**
	 * Retrieves {@link #property}
	 *
	 * @return value of {@link #property}
	 */
	public String getProperty() {
		return property;
	}
}
