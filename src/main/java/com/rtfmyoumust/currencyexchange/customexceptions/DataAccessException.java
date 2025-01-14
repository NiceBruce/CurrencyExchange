package com.rtfmyoumust.currencyexchange.customexceptions;


import jakarta.servlet.http.HttpServletResponse;

public class DataAccessException extends CustomException {
    public DataAccessException(String message) {
        super(message);
    }

    public int getErrorCode() {
        return HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
    }
}