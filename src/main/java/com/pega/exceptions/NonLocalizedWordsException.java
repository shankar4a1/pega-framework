

package com.pega.exceptions;

public class NonLocalizedWordsException extends RuntimeException {
    private static final long serialVersionUID = 1L;


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
