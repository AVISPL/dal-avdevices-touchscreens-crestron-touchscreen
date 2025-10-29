package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants;

import java.time.Duration;

import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.dal.util.ControllablePropertyFactory;

public final class Constant {
	private Constant() {
	}

	//	Values
	public static final String NOT_AVAILABLE = "N/A";
	public static final AdvancedControllableProperty DUMMY_CONTROLLER = ControllablePropertyFactory.createText(null, null);
	public static final long DEFAULT_INTERVAL_MS = Duration.ofSeconds(30).toMillis();

	//	Special characters
	public static final String COMMA = ",";

	//	Groups
	public static final String GENERAL_GROUP = "General";

	//	Warning messages
	public static final String CONTROLLABLE_PROPS_EMPTY_WARNING = "ControllableProperties list is null or empty, skipping control operation";

	//	Fail messages
	public static final String REQUEST_APIS_FAILED = "Unable to process requested API sections: [%s], error reported: [%s]";
}
