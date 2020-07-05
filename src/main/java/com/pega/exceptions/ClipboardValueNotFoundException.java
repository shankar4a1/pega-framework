

package com.pega.exceptions;

public class ClipboardValueNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;


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
