

package com.pega.testng.exceptions;

import org.testng.*;

public class PegaTestNGSkipException extends SkipException {
    private static final long serialVersionUID = 1L;


    public PegaTestNGSkipException(final String message) {
        super(message);
    }

    public PegaTestNGSkipException(final String skipMessage, final Throwable cause) {
        super(skipMessage, cause);
    }
}
