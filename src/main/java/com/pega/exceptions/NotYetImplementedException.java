

package com.pega.exceptions;

public class NotYetImplementedException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: NotYetImplementedException.java 125139 2015-02-22 15:23:22Z SachinVellanki $";

    public NotYetImplementedException() {
        super("This API is not yet implemented");
    }

    public NotYetImplementedException(final String message) {
        super(message);
    }

    public NotYetImplementedException(final Throwable cause) {
        super("This API is not yet implemented", cause);
    }

    public NotYetImplementedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
