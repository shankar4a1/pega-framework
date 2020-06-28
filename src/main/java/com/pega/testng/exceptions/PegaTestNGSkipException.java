

package com.pega.testng.exceptions;

import org.testng.*;

public class PegaTestNGSkipException extends SkipException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: PegaTestNGSkipException.java 121818 2015-01-26 07:18:23Z SachinVellanki $";

    public PegaTestNGSkipException(final String message) {
        super(message);
    }

    public PegaTestNGSkipException(final String skipMessage, final Throwable cause) {
        super(skipMessage, cause);
    }
}
