

package com.pega.exceptions;

public class PegaElementNotVisibleException extends PegaWebDriverException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: PegaElementNotVisibleException.java 121818 2015-01-26 07:18:23Z SachinVellanki $";

    public PegaElementNotVisibleException() {
    }

    public PegaElementNotVisibleException(final String message) {
        super(message);
    }

    public PegaElementNotVisibleException(final Throwable cause) {
        super(cause);
    }

    public PegaElementNotVisibleException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
