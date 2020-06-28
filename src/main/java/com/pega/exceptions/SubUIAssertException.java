

package com.pega.exceptions;

public class SubUIAssertException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: SubUIAssertException.java 208110 2016-08-25 09:21:14Z BalanaveenreddyKappeta $";

    public SubUIAssertException() {
    }

    public SubUIAssertException(final String message) {
        super(message);
    }

    public SubUIAssertException(final Throwable cause) {
        super(cause);
    }

    public SubUIAssertException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
