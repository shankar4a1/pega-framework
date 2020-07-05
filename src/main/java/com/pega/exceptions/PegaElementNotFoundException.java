

package com.pega.exceptions;

public class PegaElementNotFoundException extends PegaWebDriverException {
    private static final long serialVersionUID = 1L;


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
