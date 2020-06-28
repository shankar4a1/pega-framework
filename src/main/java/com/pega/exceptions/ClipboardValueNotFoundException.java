

package com.pega.exceptions;

public class ClipboardValueNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: ClipboardValueNotFoundException.java 208077 2016-08-24 10:36:26Z BalanaveenreddyKappeta $";

    public ClipboardValueNotFoundException() {
    }

    public ClipboardValueNotFoundException(final String message) {
        super(message);
    }

    public ClipboardValueNotFoundException(final Throwable cause) {
        super(cause);
    }

    public ClipboardValueNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
