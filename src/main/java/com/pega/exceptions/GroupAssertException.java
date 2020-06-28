

package com.pega.exceptions;

public class GroupAssertException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: GroupAssertException.java 194902 2016-05-27 11:25:38Z ShakkariSakethkumar $";

    public GroupAssertException() {
    }

    public GroupAssertException(final String message) {
        super(message);
    }

    public GroupAssertException(final Throwable cause) {
        super(cause);
    }

    public GroupAssertException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
