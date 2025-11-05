package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.models;

import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Represents an authentication cookie container that stores all relevant information for performing authenticated HTTP requests.
 *
 * @author Kevin / Symphony Dev Team
 * @since 1.0.0
 */
public class AuthCookie {
	private String trackId;
	private String cookie;
	private String origin;
	private String loginReferer;
	private String refreshToken;

	/**
	 * Retrieves {@link #trackId}
	 *
	 * @return value of {@link #trackId}
	 */
	public String getTrackId() {
		return trackId;
	}

	/**
	 * Sets {@link #trackId} value
	 *
	 * @param trackId new value of {@link #trackId}
	 */
	public void setTrackId(String trackId) {
		this.trackId = trackId;
	}

	/**
	 * Retrieves {@link #cookie}
	 *
	 * @return value of {@link #cookie}
	 */
	public String getCookie() {
		return cookie;
	}

	/**
	 * Sets {@link #cookie} value
	 *
	 * @param cookie new value of {@link #cookie}
	 */
	public void setCookie(String cookie) {
		this.cookie = cookie;
	}

	/**
	 * Retrieves {@link #origin}
	 *
	 * @return value of {@link #origin}
	 */
	public String getOrigin() {
		return origin;
	}

	/**
	 * Sets {@link #origin} value
	 *
	 * @param origin new value of {@link #origin}
	 */
	public void setOrigin(String origin) {
		this.origin = origin;
	}

	/**
	 * Retrieves {@link #loginReferer}
	 *
	 * @return value of {@link #loginReferer}
	 */
	public String getLoginReferer() {
		return loginReferer;
	}

	/**
	 * Sets {@link #loginReferer} value
	 *
	 * @param loginReferer new value of {@link #loginReferer}
	 */
	public void setLoginReferer(String loginReferer) {
		this.loginReferer = loginReferer;
	}

	/**
	 * Retrieves {@link #refreshToken}
	 *
	 * @return value of {@link #refreshToken}
	 */
	public String getRefreshToken() {
		return refreshToken;
	}

	/**
	 * Sets {@link #refreshToken} value
	 *
	 * @param refreshToken new value of {@link #refreshToken}
	 */
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

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
