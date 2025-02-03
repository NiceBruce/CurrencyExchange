package com.rtfmyoumust.currencyexchange.customexceptions;

public abstract class CustomException extends RuntimeException {

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getErrorCode();
}