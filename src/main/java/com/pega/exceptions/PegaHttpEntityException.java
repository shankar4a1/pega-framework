

package com.pega.exceptions;

public class PegaHttpEntityException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String VERSION = "$Id: PegaHttpEntityException.java 140339 2015-06-16 12:56:14Z SachinVellanki $";

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
