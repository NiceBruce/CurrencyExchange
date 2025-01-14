package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.common.ErrorResponse;
import com.rtfmyoumust.currencyexchange.customexceptions.CustomException;
import com.rtfmyoumust.currencyexchange.dto.CreateExchangeRateDto;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateDto;
import com.rtfmyoumust.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ExchangeRatesServlet", value = "/currency_exchange_war_exploded/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    public static ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json; charset=UTF-8");
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
        List<ExchangeRateDto> exchangeRateDtos;
        try {
            exchangeRateDtos = exchangeRateService.getAllExchangeRates();
            resp.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(resp.getWriter(), exchangeRateDtos);
        } catch (CustomException e) {
            resp.setStatus(e.getErrorCode());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), errorResponse);
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
        CreateExchangeRateDto createExcangeRateDto = CreateExchangeRateDto.builder()
                .baseCurrencyCode(req.getParameter("baseCurrencyCode"))
                .targetCurrencyCode(req.getParameter("targetCurrencyCode"))
                .rate(req.getParameter("rate"))
                .build();

        try {
            ExchangeRateDto exchangeRateDto = exchangeRateService.addExchangeRate(createExcangeRateDto);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            objectMapper.writeValue(resp.getWriter(), exchangeRateDto);
        } catch (CustomException e) {
            resp.setStatus(e.getErrorCode());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            objectMapper.writeValue(resp.getWriter(), errorResponse);
        }
    }
}
