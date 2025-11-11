/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseProperty;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceInfo;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.SystemVersion;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.capabilities.DeviceCapabilities;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.capabilities.PortConfig;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.AdapterMetadata;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Capabilities;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.General;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.SystemVersions;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * Utility class providing helper methods for monitoring property.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public final class MonitoringUtil {
	private static final Log LOGGER = LogFactory.getLog(MonitoringUtil.class);

	private MonitoringUtil() {
	}

	/**
	 * Generates a map of property names and their corresponding values.
	 * <p>
	 * Each property name can be optionally prefixed with a group name using a predefined format.
	 * The values are derived using the provided mapping function, with {@link Constant#NOT_AVAILABLE} as a fallback for null results.
	 * </p>
	 *
	 * @param <T> the enum type that extends {@link BaseProperty}
	 * @param properties the array of enum constants to be processed; if null, an empty map is returned
	 * @param groupName optional group name used to prefix each property's name; can be null
	 * @param mapper a function that maps each property to its corresponding string value;
	 * if null or if the result is null, {@link Constant#NOT_AVAILABLE} is used as the value
	 * @return a map where keys are (optionally grouped) property names and values are mapped strings or {@link Constant#NOT_AVAILABLE}
	 */
	public static <T extends Enum<T> & BaseProperty> Map<String, String> generateProperties(T[] properties, String groupName, Function<T, String> mapper) {
		if (properties == null || mapper == null) {
			return Collections.emptyMap();
		}
		return Arrays.stream(properties).collect(Collectors.toMap(
				property -> Objects.isNull(groupName) ? property.getName() : String.format(Constant.PROPERTY_FORMAT, groupName, property.getName()),
				property -> Optional.ofNullable(mapper.apply(property)).orElse(Constant.NOT_AVAILABLE)
		));
	}

	/**
	 * Generates a key–value map representing all available {@link SystemVersion} properties.
	 * <p>
	 * Each {@link SystemVersion} produces multiple entries, one for each constant defined
	 * in {@link SystemVersions}. The property names are constructed using the format
	 * defined in {@link Constant#PROPERTY_FORMAT}, prefixed by
	 * {@link Constant#SYSTEM_VERSIONS_GROUP} and the sanitized, title-cased system version name.
	 * </p>
	 *
	 * @param systemVersions the list of {@link SystemVersion} entries to process; if null or empty, an empty map is returned
	 * @return a {@link Map} containing formatted property names as keys and the corresponding system version values as map entries
	 */
	public static Map<String, String> generateSystemVersionProperties(List<SystemVersion> systemVersions) {
		if (CollectionUtils.isEmpty(systemVersions)) {
			return Collections.emptyMap();
		}
		SystemVersions[] versionProps = SystemVersions.values();
		Map<String, String> properties = new HashMap<>();
		systemVersions.forEach(systemVersion -> {
			String prefixName = toTitleCase(Constant.NON_ALPHANUMERIC_PATTERN.matcher(systemVersion.getName()).replaceAll(Constant.EMPTY));
			Arrays.stream(versionProps).forEach(property -> properties.put(
					String.format(Constant.PROPERTY_FORMAT, Constant.SYSTEM_VERSIONS_GROUP, prefixName + property.getName()),
					Optional.ofNullable(mapToSystemVersion(systemVersion, property)).orElse(Constant.NOT_AVAILABLE)
			));
		});
		return properties;
	}

	/**
	 * Generates adapter metadata map from version properties. Returns empty property if null or all values unavailable.
	 *
	 * @param versionProperties adapter version and build information
	 * @return the Adapter Metadata map
	 */
	public static String mapToAdapterMetadata(Properties versionProperties, AdapterMetadata adapterMetadata) {
		if (versionProperties == null) {
			LOGGER.warn("The versionProperties is null, returning empty map");
			return null;
		}
		return switch (adapterMetadata) {
			case ADAPTER_UPTIME -> mapToUptime(versionProperties.getProperty(adapterMetadata.getProperty()));
			case ADAPTER_UPTIME_MIN -> mapToUptimeMin(versionProperties.getProperty(adapterMetadata.getProperty()));
			case ADAPTER_BUILD_DATE, ADAPTER_VERSION, ACTIVE_PROPERTY_GROUPS -> mapToValue(versionProperties.getProperty(adapterMetadata.getProperty()));
		};
	}

	/**
	 * Generates general property from device info object. Returns empty property if null or all values unavailable.
	 *
	 * @param deviceInfo device info object
	 * @return the General property
	 */
	public static String mapToGeneral(DeviceInfo deviceInfo, General general) {
		if (deviceInfo == null) {
			LOGGER.warn("The deviceInfo is null, returning empty property");
			return null;
		}
		return switch (general) {
			case BUILD_DATE -> mapToValue(deviceInfo.getBuildDate());
			case FIRMWARE_VERSION -> mapToValue(deviceInfo.getDeviceVersion());
			case PRODUCT_ID -> mapToValue(deviceInfo.getModelId());
			case NAME -> mapToValue(deviceInfo.getName());
			case PUF_VERSION -> mapToValue(deviceInfo.getPufVersion());
			case SERIAL_NUMBER -> mapToValue(deviceInfo.getSerialNumber());
		};
	}

	/**
	 * Generates capability property from device capabilities object. Returns empty property if null or all values unavailable.
	 *
	 * @param capabilities device capabilities object
	 * @return the Capability property
	 */
	public static String mapToCapabilities(DeviceCapabilities capabilities, Capabilities property) {
		if (capabilities == null) {
			LOGGER.warn("The capabilities is null, returning empty property");
			return null;
		}
		return switch (property) {
			case CONFIG_FILE_UPLOAD_SUPPORTED -> mapToValue(capabilities.getConfigFileUploadSupported());
			case LOG_FILE_UPLOAD_SUPPORTED -> mapToValue(capabilities.getLogFileUploadSupported());
			case PC_NUMBER_OF_DM_INPUT -> mapToValue(getPortConfig(capabilities).getNumberOfDmInputs());
			case PC_NUMBER_OF_ETHERNET_ADAPTERS -> mapToValue(getPortConfig(capabilities).getNumberOfEthernetAdapters());
			case PC_NUMBER_OF_HDMI_INPUTS -> mapToValue(getPortConfig(capabilities).getNumberOfHdmiInputs());
			case PC_NUMBER_OF_HDMI_OUTPUTS -> mapToValue(getPortConfig(capabilities).getNumberOfHdmiOutputs());
		};
	}

	/**
	 * Generates system version property from device info object. Returns empty property if null or all values unavailable.
	 *
	 * @param systemVersion system version object
	 * @return the System Versions property
	 */
	public static String mapToSystemVersion(SystemVersion systemVersion, SystemVersions property) {
		if (systemVersion == null) {
			LOGGER.warn("The systemVersion is null, returning empty property");
			return null;
		}
		return switch (property) {
			case CATEGORY -> mapToValue(systemVersion.getCategory());
			case VERSION -> mapToValue(systemVersion.getVersion());
		};
	}

	/**
	 * Retrieves the {@link PortConfig} instance from the given {@link DeviceCapabilities}.
	 * <p>
	 * If the provided {@code capabilities} object is {@code null}, or if its internal
	 * {@code portConfig} property is {@code null}, this method returns a new, empty
	 * {@link PortConfig} instance instead of {@code null}.
	 * </p>
	 *
	 * @param capabilities the {@link DeviceCapabilities} object from which to extract
	 *                     the {@link PortConfig}; may be {@code null}
	 * @return the existing {@link PortConfig} if available, or a new instance if
	 *         {@code capabilities} or its port configuration is {@code null}
	 */
	private static PortConfig getPortConfig(DeviceCapabilities capabilities) {
		return capabilities == null
				? new PortConfig()
				: Optional.ofNullable(capabilities.getPortConfig()).orElse(new PortConfig());
	}

	/**
	 * Maps the given value to a formatted String:
	 * <ul>
	 *   <li>If the value is a non-empty String, returns it in title case.</li>
	 *   <li>If the value is "true" or "false" (case-insensitive), returns it in lowercase.</li>
	 *   <li>If the value is a Boolean or Integer, returns its string representation.</li>
	 *   <li>Returns {@code Constant.NOT_AVAILABLE} if the value is null or empty.</li>
	 * </ul>
	 *
	 * @param value the input value to map
	 * @return the mapped String value, or {@code Constant.NOT_AVAILABLE} if unavailable
	 */
	private static String mapToValue(Object value) {
		if (value == null) {
			return Constant.NOT_AVAILABLE;
		}
		if (value instanceof String str) {
			if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
				return str.toLowerCase();
			}
			return StringUtils.isNotNullOrEmpty(str) ? toTitleCase(str) : Constant.NOT_AVAILABLE;
		}
		if (value instanceof Boolean || value instanceof Integer) {
			return value.toString();
		}

		return Constant.NOT_AVAILABLE;
	}

	/**
	 * Capitalizes the first character of the input string.
	 * <p>
	 * If the input is {@code null}, empty, or the literal string {@code "null"}, this method returns {@code null}.
	 * If the input is {@code "true"} or {@code "false"}, the method returns the input unchanged.
	 * Otherwise, it returns the input string with its first character converted to uppercase.
	 * </p>
	 *
	 * @param value the input string to convert
	 * @return a string with the first character capitalized, or {@code null} if the input is invalid
	 */
	private static String toTitleCase(String value) {
		if (StringUtils.isNullOrEmpty(value) || value.equals("null")) {
			return null;
		}
		if (value.equals("true") || value.equals("false")) {
			return value;
		}

		return Character.toUpperCase(value.charAt(0)) + value.substring(1);
	}

	/**
	 * Returns the elapsed uptime between the current system time and the given timestamp in milliseconds.
	 * <p>
	 * The input timestamp represents the start time in milliseconds (typically from {@link System#currentTimeMillis()}).
	 * The returned string represents the absolute duration in the format:
	 * "X day(s) Y hour(s) Z minute(s) W second(s)", omitting any zero-value units except seconds.
	 *
	 * @param uptime the start time in milliseconds as a string (e.g., "1717581000000")
	 * @return a formatted duration string like "2 day(s) 3 hour(s) 15 minute(s) 42 second(s)", or null if parsing fails
	 */
	private static String mapToUptime(String uptime) {
		try {
			if (StringUtils.isNullOrEmpty(uptime)) {
				return null;
			}

			long uptimeSecond = (System.currentTimeMillis() - Long.parseLong(uptime)) / 1000;
			long seconds = uptimeSecond % 60;
			long minutes = uptimeSecond % 3600 / 60;
			long hours = uptimeSecond % 86400 / 3600;
			long days = uptimeSecond / 86400;
			StringBuilder rs = new StringBuilder();
			if (days > 0) {
				rs.append(days).append(" day(s) ");
			}
			if (hours > 0) {
				rs.append(hours).append(" hour(s) ");
			}
			if (minutes > 0) {
				rs.append(minutes).append(" minute(s) ");
			}
			rs.append(seconds).append(" second(s)");

			return rs.toString().trim();
		} catch (Exception e) {
			LOGGER.error(Constant.MAP_TO_UPTIME_FAILED + uptime, e);
			return null;
		}
	}

	/**
	 * Returns the elapsed uptime in **whole minutes** between the current system time and the given timestamp in milliseconds.
	 * <p>
	 * The input timestamp represents the start time in milliseconds (typically from {@link System#currentTimeMillis()}).
	 * The returned string is the total number of minutes that have elapsed, excluding seconds.
	 *
	 * @param uptime the start time in milliseconds as a string (e.g., "1717581000000")
	 * @return a string representing the total number of elapsed minutes (e.g., "125"), or null if parsing fails
	 */
	private static String mapToUptimeMin(String uptime) {
		try {
			if (StringUtils.isNullOrEmpty(uptime)) {
				return null;
			}

			long uptimeSecond = (System.currentTimeMillis() - Long.parseLong(uptime)) / 1000;
			long minutes = uptimeSecond / 60;

			return String.valueOf(minutes);
		} catch (Exception e) {
			LOGGER.error(Constant.MAP_TO_UPTIME_MIN_FAILED + uptime, e);
			return null;
		}
	}
}
