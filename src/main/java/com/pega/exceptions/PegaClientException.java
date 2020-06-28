

package com.pega.exceptions;

public class PegaClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: PegaClientException.java 140339 2015-06-16 12:56:14Z SachinVellanki $";

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
