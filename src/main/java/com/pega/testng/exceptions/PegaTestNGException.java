

package com.pega.testng.exceptions;

import org.testng.*;

public class PegaTestNGException extends TestNGException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: PegaTestNGException.java 121818 2015-01-26 07:18:23Z SachinVellanki $";

    public PegaTestNGException(final String message) {
        super(message);
    }

    public PegaTestNGException(final Throwable cause) {
        super(cause);
    }

    public PegaTestNGException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
