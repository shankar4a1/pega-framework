

package com.pega.exceptions;

import org.openqa.selenium.*;

public class PegaWebDriverException extends WebDriverException {
    private static final long serialVersionUID = 1L;


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
