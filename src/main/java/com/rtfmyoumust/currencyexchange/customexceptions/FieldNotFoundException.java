package com.rtfmyoumust.currencyexchange.customexceptions;

import static jakarta.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

public class FieldNotFoundException extends CustomException {
    public FieldNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return SC_BAD_REQUEST;
    }
}
