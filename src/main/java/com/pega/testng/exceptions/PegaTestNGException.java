

package com.pega.testng.exceptions;

import org.testng.*;

public class PegaTestNGException extends TestNGException {
    private static final long serialVersionUID = 1L;


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
