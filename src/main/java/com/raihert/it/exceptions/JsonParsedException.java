package com.raihert.it.exceptions;

public class JsonParsedException extends RuntimeException {
    public JsonParsedException(final String message) {
        super(message);
    }

    public JsonParsedException(final Throwable cause) {
        super(cause);
    }
}
