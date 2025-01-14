package com.rtfmyoumust.currencyexchange.common;

public class ErrorResponse {
    private String message;

    public ErrorResponse(String errorMessage) {
        this.message = errorMessage;
    }

    public String getMessage() {
        return message;
    }
}
