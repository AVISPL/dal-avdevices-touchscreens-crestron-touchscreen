/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.security.auth.login.FailedLoginException;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.avispl.symphony.api.common.error.ServiceNotAvailableException;
import com.avispl.symphony.api.dal.dto.control.ControllableProperty;
import com.avispl.symphony.api.dal.dto.monitor.ExtendedStatistics;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.EndpointConstant;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.Display;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.types.properties.General;

/**
 * Tests that the adapter logs in again and retries when the panel rejects its login session (SYAL-4004),
 * against a stub panel that serves the login flow, {@link EndpointConstant#DEVICE_INFO} and control POSTs to {@link EndpointConstant#DISPLAY}.
 * <p>
 * The stub follows behaviour measured on a TSW-1070 (fw 3.003.0029): a request on an expired session gets
 * 301 to /userlogin.html with an empty body (GET and POST), and a wrong password gets 403 "Invalid Credentials".
 * </p>
 */
@Tag("Mock")
class SessionRecoveryTest {
	private static final String USER = "admin";
	private static final String PASSWORD = "secret";
	private static final String MODEL = "TSW-1070";
	private static final String LOGIN_PAGE = "<!DOCTYPE html><html><body><form action=\"/userlogin.html\"></form></body></html>";

	/** How the stub panel answers a data request whose session cookie it does not accept. */
	private enum RejectMode { REDIRECT_TO_LOGIN, UNAUTHORIZED }

	private HttpServer server;
	private CrestronTouchPanelCommunicator communicator;

	private final AtomicInteger sessionCounter = new AtomicInteger();
	private final AtomicInteger successfulLogins = new AtomicInteger();
	private final AtomicInteger appliedControls = new AtomicInteger();
	private volatile String currentSession;
	private volatile String acceptedPassword = PASSWORD;
	private volatile RejectMode rejectMode = RejectMode.REDIRECT_TO_LOGIN;
	/** When set, data requests are rejected even with a fresh session. */
	private volatile boolean rejectEverySession;

	@BeforeEach
	void setUp() throws Exception {
		this.server = HttpServer.create(new InetSocketAddress("127.0.0.1", 0), 0);
		this.server.createContext(EndpointConstant.LOGIN, this::handleLogin);
		this.server.createContext(EndpointConstant.LOGOUT, exchange -> this.respond(exchange, 200, "text/plain", "", Map.of()));
		this.server.createContext(EndpointConstant.DEVICE_INFO, this::handleDeviceInfo);
		this.server.createContext(EndpointConstant.DISPLAY, this::handleDisplay);
		this.server.start();

		this.communicator = new CrestronTouchPanelCommunicator();
		this.communicator.setProtocol("http");
		this.communicator.setHost("127.0.0.1");
		this.communicator.setPort(this.server.getAddress().getPort());
		this.communicator.setLogin(USER);
		this.communicator.setPassword(PASSWORD);
		this.communicator.setDisplayPropertyGroups(Constant.GENERAL_GROUP);
		this.communicator.init();
	}

	@AfterEach
	void tearDown() throws Exception {
		this.communicator.destroy();
		this.server.stop(0);
	}

	@Test
	void relogsInWhenExpiredSessionIsRedirectedToLoginPage() throws Exception {
		Assertions.assertEquals(MODEL, this.pollModel());

		this.rejectMode = RejectMode.REDIRECT_TO_LOGIN;
		this.currentSession = null;

		Assertions.assertEquals(MODEL, this.pollModel(), "Properties must not fall back to N/A after the session expires");
		Assertions.assertEquals(2, this.successfulLogins.get());
	}

	@Test
	void relogsInWhenExpiredSessionGetsUnauthorized() throws Exception {
		Assertions.assertEquals(MODEL, this.pollModel());

		this.rejectMode = RejectMode.UNAUTHORIZED;
		this.currentSession = null;

		Assertions.assertEquals(MODEL, this.pollModel());
		Assertions.assertEquals(2, this.successfulLogins.get());
	}

	@Test
	void failsLoginWhenPasswordIsWrong() {
		this.acceptedPassword = "changed";

		Assertions.assertThrows(FailedLoginException.class, () -> this.communicator.getMultipleStatistics());
	}

	@Test
	void failsLoginWhenPasswordChangesAfterSessionExpires() throws Exception {
		Assertions.assertEquals(MODEL, this.pollModel());

		this.acceptedPassword = "changed";
		this.currentSession = null;

		Assertions.assertThrows(FailedLoginException.class, () -> this.communicator.getMultipleStatistics());
	}

	@Test
	void reportsServiceNotAvailableWhenFreshSessionIsAlsoRedirected() throws Exception {
		Assertions.assertEquals(MODEL, this.pollModel());

		this.rejectEverySession = true;

		ServiceNotAvailableException e = Assertions.assertThrows(ServiceNotAvailableException.class, () -> this.communicator.getMultipleStatistics());
		Assertions.assertEquals(Constant.UNEXPECTED_HTML_RESPONSE.formatted(EndpointConstant.DEVICE_INFO), e.getMessage(),
				"The message is for the end user: no response body or redirect details");
		Assertions.assertEquals(2, this.successfulLogins.get(), "Must retry once, not loop");
	}

	@Test
	void failsLoginWhenFreshSessionGetsUnauthorized() throws Exception {
		Assertions.assertEquals(MODEL, this.pollModel());

		this.rejectMode = RejectMode.UNAUTHORIZED;
		this.rejectEverySession = true;

		Assertions.assertThrows(FailedLoginException.class, () -> this.communicator.getMultipleStatistics());
		Assertions.assertEquals(2, this.successfulLogins.get(), "Must retry once, not loop");
	}

	@Test
	void appliesControlAfterSessionExpires() throws Exception {
		Assertions.assertEquals(MODEL, this.pollModel());

		this.rejectMode = RejectMode.REDIRECT_TO_LOGIN;
		this.currentSession = null;

		ControllableProperty mute = new ControllableProperty();
		mute.setProperty(String.format(Constant.PROPERTY_FORMAT, Constant.DISPLAY_GROUP, Display.AUDIO_PANEL_MUTE.getName()));
		mute.setValue("1");
		this.communicator.controlProperty(mute);

		Assertions.assertEquals(1, this.appliedControls.get(), "The control must reach the panel on a valid session");
		Assertions.assertEquals(2, this.successfulLogins.get());
	}

	private String pollModel() throws Exception {
		ExtendedStatistics statistics = (ExtendedStatistics) this.communicator.getMultipleStatistics().get(0);
		return statistics.getStatistics().get(General.MODEL.getName());
	}

	private void handleLogin(HttpExchange exchange) throws IOException {
		if ("GET".equals(exchange.getRequestMethod())) {
			this.respond(exchange, 200, "text/html", LOGIN_PAGE, Map.of("Set-Cookie", List.of("TRACKID=t1; Path=/")));
			return;
		}
		String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
		Map<String, String> form = this.parseForm(body);
		if (USER.equals(form.get("login")) && this.acceptedPassword.equals(form.get("passwd"))) {
			this.currentSession = "s" + this.sessionCounter.incrementAndGet();
			this.successfulLogins.incrementAndGet();
			this.respond(exchange, 200, "text/html", "", Map.of(
					"Set-Cookie", List.of("SESSION=" + this.currentSession + "; Path=/"),
					Constant.CREST_XSRF_TOKEN_HEADER, List.of("xsrf-" + this.currentSession)
			));
		} else {
			//	Like the panel: a wrong password gets 403 with "Invalid Credentials" and no Content-Type.
			this.respond(exchange, 403, null, "Invalid Credentials", Map.of());
		}
	}

	private void handleDeviceInfo(HttpExchange exchange) throws IOException {
		if (this.isSessionAccepted(exchange)) {
			this.respond(exchange, 200, "text/plain",
					"{\"Device\":{\"DeviceInfo\":{\"Model\":\"%s\",\"SerialNumber\":\"1234\"}}}".formatted(MODEL), Map.of());
		} else {
			this.reject(exchange);
		}
	}

	private void handleDisplay(HttpExchange exchange) throws IOException {
		if (!"POST".equals(exchange.getRequestMethod())) {
			//	A followed redirect of the POST arrives here as a GET; the stub answers it like the login page.
			this.respond(exchange, 200, "text/html", LOGIN_PAGE, Map.of());
			return;
		}
		exchange.getRequestBody().readAllBytes();
		if (this.isSessionAccepted(exchange)) {
			this.appliedControls.incrementAndGet();
			this.respond(exchange, 200, "text/plain", "{\"Actions\":[{\"Results\":[{\"StatusId\":0}]}]}", Map.of());
		} else {
			this.reject(exchange);
		}
	}

	private boolean isSessionAccepted(HttpExchange exchange) {
		String cookie = exchange.getRequestHeaders().getFirst("Cookie");
		String session = this.currentSession;
		return !this.rejectEverySession && session != null && cookie != null && cookie.contains("SESSION=" + session + ";");
	}

	private void reject(HttpExchange exchange) throws IOException {
		if (this.rejectMode == RejectMode.UNAUTHORIZED) {
			this.respond(exchange, 401, "text/plain", "Unauthorized", Map.of());
		} else {
			this.respond(exchange, 301, null, "", Map.of("Location", List.of(EndpointConstant.LOGIN)));
		}
	}

	private Map<String, String> parseForm(String body) {
		java.util.HashMap<String, String> form = new java.util.HashMap<>();
		for (String pair : body.split("&")) {
			String[] parts = pair.split("=", 2);
			if (parts.length == 2) {
				form.put(URLDecoder.decode(parts[0], StandardCharsets.UTF_8), URLDecoder.decode(parts[1], StandardCharsets.UTF_8));
			}
		}
		return form;
	}

	private void respond(HttpExchange exchange, int status, String contentType, String body, Map<String, List<String>> headers) throws IOException {
		if (contentType != null) {
			exchange.getResponseHeaders().set("Content-Type", contentType);
		}
		headers.forEach((name, values) -> exchange.getResponseHeaders().put(name, values));
		byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
		exchange.sendResponseHeaders(status, bytes.length == 0 ? -1 : bytes.length);
		try (OutputStream out = exchange.getResponseBody()) {
			out.write(bytes);
		}
	}
}
