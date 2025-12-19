/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.bases;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * The template for all network adapter.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public abstract class BaseAdapter {
	@JsonProperty("DomainName")
	private String domainName;
	@JsonProperty("LinkStatus")
	private Boolean linkStatus;
	@JsonProperty("MacAddress")
	private String macAddress;
}
