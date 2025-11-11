/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants;

import java.time.Duration;
import java.util.regex.Pattern;

import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.dal.util.ControllablePropertyFactory;

/**
 * Utility class that defines constant values used across the application.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public final class Constant {
	private Constant() {
	}

	//	Formats
	public static final String PROPERTY_FORMAT = "%s#%s";
	public static final Pattern NON_ALPHANUMERIC_PATTERN = Pattern.compile("[^a-zA-Z0-9]");

	//	Values
	public static final String NOT_AVAILABLE = "N/A";
	public static final AdvancedControllableProperty DUMMY_CONTROLLER = ControllablePropertyFactory.createText(null, null);
	public static final long DEFAULT_INTERVAL_MS = Duration.ofSeconds(30).toMillis();
	public static final String CREST_XSRF_TOKEN_HEADER = "CREST-XSRF-TOKEN";
	public static final String X_CREST_XSRF_TOKEN_HEADER = "X-CREST-XSRF-TOKEN";

	//	Special characters
	public static final String COMMA = ",";
	public static final String SPACE = " ";
	public static final String EMPTY = "";

	//	Groups
	public static final String GENERAL_GROUP = "General";
	public static final String ADAPTER_METADATA_GROUP = "AdapterMetadata";
	public static final String CAPABILITIES_GROUP = "Capabilities";
	public static final String SYSTEM_VERSIONS_GROUP = "SystemVersions";

	//	Warning messages
	public static final String CONTROLLABLE_PROPS_EMPTY_WARNING = "ControllableProperties list is null or empty, skipping control operation";
	public static final String UNSUPPORTED_MAP_PROPERTY_WARNING = "Unsupported %s with property %s";
	public static final String FETCHED_DATA_NULL_WARNING = "Fetched data is null. Endpoint: %s, ResponseClass: %s";

	//	Fail messages
	public static final String REQUEST_APIS_FAILED = "Unable to process requested API sections: [%s], error reported: [%s]";
	public static final String READ_PROPERTIES_FILE_FAILED = "Failed to load version properties file.";
	public static final String FETCH_DATA_FAILED = "Exception while fetching data. Endpoint: %s, ResponseClass: %s";
	public static final String MAP_TO_UPTIME_FAILED = "Failed to mapToUptime with uptime: ";
	public static final String MAP_TO_UPTIME_MIN_FAILED = "Failed to mapToUptimeMin with uptime: ";
	public static final String LOGIN_FAILED = "Failed to login, please check the credentials";
}
