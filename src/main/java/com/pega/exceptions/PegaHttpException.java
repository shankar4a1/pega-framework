

package com.pega.exceptions;

public class PegaHttpException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: PegaHttpException.java 142258 2015-06-30 11:44:42Z SachinVellanki $";

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
