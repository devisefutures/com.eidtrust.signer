package eu.assina.sa.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class RSSPRequestFailedException extends RuntimeException {

	public RSSPRequestFailedException(String message) {
        super(message);
    }

    public RSSPRequestFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
