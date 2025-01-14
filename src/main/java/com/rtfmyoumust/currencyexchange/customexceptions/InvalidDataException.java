package com.rtfmyoumust.currencyexchange.customexceptions;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class InvalidDataException extends CustomException{
    public InvalidDataException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return SC_BAD_REQUEST;
    }
}
