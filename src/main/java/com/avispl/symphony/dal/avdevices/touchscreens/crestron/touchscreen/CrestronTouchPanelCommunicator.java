package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.CollectionUtils;

import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.IntervalSetting;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.adapter.RetrievalType;
import com.avispl.symphony.dal.communicator.RestCommunicator;
import com.avispl.symphony.dal.util.StringUtils;

public class CrestronTouchPanelCommunicator extends RestCommunicator implements Monitorable, Controller {
	/** Lock for thread-safe operations. */
	private final ReentrantLock reentrantLock;
	/** Jackson object mapper for JSON serialization and deserialization. */
	private final ObjectMapper objectMapper;

	/** Application configuration loaded from {@code version.properties}. */
	private final Properties versionProperties;
	/** Stores extended statistics to be sent to the aggregator. */
	private ExtendedStatistics localExtendedStatistics;

	/** Indicates whether control properties are visible; defaults to false. */
	private boolean isConfigManagement;
	/** Indicates whether groups are display; defaults to General. */
	private final Set<String> displayPropertyGroups;
	/** Interval control for retrieving data from APIs. */
	private final EnumMap<RetrievalType, IntervalSetting> retrievalIntervals;

	public CrestronTouchPanelCommunicator() {
		this.reentrantLock = new ReentrantLock();
		this.objectMapper = new ObjectMapper();

		this.versionProperties = new Properties();
		this.localExtendedStatistics = new ExtendedStatistics();

		this.isConfigManagement = false;
		this.displayPropertyGroups = new HashSet<>(Collections.singletonList(Constant.GENERAL_GROUP));
		this.retrievalIntervals = new EnumMap<>(RetrievalType.class);
	}

	public boolean configManagement() {
		return isConfigManagement;
	}

	public void setConfigManagement(boolean configManagement) {
		isConfigManagement = configManagement;
	}

	/**
	 * Returns a comma-separated list of property group names that are configured to be displayed.
	 *
	 * @return a comma-separated string of display property group names; may be empty if no groups are configured
	 */
	public String getDisplayPropertyGroups() {
		return String.join(Constant.COMMA, this.displayPropertyGroups);
	}

	/**
	 * Sets the list of property groups to be displayed, using a comma-separated string from adapter properties.
	 *
	 * @param displayPropertyGroups a comma-separated list of property group names to display; may be {@code null} or empty
	 */
	public void setDisplayPropertyGroups(String displayPropertyGroups) {
		if (StringUtils.isNullOrEmpty(displayPropertyGroups)) {
			return;
		}
		this.displayPropertyGroups.clear();
		Arrays.stream(displayPropertyGroups.split(Constant.COMMA)).map(String::trim)
				.filter(displayPropertyGroup -> !displayPropertyGroup.isEmpty())
				.forEach(this.displayPropertyGroups::add);
	}

	/**
	 * Returns the configured retrieval interval (in milliseconds) for general properties.
	 * <p>
	 * If the interval has not been explicitly set, a default {@link IntervalSetting} will be created and returned.
	 *
	 * @return the retrieval interval for General properties, in milliseconds
	 */
	public long getGeneralInterval() {
		return this.getIntervalSettingByType(RetrievalType.GENERAL).getIntervalMs();
	}

	/**
	 * Sets the retrieval interval(ms) for general properties from adapter properties.
	 *
	 * @param generalInterval the interval duration(ms)
	 */
	public void setGeneralInterval(long generalInterval) {
		this.retrievalIntervals.put(RetrievalType.GENERAL, new IntervalSetting(generalInterval));
	}

	/**
	 * Returns the configured retrieval interval (in milliseconds) for Capabilities group.
	 * <p>
	 * If the interval has not been explicitly set, a default {@link IntervalSetting} will be created and returned.
	 *
	 * @return the retrieval interval for Capabilities group, in milliseconds
	 */
	public long getCapabilitiesInterval() {
		return this.getIntervalSettingByType(RetrievalType.CAPABILITIES).getIntervalMs();
	}

	/**
	 * Sets the retrieval interval(ms) for Capabilities group.
	 *
	 * @param capabilitiesInterval the interval duration(ms)
	 */
	public void setCapabilitiesInterval(long capabilitiesInterval) {
		this.retrievalIntervals.put(RetrievalType.CAPABILITIES, new IntervalSetting(capabilitiesInterval));
	}

	/**
	 * Returns the configured retrieval interval (in milliseconds) for Display, Audio Display, and Button Toolbar Display group.
	 * <p>
	 * If the interval has not been explicitly set, a default {@link IntervalSetting} will be created and returned.
	 *
	 * @return the retrieval interval for Display, Audio Display, and Button Toolbar Display group., in milliseconds
	 */
	public long getDisplayInterval() {
		return this.getIntervalSettingByType(RetrievalType.DISPLAY).getIntervalMs();
	}

	/**
	 * Sets the retrieval interval(ms) for Display, Audio Display, and Button Toolbar Display group.
	 *
	 * @param displayInterval the interval duration(ms)
	 */
	public void setDisplayInterval(long displayInterval) {
		this.retrievalIntervals.put(RetrievalType.DISPLAY, new IntervalSetting(displayInterval));
	}

	/**
	 * Returns the configured retrieval interval (in milliseconds) for Network group.
	 * <p>
	 * If the interval has not been explicitly set, a default {@link IntervalSetting} will be created and returned.
	 *
	 * @return the retrieval interval for Network group, in milliseconds
	 */
	public long getNetworkInterval() {
		return this.getIntervalSettingByType(RetrievalType.NETWORK).getIntervalMs();
	}

	/**
	 * Sets the retrieval interval(ms) for Network group.
	 *
	 * @param networkInterval the interval duration(ms)
	 */
	public void setNetworkInterval(long networkInterval) {
		this.retrievalIntervals.put(RetrievalType.NETWORK, new IntervalSetting(networkInterval));
	}

	/**
	 * Returns the configured retrieval interval (in milliseconds) for System Versions group.
	 * <p>
	 * If the interval has not been explicitly set, a default {@link IntervalSetting} will be created and returned.
	 *
	 * @return the retrieval interval for System Versions group, in milliseconds
	 */
	public long getSystemVersionsInterval() {
		return this.getIntervalSettingByType(RetrievalType.SYSTEM_VERSIONS).getIntervalMs();
	}

	/**
	 * Sets the retrieval interval(ms) for System Versions group.
	 *
	 * @param systemVersionsInterval the interval duration(ms)
	 */
	public void setSystemVersionsInterval(long systemVersionsInterval) {
		this.retrievalIntervals.put(RetrievalType.SYSTEM_VERSIONS, new IntervalSetting(systemVersionsInterval));
	}

	@Override
	protected void internalInit() throws Exception {
		this.setTrustAllCertificates(true);
		this.setAuthenticationScheme(AuthenticationScheme.None);
		super.internalInit();
	}

	@Override
	protected void internalDestroy() {
		this.versionProperties.clear();
		this.localExtendedStatistics = null;
		this.displayPropertyGroups.clear();
		this.retrievalIntervals.clear();
		super.internalDestroy();
	}

	@Override
	protected void authenticate() throws Exception {

	}

	@Override
	public List<Statistics> getMultipleStatistics() throws Exception {
		this.reentrantLock.lock();
		try {

		} finally {
			this.reentrantLock.unlock();
		}
		return Collections.singletonList(this.localExtendedStatistics);
	}

	@Override
	public void controlProperty(ControllableProperty controllableProperty) throws Exception {
		this.reentrantLock.lock();
		try {

		} finally {
			this.reentrantLock.unlock();
		}
	}

	@Override
	public void controlProperties(List<ControllableProperty> controllableProperties) throws Exception {
		if (CollectionUtils.isEmpty(controllableProperties)) {
			if (this.logger.isWarnEnabled()) {
				this.logger.warn(Constant.CONTROLLABLE_PROPS_EMPTY_WARNING);
			}
			return;
		}
		for (ControllableProperty controllableProperty : controllableProperties) {
			this.controlProperty(controllableProperty);
		}
	}

	/**
	 * Returns the IntervalSetting for the given type, creating one if absent.
	 * Guarantees a non-null entry so callers don't need null checks.
	 */
	private IntervalSetting getIntervalSettingByType(RetrievalType type) {
		return this.retrievalIntervals.computeIfAbsent(type, t -> new IntervalSetting());
	}

	/**
	 * Checks whether the specified property group is configured to be displayed.
	 *
	 * @param groupName the name of the property group to check
	 * @return {@code true} if the group is configured to be displayed; {@code false} otherwise
	 */
	public boolean shouldDisplayGroup(String groupName) {
		return this.displayPropertyGroups.contains("All") || this.displayPropertyGroups.contains(groupName);
	}
}
