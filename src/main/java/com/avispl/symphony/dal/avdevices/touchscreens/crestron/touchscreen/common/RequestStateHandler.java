/** Copyright (c) 2025 AVI-SPL, Inc. All Rights Reserved. */
package com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.avispl.symphony.api.dal.error.ResourceNotReachableException;
import com.avispl.symphony.dal.avdevices.touchscreens.crestron.touchscreen.common.constants.Constant;

/**
 * Handler is responsible for storing and processing api errors reported by the aggregator.
 * Whenever an important part of the API fails, aggregator should call {@link #push(String, Exception)},
 * when the error is resolved - {@link #resolve(String)}
 *
 * Then, {@link #verifyState()} is called after the data processing, and if there are errors - the RuntimeException is thrown
 * with the details about the failed API sections and top error cause.
 *
 * @author Kevin/Symphony Team
 * @since 1.0.0
 */
public class RequestStateHandler {
	/** Map of api sections and corresponding instances of {@link Throwable} */
	private final Map<String, Throwable> apiErrors = new ConcurrentHashMap<>();
	private final Set<String> sentRequests = ConcurrentHashMap.newKeySet();

	public void pushRequest(String endpoint) {
		this.sentRequests.add(endpoint);
	}

	public void clear() {
		this.sentRequests.clear();
	}

	/**
	 * Handles and records API errors by either rethrowing specific exceptions or storing them in {@link #apiErrors}.
	 *
	 * @param apiSection the identifier of the API section (property group) where the error occurred
	 * @param error the exception instance to handle
	 */
	public void push(String apiSection, Exception error) {
		this.apiErrors.put(apiSection, error);
	}

	/**
	 * Remove an error from {@link #apiErrors}
	 *
	 * @param apiSection API section name to remove from {@link #apiErrors}
	 */
	public void resolve(String apiSection) {
		this.apiErrors.remove(apiSection);
	}

	/**
	 * Checks {@link #apiErrors} and throws an exception if all requests failed.
	 *
	 * @throws ResourceNotReachableException if {@link #apiErrors} is not empty and matches sent requests
	 */
	public void verifyState() {
		if (this.apiErrors.isEmpty() || this.apiErrors.size() != this.sentRequests.size()) {
			return;
		}

		String apiSections = String.join(Constant.COMMA, this.apiErrors.keySet());
		Throwable error = this.apiErrors.values().iterator().next();
		String errorText = error != null ? error.getMessage() : Constant.NOT_AVAILABLE;
		throw new ResourceNotReachableException(String.format(Constant.REQUEST_APIS_FAILED, apiSections, errorText));
	}
}