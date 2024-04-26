package eu.assina.rssp.common.error;

import eu.assina.common.ApiError;

public class ApiException extends RuntimeException {
	private ApiError apiError;
	private String[] messageParams;

	public ApiException(ApiError apiError) {
		super(apiError.getDescription());
		this.apiError = apiError;
	}

	public ApiException(ApiError apiError, String message, String... args) {
		super(message);
		this.apiError = apiError;
		this.messageParams = args;
	}

	public ApiException(ApiError apiError, String message, Exception cause) {
		super(message, cause);
		this.apiError = apiError;
	}

	public ApiException(ApiError apiError, Exception cause) {
		super(apiError.getDescription(), cause);
		this.apiError = apiError;
	}

	public ApiError getApiError() {
		return apiError;
	}

	public String[] getMessageParams() {
		return messageParams;
	}
}
