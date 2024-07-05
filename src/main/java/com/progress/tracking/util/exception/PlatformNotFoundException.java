package com.progress.tracking.util.exception;

public class PlatformNotFoundException extends RuntimeException {

    public PlatformNotFoundException(final String platformName) {
        super("Platform not found: " + platformName);
    }

}
