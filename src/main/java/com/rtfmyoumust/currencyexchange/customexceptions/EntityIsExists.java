package com.rtfmyoumust.currencyexchange.customexceptions;

import static jakarta.servlet.http.HttpServletResponse.SC_CONFLICT;

public class EntityIsExists extends CustomException {
    public EntityIsExists(String message) {
        super(message);
    }

    public EntityIsExists(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getErrorCode() {
        return SC_CONFLICT;
    }
}
