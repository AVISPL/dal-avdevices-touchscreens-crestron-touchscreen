/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException.Forbidden;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.security.auth.login.FailedLoginException;
import org.apache.commons.collections.CollectionUtils;

import com.avispl.symphony.api.common.error.InvalidArgumentException;
import com.avispl.symphony.api.dal.control.Controller;
import com.avispl.symphony.api.dal.dto.control.AdvancedControllableProperty;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.api.dal.dto.monitor.Statistics;
import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.api.dal.monitor.Monitorable;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.RequestStateHandler;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.utils.ControlUtil;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.utils.MonitoringUtil;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.AuthCookie;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceCapabilities;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.DeviceInfo;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.IntervalSetting;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.SystemVersion;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.NetworkAdapters;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.ResponseType;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.adapter.RetrievalType;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.AdapterMetadata;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Capabilities;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Display;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.General;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Network;
import com.avispl.symphony.dal.communicator.RestCommunicator;
import com.avispl.symphony.dal.util.StringUtils;

/**
 * Main adapter class for Crestron Touch Panel. Responsible for generating monitoring, controllable.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public class CrestronTouchPanelCommunicator extends RestCommunicator implements Monitorable, Controller {
	/** Lock for thread-safe operations. */
	private final ReentrantLock reentrantLock;
	/** Object mapper used to convert JSON responses into Java objects. */
	private final ObjectMapper objectMapper;
	private final RequestStateHandler requestStateHandler;

	/** Device adapter instantiation timestamp. */
	private final long adapterInitializationTimestamp;
	/** Application configuration loaded from {@code version.properties}. */
	private final Properties versionProperties;
	/** Stores extended statistics to be sent to the adapter. */
	private ExtendedStatistics localExtendedStatistics;
	/** Authentication cookie data used for login session in {@link #authenticate()}. */
	private AuthCookie authCookie;
	/** Device information retrieved from {@link EndpointConstant#DEVICE_INFO}. */
	private DeviceInfo deviceInfo;
	/** Device capabilities retrieved from {@link EndpointConstant#DEVICE_CAPABILITIES}. */
	private DeviceCapabilities deviceCapabilities;
	/** System versions retrieved from {@link EndpointConstant#SYSTEM_VERSIONS}. */
	private List<SystemVersion> systemVersions;
	/** Network adapters retrieved from {@link EndpointConstant#NETWORK_ADAPTERS}. */
	private NetworkAdapters networkAdapters;
	/** Display retrieved from {@link EndpointConstant#DISPLAY}. */
	private DeviceDisplay deviceDisplay;

	/** Indicates whether control properties are visible; defaults to false. */
	private boolean isConfigManagement;
	/** Indicates whether groups are displayed; defaults to General. */
	private final Set<String> displayPropertyGroups;
	/** Interval control for retrieving data from APIs. */
	private final EnumMap<RetrievalType, IntervalSetting> retrievalIntervals;

	public CrestronTouchPanelCommunicator() {
		this.reentrantLock = new ReentrantLock();
		this.objectMapper = new ObjectMapper();
		this.requestStateHandler = new RequestStateHandler();

		this.adapterInitializationTimestamp = System.currentTimeMillis();
		this.versionProperties = new Properties();
		this.localExtendedStatistics = new ExtendedStatistics();
		this.authCookie = new AuthCookie();
		this.deviceInfo = new DeviceInfo();
		this.deviceCapabilities = new DeviceCapabilities();
		this.systemVersions = new ArrayList<>();
		this.networkAdapters = new NetworkAdapters();
		this.deviceDisplay = new DeviceDisplay();

		this.isConfigManagement = false;
		this.displayPropertyGroups = new LinkedHashSet<>(Set.of(Constant.GENERAL_GROUP));
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
		this.loadProperties(this.versionProperties);
		super.internalInit();
	}

	@Override
	protected void internalDestroy() {
		this.versionProperties.clear();
		this.localExtendedStatistics = null;
		this.requestStateHandler.clear();
		this.authCookie = null;
		this.deviceInfo = null;
		this.deviceCapabilities = null;
		this.systemVersions = null;
		this.networkAdapters = null;
		this.deviceDisplay = null;
		this.displayPropertyGroups.clear();
		this.retrievalIntervals.clear();
		super.internalDestroy();
	}

	@Override
	protected void authenticate() throws Exception {
		if (StringUtils.isNullOrEmpty(this.getLogin(), true)
				|| StringUtils.isNullOrEmpty(this.getPassword(), true)) {
			throw new FailedLoginException(Constant.LOGIN_FAILED);
		}
		try {
			RestTemplate restTemplate = this.obtainRestTemplate();
			final String baseUrl = this.getProtocol() + "://" + this.host + ":" + this.getPort();
			final String loginUrl = baseUrl + EndpointConstant.LOGIN;
			//	Send GET login request to fetch TRACK ID cookie, required for POST login request.
			if (this.authCookie.getTrackId() == null) {
				ResponseEntity<String> getLoginResponse = restTemplate.exchange(loginUrl, HttpMethod.GET, HttpEntity.EMPTY, String.class);
				List<String> getCookies = getLoginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
				if (CollectionUtils.isNotEmpty(getCookies)) {
					String trackIdCookieValue = getCookies.get(0);
					this.authCookie.setTrackId(trackIdCookieValue);
					this.authCookie.setOrigin(this.host);
					this.authCookie.setLoginReferer(this.host + EndpointConstant.LOGIN);
				}
			}
			//	Send POST login request to fetch Set-Cookie and refresh token.
			if (this.authCookie.getCookie() == null) {
				//	Call the Logout API to clear the login session
				restTemplate.exchange(baseUrl + EndpointConstant.LOGOUT, HttpMethod.GET, HttpEntity.EMPTY, String.class);
				HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(
						this.authCookie.getFormURLEncodedBody(this.getLogin(), this.getPassword()),
						this.authCookie.getRequestHeaders()
				);
				ResponseEntity<String> postLoginResponse = restTemplate.exchange(loginUrl, HttpMethod.POST, request, String.class);
				List<String> postCookies = postLoginResponse.getHeaders().get(HttpHeaders.SET_COOKIE);
				if (CollectionUtils.isNotEmpty(postCookies)) {
					this.authCookie.setCookie(String.join(Constant.COMMA, postCookies));
					this.authCookie.setRefreshToken(postLoginResponse.getHeaders().getFirst(Constant.CREST_XSRF_TOKEN_HEADER));
				}
			}
		} catch (Unauthorized | Forbidden ex) {
			throw new FailedLoginException(ex.getResponseBodyAsString());
		} catch (ResourceAccessException ex) {
			throw new ResourceNotReachableException(ex.getCause().getMessage(), ex);
		}
	}

	@Override
	protected HttpHeaders putExtraRequestHeaders(HttpMethod httpMethod, String uri, HttpHeaders headers) throws Exception {
		headers.set(HttpHeaders.COOKIE, this.authCookie.getCookie());
		if (HttpMethod.POST.equals(httpMethod)) {
			headers.set(Constant.X_CREST_XSRF_TOKEN_HEADER, this.authCookie.getRefreshToken());
		}
		return super.putExtraRequestHeaders(httpMethod, uri, headers);
	}

	@Override
	public List<Statistics> getMultipleStatistics() throws Exception {
		this.reentrantLock.lock();
		try {
			this.setupData();
			ExtendedStatistics extendedStatistics = new ExtendedStatistics();
			Map<String, String> statistics = new HashMap<>();
			statistics.putAll(MonitoringUtil.generateProperties(
					General.values(), null,
					property -> MonitoringUtil.mapToGeneral(this.deviceInfo, property)
			));
			statistics.putAll(MonitoringUtil.generateProperties(
					AdapterMetadata.values(), Constant.ADAPTER_METADATA_GROUP,
					property -> MonitoringUtil.mapToAdapterMetadata(this.versionProperties, property)
			));
			statistics.putAll(MonitoringUtil.generateProperties(
					Capabilities.values(), Constant.CAPABILITIES_GROUP,
					property -> MonitoringUtil.mapToCapabilities(this.deviceCapabilities, property)
			));
			statistics.putAll(MonitoringUtil.generateSystemVersionProperties(this.systemVersions));
			statistics.putAll(MonitoringUtil.generateProperties(
					Network.values(), Constant.NETWORK_GROUP,
					property -> MonitoringUtil.mapToNetwork(this.networkAdapters, property)
			));
			statistics.putAll(MonitoringUtil.generateDisplayProperties(this.deviceDisplay));

			List<AdvancedControllableProperty> controllableProperties = Optional.of(ControlUtil.generateDisplayControllers(this.deviceDisplay))
					.filter(list -> !list.isEmpty()).orElseGet(() -> Collections.singletonList(Constant.DUMMY_CONTROLLER));

			extendedStatistics.setStatistics(statistics);
			extendedStatistics.setControllableProperties(controllableProperties);
			this.localExtendedStatistics = extendedStatistics;
		} finally {
			this.reentrantLock.unlock();
		}
		return Collections.singletonList(this.localExtendedStatistics);
	}

	@Override
	public void controlProperty(ControllableProperty controllableProperty) throws Exception {
		this.reentrantLock.lock();
		try {
			String[] components = controllableProperty.getProperty().split(Constant.HASH);
			if (!Constant.DISPLAY_GROUP.equals(components[0])) {
				throw new InvalidArgumentException("Unsupported group %s to control".formatted(components[0]));
			}
			Display display = Display.getByName(components[1])
					.orElseThrow(() -> new InvalidArgumentException("Unsupported property %s to control".formatted(controllableProperty.getProperty())));
			Map<String, Object> body = ControlUtil.buildDisplayRequest(display, controllableProperty.getValue());
			this.doPost(EndpointConstant.DISPLAY, body);
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
	 * Loads version properties and sets initial values used to create Adapter metadata group.
	 *
	 * @param properties the properties to load and update
	 */
	private void loadProperties(Properties properties) {
		try {
			properties.load(this.getClass().getResourceAsStream("/version.properties"));
			properties.setProperty(AdapterMetadata.ADAPTER_UPTIME.getProperty(), String.valueOf(this.adapterInitializationTimestamp));
			properties.setProperty(AdapterMetadata.ACTIVE_PROPERTY_GROUPS.getProperty(), this.getDisplayPropertyGroups());
		} catch (IOException e) {
			this.logger.error(Constant.READ_PROPERTIES_FILE_FAILED, e);
		}
	}

	/**
	 * Initializes and loads required device data from the APIs.
	 *
	 * @throws Exception if authentication or data retrieval fails
	 */
	private void setupData() throws Exception {
		this.authenticate();
		this.requestStateHandler.clear();
		this.deviceInfo = this.fetchData(EndpointConstant.DEVICE_INFO, ResponseType.DEVICE_INFO);
		this.deviceCapabilities = this.fetchData(EndpointConstant.DEVICE_CAPABILITIES, ResponseType.DEVICE_CAPABILITIES);
		this.systemVersions = this.fetchData(EndpointConstant.SYSTEM_VERSIONS, ResponseType.SYSTEM_VERSIONS);
		this.networkAdapters = this.fetchData(EndpointConstant.NETWORK_ADAPTERS, ResponseType.NETWORK_ADAPTERS);
		this.deviceDisplay = this.fetchData(EndpointConstant.DISPLAY, ResponseType.DISPLAY);
		this.requestStateHandler.verifyState();
	}

	/**
	 * Returns the IntervalSetting for the given type, creating one if absent.
	 * Guarantees a non-null entry so callers don't need null checks.
	 */
	private IntervalSetting getIntervalSettingByType(RetrievalType type) {
		return this.retrievalIntervals.computeIfAbsent(type, t -> new IntervalSetting());
	}

	/**
	 * Fetches data from a given endpoint and maps the response to the specified class type in {@link ResponseType}.
	 *
	 * @param endpoint the API endpoint URL to request data from
	 * @param responseType defines how to extract and map the response into a specific class
	 * @param <T> the generic type representing the expected response object
	 * @return the mapped response object, or {@code null} if the response is empty or mapping fails
	 * @throws FailedLoginException if authentication fails while accessing the endpoint
	 * @throws ResourceNotReachableException if the target endpoint cannot be reached
	 */
	public <T> T fetchData(String endpoint, ResponseType responseType) throws FailedLoginException {
		String responseClassName = responseType.getClazz().getSimpleName();
		try {
			this.requestStateHandler.pushRequest(endpoint);
			String response = super.doGet(endpoint);
			JsonNode responseNode = responseType.extractNode(this.objectMapper.readTree(response));
			@SuppressWarnings("unchecked")
			T mappedResponse = responseType.isCollection()
					? (T) this.objectMapper.convertValue(responseNode, responseType.getTypeRef(this.objectMapper))
					: (T) this.objectMapper.treeToValue(responseNode, responseType.getClazz());
			if (Objects.isNull(mappedResponse)) {
				this.logger.warn(String.format(Constant.FETCHED_DATA_NULL_WARNING, endpoint, responseClassName));
			}
			this.requestStateHandler.resolve(endpoint);

			return mappedResponse;
		} catch (FailedLoginException | ResourceNotReachableException e) {
			throw e;
		} catch (Exception e) {
			this.requestStateHandler.push(endpoint, e);
			this.logger.error(Constant.FETCH_DATA_FAILED.formatted(endpoint, responseClassName), e);
			return null;
		}
	}
}
