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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases.BaseProperty;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceCapabilities;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceInfo;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.SystemVersion;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay.Audio;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay.VirtualButtons;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.Lcd;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.Lcd.AutoBrightness;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.Lcd.Presets;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.NetworkAdapters;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.adapters.IPv6;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.AdapterMetadata;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Capabilities;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Display;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.General;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Network;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.SystemVersions;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * Utility class providing helper methods for monitoring property.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MonitoringUtil {
	private static final Log LOGGER = LogFactory.getLog(MonitoringUtil.class);

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
	 * Generates a key–value map representing all available {@link DeviceDisplay} properties.
	 * <p>
	 * Each {@link Display} entry produces one or more key–value pairs using the naming format
	 * defined in {@link Constant#PROPERTY_FORMAT}, prefixed by
	 * {@link Constant#DISPLAY_GROUP}. Values are normalized via mapping helpers and fall back
	 * to {@link Constant#NOT_AVAILABLE} when not provided.
	 * </p>
	 *
	 * @param display the {@link DeviceDisplay} instance to process; if null, an empty map is returned
	 * @return a {@link Map} containing formatted property names and their corresponding values
	 */
	public static Map<String, String> generateDisplayProperties(DeviceDisplay display) {
		if (display == null) {
			LOGGER.warn("The display is null, returning empty property");
			return Collections.emptyMap();
		}
		String prefixName = Constant.DISPLAY_GROUP;
		AutoBrightness autoBrightness = Util.getLcdAutoBrightness(display);
		Audio audio = Util.getDisplayAudio(display);
		Lcd lcd = Util.getLcd(display);
		Presets presets = Util.getLcdPresets(display);
		VirtualButtons buttonToolbar = Util.getDisplayButtonToolbar(display);
		Map<String, String> properties = new HashMap<>();
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.DISPLAY_STATUS.getName()),
				Optional.ofNullable(mapToValue(display.getCurrentState())).orElse(Constant.NOT_AVAILABLE)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LOCAL_SETUP_SEQUENCE.getName()),
				mapToValue(autoBrightness.getIsEnabled(), Constant.ON, Constant.OFF)
		);
		//	Audio
		//	Audio.Panel
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_PANEL_MUTE.getName()),
				mapToValue(audio.getIsMuted(), Constant.ON, Constant.OFF)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_PANEL_VOLUME.getName()),
				Optional.ofNullable(mapToValue(audio.getVolume())).orElse(Constant.NOT_AVAILABLE)
		);
		if (Boolean.FALSE.equals(audio.getIsMuted())) {
			properties.put(
					Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_PANEL_VOLUME_VALUE.getName()),
					Optional.ofNullable(mapToValue(audio.getVolume())).orElse(Constant.NOT_AVAILABLE)
			);
		}
		//	Audio.Media
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_MEDIA_MUTE.getName()),
				mapToValue(audio.getIsMediaMuted(), Constant.ON, Constant.OFF)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_MEDIA_VOLUME.getName()),
				Optional.ofNullable(mapToValue(audio.getMediaVolume())).orElse(Constant.NOT_AVAILABLE)
		);
		if (Boolean.FALSE.equals(audio.getIsMediaMuted())) {
			properties.put(
					Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_MEDIA_VOLUME_VALUE.getName()),
					Optional.ofNullable(mapToValue(audio.getMediaVolume())).orElse(Constant.NOT_AVAILABLE)
			);
		}
		//	Audio.Beep
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_BEEP_ENABLED.getName()),
				mapToValue(audio.getIsBeepEnabled(), Constant.ON, Constant.OFF)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_BEEP_VOLUME.getName()),
				Optional.ofNullable(mapToValue(audio.getBeepVolume())).orElse(Constant.NOT_AVAILABLE)
		);
		if (Boolean.TRUE.equals(audio.getIsBeepEnabled())) {
			properties.put(
					Constant.PROPERTY_FORMAT.formatted(prefixName, Display.AUDIO_BEEP_VOLUME_VALUE.getName()),
					Optional.ofNullable(mapToValue(audio.getBeepVolume())).orElse(Constant.NOT_AVAILABLE)
			);
		}
		//	LCD
		//	LCD.Brightness
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_AUTO_BRIGHTNESS.getName()),
				mapToValue(autoBrightness.getIsEnabled(), Constant.ON, Constant.OFF)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_BRIGHTNESS.getName()),
				Optional.ofNullable(mapToValue(lcd.getBrightness())).orElse(Constant.NOT_AVAILABLE)
		);
		if (Boolean.FALSE.equals(autoBrightness.getIsEnabled())) {
			properties.put(
					Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_BRIGHTNESS_VALUE.getName()),
					Optional.ofNullable(mapToValue(lcd.getBrightness())).orElse(Constant.NOT_AVAILABLE)
			);
		}
		//	LCD.Threshold
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_ALS_THRESHOLD.getName()),
				Optional.ofNullable(mapToValue(autoBrightness.getThresholdValue())).orElse(Constant.NOT_AVAILABLE)
		);
		if (Boolean.TRUE.equals(autoBrightness.getIsEnabled())) {
			properties.put(
					Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_ALS_THRESHOLD_VALUE.getName()),
					Optional.ofNullable(mapToValue(autoBrightness.getThresholdValue())).orElse(Constant.NOT_AVAILABLE)
			);
		}
		//	LCD.HighPreset
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_BRIGHTNESS_HIGH_PRESET.getName()),
				Optional.ofNullable(mapToValue(presets.getHighLevel())).orElse(Constant.NOT_AVAILABLE)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_BRIGHTNESS_HIGH_PRESET_VALUE.getName()),
				Optional.ofNullable(mapToValue(presets.getHighLevel())).orElse(Constant.NOT_AVAILABLE)
		);
		//	LCD.LowPreset
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_BRIGHTNESS_LOW_PRESET.getName()),
				Optional.ofNullable(mapToValue(presets.getLowLevel())).orElse(Constant.NOT_AVAILABLE)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_BRIGHTNESS_LOW_PRESET_VALUE.getName()),
				Optional.ofNullable(mapToValue(presets.getLowLevel())).orElse(Constant.NOT_AVAILABLE)
		);
		//	LCD.StandByTimeout
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_STANDBY_TIMEOUT.getName()),
				Optional.ofNullable(mapToValue(lcd.getStandbyTimeoutMinutes())).orElse(Constant.NOT_AVAILABLE)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.LCD_STANDBY_TIMEOUT_VALUE.getName()),
				Optional.ofNullable(mapToValue(lcd.getStandbyTimeoutMinutes())).orElse(Constant.NOT_AVAILABLE)
		);
		//	ButtonToolbar
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.BUTTON_TOOLBAR_SHOW_ON_WAKE.getName()),
				mapToValue(buttonToolbar.getIsShowOnWakeEnabled(), Constant.ON, Constant.OFF)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.BUTTON_TOOLBAR_SHOW_DURING_STANDBY.getName()),
				mapToValue(buttonToolbar.getIsShowOnWakeEnabled(), Constant.ON, Constant.OFF)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.BUTTON_TOOLBAR_DISPLAY_EDGE.getName()),
				Optional.ofNullable(mapToValue(buttonToolbar.getDisplayEdge())).orElse(Constant.NOT_AVAILABLE)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.BUTTON_TOOLBAR_AUTO_HIDE_TIMEOUT.getName()),
				Optional.ofNullable(mapToValue(buttonToolbar.getAutoHideTimeOutSeconds())).orElse(Constant.NOT_AVAILABLE)
		);
		properties.put(
				Constant.PROPERTY_FORMAT.formatted(prefixName, Display.BUTTON_TOOLBAR_AUTO_HIDE_TIMEOUT_VALUE.getName()),
				Optional.ofNullable(mapToValue(buttonToolbar.getAutoHideTimeOutSeconds())).orElse(Constant.NOT_AVAILABLE)
		);
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
			case CATEGORY -> mapToValue(deviceInfo.getCategory());
			case DEVICE_ID -> mapToValue(deviceInfo.getDeviceId());
			case DEVICE_KEY -> mapToValue(deviceInfo.getDeviceKey());
			case FIRMWARE_VERSION -> mapToValue(deviceInfo.getDeviceVersion());
			case PRODUCT_ID -> mapToValue(deviceInfo.getModelId());
			case MAC_ADDRESS -> mapToValue(deviceInfo.getMacAddress());
			case MANUFACTURER -> mapToValue(deviceInfo.getManufacturer());
			case MODEL -> mapToValue(deviceInfo.getModel());
			case NAME -> mapToValue(deviceInfo.getName());
			case REBOOT_REASON -> mapToValue(deviceInfo.getRebootReason());
			case SERIAL_NUMBER -> mapToValue(deviceInfo.getSerialNumber());
			case VERSION -> mapToValue(deviceInfo.getVersion());
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
			case CONFIG_FILE_UPLOAD_SUPPORTED -> mapToValue(capabilities.getIsConfigFileUploadSupported());
			case LOG_FILE_UPLOAD_SUPPORTED -> mapToValue(capabilities.getIsLogFileUploadSupported());
			case PC_NUMBER_OF_DM_INPUT -> mapToValue(Util.getPortConfig(capabilities).getNumberOfDmInputs());
			case PC_NUMBER_OF_ETHERNET_ADAPTERS -> mapToValue(Util.getPortConfig(capabilities).getNumberOfEthernetAdapters());
			case PC_NUMBER_OF_HDMI_INPUTS -> mapToValue(Util.getPortConfig(capabilities).getNumberOfHdmiInputs());
			case PC_NUMBER_OF_HDMI_OUTPUTS -> mapToValue(Util.getPortConfig(capabilities).getNumberOfHdmiOutputs());
		};
	}

	/**
	 * Generates network adapters property from device capabilities object. Returns empty property if null or all values unavailable.
	 *
	 * @param networkAdapters network adapters object
	 * @return the Network property
	 */
	public static String mapToNetwork(NetworkAdapters networkAdapters, Network property) {
		if (networkAdapters == null) {
			LOGGER.warn("The networkAdapters is null, returning empty property");
			return null;
		}
		return switch (property) {
			case DNS_SERVERS -> mapToValue(String.join(Constant.COMMA, Util.getIPv4(networkAdapters.getDnsSettings()).getDnsServers()));
			case HOSTNAME -> mapToValue(networkAdapters.getHostName());
			case IPV6_ENABLED -> mapToValue(Optional.ofNullable(networkAdapters.getIPv6()).orElse(new IPv6()).getSupported(), "Yes", "No");
			case LAN_DEFAULT_GATEWAY -> mapToValue(Util.getIPv4(networkAdapters.getAdapters()).getDefaultGateway());
			case LAN_DHCP_ENABLED -> mapToValue(Util.getIPv4(networkAdapters.getAdapters()).getIsDhcpEnabled(), Constant.ON, Constant.OFF);
			case LAN_DOMAIN_NAME -> mapToValue(Util.getEthernetLan(networkAdapters.getAdapters()).getDomainName(), false);
			case LAN_IP_ADDRESS -> mapToValue(Util.getFirstAddress(networkAdapters.getAdapters()).getAddress());
			case LAN_LINK_ACTIVE -> mapToValue(Util.getEthernetLan(networkAdapters.getAdapters()).getLinkStatus());
			case LAN_SUBNET_MASK -> mapToValue(Util.getFirstAddress(networkAdapters.getAdapters()).getSubnetMask());
			case WIFI_DOMAIN_NAME -> mapToValue(Util.getWifi(networkAdapters.getAdapters()).getDomainName(), false);
			case WIFI_LINK_ACTIVE -> mapToValue(Util.getWifi(networkAdapters.getAdapters()).getLinkStatus());
			case WIFI_MAC_ADDRESS -> mapToValue(Util.getWifi(networkAdapters.getAdapters()).getMacAddress());
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
	 * Maps the given value to a formatted string using title case for normal text.
	 * <p>
	 * Delegates to {@link #mapToValue(Object, boolean)} with {@code isTitleCase = true}.
	 * </p>
	 *
	 * @param value the input value to map
	 * @return the mapped string, or {@code Constant.NOT_AVAILABLE} if unavailable
	 */
	private static String mapToValue(Object value) {
		return mapToValue(value, true);
	}

	/**
	 * Maps the given value to a formatted string based on its type:
	 * <ul>
	 *   <li>For non-empty strings:
	 *     <ul>
	 *       <li>Returns "true" / "false" in lowercase if the value represents a boolean.</li>
	 *       <li>Returns title-cased or raw text depending on {@code isTitleCase}.</li>
	 *     </ul>
	 *   </li>
	 *   <li>For {@link Boolean} or {@link Integer}, returns their string value.</li>
	 *   <li>Returns {@code null} or unsupported types.</li>
	 * </ul>
	 *
	 * @param value the value to map
	 * @param isTitleCase whether normal string values should be converted to title case
	 * @return the mapped string, or {@code null} if unavailable
	 */
	private static String mapToValue(Object value, boolean isTitleCase) {
		if (value == null) {
			return null;
		}
		if (value instanceof String str) {
			if (StringUtils.isNullOrEmpty(str)) {
				return null;
			}
			if ("true".equalsIgnoreCase(str) || "false".equalsIgnoreCase(str)) {
				return str.toLowerCase();
			}
			return isTitleCase ? toTitleCase(str) : str;
		}
		if (value instanceof Boolean || value instanceof Integer) {
			return value.toString();
		}

		return null;
	}

	/**
	 * Maps a {@link Boolean} to either the given {@code trueValue} or {@code falseValue},
	 * using {@link #mapToValue(Object)} to normalize the output format.
	 *
	 * @param value      the boolean value to evaluate; null is treated as {@code false}
	 * @param trueValue  the string to use when {@code value} is {@code true}
	 * @param falseValue the string to use when {@code value} is not {@code true}
	 * @return the mapped string produced from either {@code trueValue} or {@code falseValue}
	 */
	private static String mapToValue(Boolean value, String trueValue, String falseValue) {
		return Objects.equals(Boolean.TRUE, value) ? mapToValue(trueValue) : mapToValue(falseValue);
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
