package eu.assina.sa.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CredentialNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CredentialNotFoundException(String message) {
        super(message);
    }

    public CredentialNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
