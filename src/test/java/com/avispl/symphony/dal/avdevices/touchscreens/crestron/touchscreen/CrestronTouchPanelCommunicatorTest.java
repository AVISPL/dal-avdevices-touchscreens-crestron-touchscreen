/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.DisplayEdge;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Display;

/**
 * Unit tests for the {@link CrestronTouchPanelCommunicator} class.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
class CrestronTouchPanelCommunicatorTest {
	private ExtendedStatistics extendedStatistics;
	private CrestronTouchPanelCommunicator communicator;

	@BeforeEach
	void setUp() throws Exception {
		this.communicator = new CrestronTouchPanelCommunicator();
		this.communicator.setHost("");
		this.communicator.setPort(443);
		this.communicator.setLogin("");
		this.communicator.setPassword("");
		this.communicator.init();
		this.communicator.connect();
	}

	@AfterEach
	void destroy() throws Exception {
		this.communicator.disconnect();
		this.communicator.destroy();
	}

	@Test
	void testGetMultipleStatistics() throws Exception {
		this.extendedStatistics = (ExtendedStatistics) this.communicator.getMultipleStatistics().get(0);
		Map<String, String> statistics = this.extendedStatistics.getStatistics();

		this.verifyStatistics(statistics);
	}

	@Test
	void testControlPropertyWithDropdown() throws Exception {
		String propertyName = String.format(Constant.PROPERTY_FORMAT, Constant.DISPLAY_GROUP, Display.BUTTON_TOOLBAR_DISPLAY_EDGE.getName());
		String value = DisplayEdge.LEFT.getValue();
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(value);

		this.communicator.controlProperty(controllableProperty);
		this.extendedStatistics = (ExtendedStatistics) this.communicator.getMultipleStatistics().get(0);
		Map<String, String> statistics = this.extendedStatistics.getStatistics();
		String verifiedValue = statistics.get(propertyName);
		Assertions.assertNotNull(verifiedValue, "Can't found the property name %s".formatted(propertyName));
		Assertions.assertEquals(value, verifiedValue, "The value of %s does not equals".formatted(propertyName));
	}

	@Test
	void testControlPropertyWithSwitch() throws Exception {
		String propertyName = String.format(Constant.PROPERTY_FORMAT, Constant.DISPLAY_GROUP, Display.AUDIO_PANEL_MUTE.getName());
		String value = "0";
		ControllableProperty controllableProperty = new ControllableProperty();
		controllableProperty.setProperty(propertyName);
		controllableProperty.setValue(value);

		this.communicator.controlProperty(controllableProperty);
		this.extendedStatistics = (ExtendedStatistics) this.communicator.getMultipleStatistics().get(0);
		Map<String, String> statistics = this.extendedStatistics.getStatistics();
		String verifiedValue = statistics.get(propertyName);
		String comparedValue = "1".equals(value) ? Constant.ON : Constant.OFF;
		Assertions.assertNotNull(verifiedValue, "Can't found the property name %s".formatted(propertyName));
		Assertions.assertEquals(comparedValue, verifiedValue, "The value of %s does not equals".formatted(propertyName));
	}

	private void verifyStatistics(Map<String, String> statistics) {
		Map<String, Map<String, String>> groups = new LinkedHashMap<>();
		groups.put(Constant.GENERAL_GROUP, this.filterGroupStatistics(statistics, null));
		groups.put(Constant.ADAPTER_METADATA_GROUP, this.filterGroupStatistics(statistics, Constant.ADAPTER_METADATA_GROUP));
		groups.put(Constant.CAPABILITIES_GROUP, this.filterGroupStatistics(statistics, Constant.CAPABILITIES_GROUP));
		groups.put(Constant.SYSTEM_VERSIONS_GROUP, this.filterGroupStatistics(statistics, Constant.SYSTEM_VERSIONS_GROUP));
		groups.put(Constant.NETWORK_GROUP, this.filterGroupStatistics(statistics, Constant.NETWORK_GROUP));
		groups.put(Constant.DISPLAY_GROUP, this.filterGroupStatistics(statistics, Constant.DISPLAY_GROUP));

		for (Map<String, String> initGroup : groups.values()) {
			for (Map.Entry<String, String> initStatistics : initGroup.entrySet()) {
				Assertions.assertNotNull(initStatistics.getValue(), "Value is null with property: " + initStatistics.getKey());
			}
		}
	}

	private Map<String, String> filterGroupStatistics(Map<String, String> statistics, String groupName) {
		return statistics.entrySet().stream()
				.filter(e -> {
					if (groupName == null) {
						return !e.getKey().contains("#");
					}
					return e.getKey().startsWith(groupName);
				})
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}
}
