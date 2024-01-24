package com.progress.tracking.util.exception;

public class InvalidParameterException extends Exception {
    private static final String FIELD_NAME_TAG = "<FIELD_NAME>";
    private static final String MESSAGE = "The field '" + FIELD_NAME_TAG + "' is missing or invalid.";

    public InvalidParameterException(String fieldName) {
        this(fieldName, "");
    }

    public InvalidParameterException(String fieldName, String additionalMessage) {
        super(createMessage(fieldName, additionalMessage));
    }

    private static String createMessage(final String fieldName, final String additionalMessage) {
        final StringBuilder sb = new StringBuilder(MESSAGE.replace(FIELD_NAME_TAG, fieldName));

        if (additionalMessage != null && !additionalMessage.isBlank())
            sb.append(" ").append(additionalMessage);

        return sb.toString();
    }

}
