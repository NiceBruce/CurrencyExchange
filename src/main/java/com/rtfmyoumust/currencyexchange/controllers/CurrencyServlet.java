package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.common.ErrorResponse;
import com.rtfmyoumust.currencyexchange.customexceptions.CustomException;
import com.rtfmyoumust.currencyexchange.dto.CurrencyDto;
import com.rtfmyoumust.currencyexchange.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CurrencyServlet", value = "/currency_exchange_war_exploded/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET");
        String code = request.getPathInfo().substring(1);
        CurrencyDto currencyDTO;

        if (code.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("Код валюты отсутствует в адресе");
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }

        try {
            currencyDTO = currencyService.getCurrencyByCode(code);
            objectMapper.writeValue(response.getWriter(), currencyDTO);
        } catch (CustomException e) {
            response.setStatus(e.getErrorCode());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}
