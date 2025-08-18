package joserodpt.reallogin.common.exception;

public class AuthenticationRequestException extends RuntimeException {

    public AuthenticationRequestException() {
    }

    public AuthenticationRequestException(final String message) {
        super(message);
    }

    public AuthenticationRequestException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public AuthenticationRequestException(final Throwable cause) {
        super(cause);
    }
}
