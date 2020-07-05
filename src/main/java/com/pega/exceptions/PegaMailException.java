

package com.pega.exceptions;

public class PegaMailException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public PegaMailException() {
    }

    public PegaMailException(final String message) {
        super(message);
    }

    public PegaMailException(final Throwable cause) {
        super(cause);
    }

    public PegaMailException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
