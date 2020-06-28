

package com.pega.exceptions;

public class PegaElementNotFoundException extends PegaWebDriverException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: PegaElementNotFoundException.java 121818 2015-01-26 07:18:23Z SachinVellanki $";

    public PegaElementNotFoundException() {
    }

    public PegaElementNotFoundException(final String message) {
        super(message);
    }

    public PegaElementNotFoundException(final Throwable cause) {
        super(cause);
    }

    public PegaElementNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
