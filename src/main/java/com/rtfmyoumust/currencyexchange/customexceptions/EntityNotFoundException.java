package com.rtfmyoumust.currencyexchange.customexceptions;

import static jakarta.servlet.http.HttpServletResponse.SC_NOT_FOUND;

public class EntityNotFoundException extends CustomException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    @Override
    public int getErrorCode() {
        return SC_NOT_FOUND;
    }
}
