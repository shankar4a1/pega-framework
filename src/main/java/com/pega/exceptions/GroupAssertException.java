

package com.pega.exceptions;

public class GroupAssertException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public GroupAssertException() {
    }

    public GroupAssertException(final String message) {
        super(message);
    }

    public GroupAssertException(final Throwable cause) {
        super(cause);
    }

    public GroupAssertException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
