/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

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
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.SystemVersion;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.display.DeviceDisplay;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models.network.NetworkAdapters;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.ResponseType;
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
	/** Set of supported group filter for {@code displayPropertyGroups}. */
	private static final Set<String> SUPPORTED_GROUP_FILTERS = new TreeSet<>(Set.of(
			Constant.GENERAL_GROUP, Constant.CAPABILITIES_GROUP, Constant.DISPLAY_GROUP,
			Constant.NETWORK_GROUP, Constant.SYSTEM_VERSIONS_GROUP
	));

	/** Lock for thread-safe operations. */
	private final ReentrantLock reentrantLock;
	/** Object mapper used to convert JSON responses into Java objects. */
	private final ObjectMapper objectMapper;
	/** Handles request status tracking and error detection. */
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

	/** Indicates whether groups are displayed; defaults to {@link Constant#GENERAL_GROUP}. */
	private final Set<String> displayPropertyGroups;

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

		this.displayPropertyGroups = new TreeSet<>(Set.of(Constant.GENERAL_GROUP));
	}

	/**
	 * Returns a comma-separated list of property group names that are configured to be displayed.
	 *
	 * @return a comma-separated string of display property group names; may be empty if no groups are configured
	 */
	public String getDisplayPropertyGroups() {
		return String.join(Constant.COMMA_SPACE, this.displayPropertyGroups);
	}

	/**
	 * Sets the display property groups based on a comma-separated list.
	 * <p>
	 * Trims values automatically. If {@link Constant#ALL} is present, {@link #SUPPORTED_GROUP_FILTERS} are added.
	 * Invalid groups trigger a warning and only the default group applied. {@code null} or empty input is ignored.
	 * </p>
	 *`
	 * @param displayPropertyGroups comma-separated group names; may be {@code null} or empty
	 */
	public void setDisplayPropertyGroups(String displayPropertyGroups) {
		if (StringUtils.isNullOrEmpty(displayPropertyGroups, true)) {
			return;
		}
		Set<String> checkedGroups = Arrays.stream(displayPropertyGroups.split(Constant.COMMA))
				.map(String::trim).filter(p -> !p.isEmpty()).collect(Collectors.toSet());
		if (checkedGroups.contains(Constant.ALL)) {
			this.displayPropertyGroups.addAll(SUPPORTED_GROUP_FILTERS);
			return;
		}
		if (!CollectionUtils.containsAny(SUPPORTED_GROUP_FILTERS, checkedGroups)) {
			this.logger.warn(Constant.NO_VALID_DISPLAY_PROPERTY_GROUPS_WARNING.formatted(displayPropertyGroups));
		} else {
			this.displayPropertyGroups.clear();
			checkedGroups.stream().filter(SUPPORTED_GROUP_FILTERS::contains).forEach(this.displayPropertyGroups::add);
		}
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
			if (this.shouldDisplayGroup(Constant.GENERAL_GROUP)) {
				statistics.putAll(MonitoringUtil.generateProperties(
						General.values(), null, property -> MonitoringUtil.mapToGeneral(this.deviceInfo, property)
				));
			}
			statistics.putAll(MonitoringUtil.generateProperties(
					AdapterMetadata.values(), Constant.ADAPTER_METADATA_GROUP, property -> MonitoringUtil.mapToAdapterMetadata(this.versionProperties, property)
			));
			if (this.shouldDisplayGroup(Constant.CAPABILITIES_GROUP)) {
				statistics.putAll(MonitoringUtil.generateProperties(
						Capabilities.values(), Constant.CAPABILITIES_GROUP, property -> MonitoringUtil.mapToCapabilities(this.deviceCapabilities, property)
				));
			}
			if (this.shouldDisplayGroup(Constant.SYSTEM_VERSIONS_GROUP)) {
				statistics.putAll(MonitoringUtil.generateSystemVersionProperties(this.systemVersions));
			}
			if (this.shouldDisplayGroup(Constant.NETWORK_GROUP)) {
				statistics.putAll(MonitoringUtil.generateProperties(
						Network.values(), Constant.NETWORK_GROUP, property -> MonitoringUtil.mapToNetwork(this.networkAdapters, property)
				));
			}
			if (this.shouldDisplayGroup(Constant.DISPLAY_GROUP)) {
				statistics.putAll(MonitoringUtil.generateDisplayProperties(this.deviceDisplay));
			}

			List<AdvancedControllableProperty> controllableProperties = new ArrayList<>();
			if (this.shouldDisplayGroup(Constant.DISPLAY_GROUP)) {
				controllableProperties.addAll(ControlUtil.generateDisplayControllers(this.deviceDisplay));
			}
			if (CollectionUtils.isEmpty(controllableProperties)) {
				controllableProperties.add(Constant.DUMMY_CONTROLLER);
			}

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
		if (this.shouldDisplayGroup(Constant.GENERAL_GROUP)) {
			this.deviceInfo = this.fetchData(EndpointConstant.DEVICE_INFO, ResponseType.DEVICE_INFO);
		}
		if (this.shouldDisplayGroup(Constant.CAPABILITIES_GROUP)) {
			this.deviceCapabilities = this.fetchData(EndpointConstant.DEVICE_CAPABILITIES, ResponseType.DEVICE_CAPABILITIES);
		}
		if (this.shouldDisplayGroup(Constant.SYSTEM_VERSIONS_GROUP)) {
			this.systemVersions = this.fetchData(EndpointConstant.SYSTEM_VERSIONS, ResponseType.SYSTEM_VERSIONS);
		}
		if (this.shouldDisplayGroup(Constant.NETWORK_GROUP)) {
			this.networkAdapters = this.fetchData(EndpointConstant.NETWORK_ADAPTERS, ResponseType.NETWORK_ADAPTERS);
		}
		if (this.shouldDisplayGroup(Constant.DISPLAY_GROUP)) {
			this.deviceDisplay = this.fetchData(EndpointConstant.DISPLAY, ResponseType.DISPLAY);
		}
		this.requestStateHandler.verifyState();
	}

	/**
	 * Checks whether the specified property group is configured to be displayed.
	 *
	 * @param groupName the name of the property group to check
	 * @return {@code true} if the group is configured to be displayed; {@code false} otherwise
	 */
	public boolean shouldDisplayGroup(String groupName) {
		return CollectionUtils.isNotEmpty(this.displayPropertyGroups) && this.displayPropertyGroups.contains(groupName);
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
