

package com.pega.exceptions;

public class PegaElementNotVisibleException extends PegaWebDriverException {
    private static final long serialVersionUID = 1L;


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
