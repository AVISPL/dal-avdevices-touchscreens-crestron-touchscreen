package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;

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
}
