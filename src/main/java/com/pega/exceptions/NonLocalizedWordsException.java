

package com.pega.exceptions;

public class NonLocalizedWordsException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private static final String COPYRIGHT = "Copyright (c) 2014  Pegasystems Inc.";
    private static final String VERSION = "$Id: NonLocalizedWordsException.java 198173 2016-06-16 05:13:57Z PavanBeri $";

    public NonLocalizedWordsException() {
    }

    public NonLocalizedWordsException(final String message) {
        super(message);
    }

    public NonLocalizedWordsException(final Throwable cause) {
        super(cause);
    }

    public NonLocalizedWordsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
