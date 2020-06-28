

package com.pega.exceptions;

import org.openqa.selenium.*;

public class PegaWebDriverException extends WebDriverException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: PegaWebDriverException.java 121818 2015-01-26 07:18:23Z SachinVellanki $";

    public PegaWebDriverException() {
    }

    public PegaWebDriverException(final String message) {
        super(message);
    }

    public PegaWebDriverException(final Throwable cause) {
        super(cause);
    }

    public PegaWebDriverException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
