package eu.assina.sa.error;

import eu.assina.common.ApiError;
import eu.assina.common.ApiErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Logs errors that occur in the Signing App
 */
@ControllerAdvice
public class ClientExceptionHandler
		extends ResponseEntityExceptionHandler
{
	private static final Logger log = LoggerFactory.getLogger(ClientExceptionHandler.class);

	@ExceptionHandler(value = {RSSPClientException.class})
	protected ResponseEntity<?> handleClientException(RuntimeException ae, WebRequest request) {
		ApiError apiError = ((RSSPClientException)ae);
		String message = ae.getMessage();
		return apiErrorToResponse(apiError, message, ae, request);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("Unhandled exception in Assina Signing Application", ex);
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private ResponseEntity<Object> apiErrorToResponse(
			ApiError error, String message, Exception ex, WebRequest request) {
		ApiErrorResponse response = new ApiErrorResponse(error.getCode(), error.getDescription());
		HttpStatus httpStatus = HttpStatus.valueOf(error.getHttpCode());
		// log a warning for all messages
		log.warn("Responding to error: {} with status {}. Error description {}; message: {}",
				error.getCode(), error.getHttpCode(), error.getDescription(), message);

		return handleExceptionInternal(ex, response, new HttpHeaders(), httpStatus, request);
	}
}
