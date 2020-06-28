

package com.pega.exceptions;

public class RerunNotYetSupportedException extends PegaWebDriverException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: RerunNotYetSupportedException.java 125139 2015-02-22 15:23:22Z SachinVellanki $";

    public RerunNotYetSupportedException() {
        super("Have you run it on fresh instace?");
    }

    public RerunNotYetSupportedException(final String message) {
        super(message);
    }

    public RerunNotYetSupportedException(final Throwable cause) {
        super("Have you run it on fresh instace?", cause);
    }

    public RerunNotYetSupportedException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
