/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.exceptions;

/**
 * Thrown when the device answers with something other than the JSON its API returns, such as an HTML page or a redirect.
 * <p>
 * The panel answers this way when it does not accept the login session, and when its web service is not working.
 * </p>
 *
 * @author Maksym Rossiitsev / Symphony Dev Team
 * @since 1.0.2
 */
public class UnexpectedResponseException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	/**
	 * Creates the exception with a message for the end user.
	 *
	 * @param message the message, without response bodies or other diagnostic details; those belong in the log
	 */
	public UnexpectedResponseException(String message) {
		super(message);
	}
}
