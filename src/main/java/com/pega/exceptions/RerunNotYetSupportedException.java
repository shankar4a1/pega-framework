

package com.pega.exceptions;

public class RerunNotYetSupportedException extends PegaWebDriverException {
    private static final long serialVersionUID = 1L;


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
