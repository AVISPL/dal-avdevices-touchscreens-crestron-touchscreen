/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents an authentication cookie container that stores all relevant information for performing authenticated HTTP requests.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
public class AuthCookie {
	private String trackId;
	private String cookie;
	private String origin;
	private String loginReferer;
	private String refreshToken;

	/**
	 * Builds and returns a set of default HTTP headers.
	 *
	 * @return a {@link HttpHeaders} object containing authentication headers.
	 */
	public final HttpHeaders getRequestHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(HttpHeaders.COOKIE, this.trackId);
		headers.set(HttpHeaders.ORIGIN, this.origin);
		headers.set(HttpHeaders.REFERER, this.loginReferer);

		return headers;
	}

	/**
	 * Builds a form-encoded request body for login requests.
	 *
	 * @param username the login username.
	 * @param password the login password.
	 * @return a {@link MultiValueMap} representing the form body for {@code application/x-www-form-urlencoded} POST requests.
	 */
	public final MultiValueMap<String, String> getFormURLEncodedBody(String username, String password) {
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("login", username);
		params.add("passwd", password);

		return params;
	}
}
