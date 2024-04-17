package com.progress.tracking.util.exception;

public class WrapperExecutionException extends RuntimeException {

    public WrapperExecutionException(String message) {
        super(message);
    }

    public WrapperExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
