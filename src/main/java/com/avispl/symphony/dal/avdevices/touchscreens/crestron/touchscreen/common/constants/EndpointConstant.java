/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Utility class that defines API endpoint paths and URI patterns.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EndpointConstant {
	public static final String LOGIN = "/userlogin.html";
	public static final String LOGOUT = "/logout";
	public static final String DEVICE_INFO = "/Device/DeviceInfo";
	public static final String DEVICE_CAPABILITIES = "/Device/DeviceCapabilities";
	public static final String SYSTEM_VERSIONS = "/Device/SystemVersions";
	public static final String NETWORK_ADAPTERS = "/Device/NetworkAdapters";
	public static final String DISPLAY = "/Device/Display";
}
