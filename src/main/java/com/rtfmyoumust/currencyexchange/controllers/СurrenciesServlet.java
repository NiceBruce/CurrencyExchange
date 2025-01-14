package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.common.ErrorResponse;
import com.rtfmyoumust.currencyexchange.customexceptions.*;
import com.rtfmyoumust.currencyexchange.dto.CreateCurrencyDto;
import com.rtfmyoumust.currencyexchange.dto.CurrencyDto;
import com.rtfmyoumust.currencyexchange.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CurrenciesServlet", value = "/currency_exchange_war_exploded/currencies")
public class СurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
        List<CurrencyDto> currencies;
        try {
            currencies = currencyService.getCurrencies();
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), currencies);
        } catch (CustomException e) {
            response.setStatus(e.getErrorCode());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
        var currenctDto = CreateCurrencyDto.builder()
                .code(request.getParameter("code"))
                .name(request.getParameter("name"))
                .sign(request.getParameter("sign"))
                .build();

        try {
            CurrencyDto currencyDto = currencyService.addCurrency(currenctDto);
            response.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(response.getWriter(), currencyDto);
        } catch (CustomException e) {
            response.setStatus(e.getErrorCode());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}