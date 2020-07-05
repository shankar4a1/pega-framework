

package com.pega.exceptions;

public class PegaClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public PegaClientException() {
    }

    public PegaClientException(final String message) {
        super(message);
    }

    public PegaClientException(final Throwable cause) {
        super(cause);
    }

    public PegaClientException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
