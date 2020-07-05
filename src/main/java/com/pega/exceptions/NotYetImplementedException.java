

package com.pega.exceptions;

public class NotYetImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public NotYetImplementedException() {
        super("This API is not yet implemented");
    }

    public NotYetImplementedException(final String message) {
        super(message);
    }

    public NotYetImplementedException(final Throwable cause) {
        super("This API is not yet implemented", cause);
    }

    public NotYetImplementedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
