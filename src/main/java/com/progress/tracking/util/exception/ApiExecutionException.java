package com.progress.tracking.util.exception;

public class ApiExecutionException extends Exception {

    public ApiExecutionException(String message) {
        super(message);
    }

    public ApiExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
