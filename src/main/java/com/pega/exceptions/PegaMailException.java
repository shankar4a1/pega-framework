

package com.pega.exceptions;

public class PegaMailException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String VERSION = "$Id: PegaMailException.java 162485 2015-11-10 03:24:34Z SachinVellanki $";

    public PegaMailException() {
    }

    public PegaMailException(final String message) {
        super(message);
    }

    public PegaMailException(final Throwable cause) {
        super(cause);
    }

    public PegaMailException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
