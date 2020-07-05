

package com.pega.exceptions;

public class SubUIAssertException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public SubUIAssertException() {
    }

    public SubUIAssertException(final String message) {
        super(message);
    }

    public SubUIAssertException(final Throwable cause) {
        super(cause);
    }

    public SubUIAssertException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
