package com.rtfmyoumust.currencyexchange.customexceptions;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return SC_NOT_FOUND;
    }
}
