

package com.pega.exceptions;

public class PegaHttpEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public PegaHttpEntityException() {
    }

    public PegaHttpEntityException(final String message) {
        super(message);
    }

    public PegaHttpEntityException(final Throwable cause) {
        super(cause);
    }

    public PegaHttpEntityException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
