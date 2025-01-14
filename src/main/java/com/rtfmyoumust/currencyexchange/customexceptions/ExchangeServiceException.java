package com.rtfmyoumust.currencyexchange.customexceptions;

public class ExchangeServiceException extends CustomException {

    public ExchangeServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExchangeServiceException(String message) { super(message); }

    @Override
    public int getErrorCode() {
        return 0;
    }
}
