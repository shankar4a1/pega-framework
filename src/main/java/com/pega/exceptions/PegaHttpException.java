

package com.pega.exceptions;

public class PegaHttpException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public PegaHttpException() {
    }

    public PegaHttpException(final String message) {
        super(message);
    }

    public PegaHttpException(final Throwable cause) {
        super(cause);
    }

    public PegaHttpException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
