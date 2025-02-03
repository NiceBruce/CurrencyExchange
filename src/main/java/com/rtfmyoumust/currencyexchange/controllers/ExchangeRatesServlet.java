package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateRequestDto;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateResponseDto;
import com.rtfmyoumust.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "ExchangeRatesServlet", value = "/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    public static ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<ExchangeRateResponseDto> exchangeRatesDto = exchangeRateService.getAllExchangeRates();
        resp.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(resp.getWriter(), exchangeRatesDto);
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        ExchangeRateRequestDto createExcangeRateDto = ExchangeRateRequestDto.builder()
                .baseCurrencyCode(req.getParameter("baseCurrencyCode"))
                .targetCurrencyCode(req.getParameter("targetCurrencyCode"))
                .rate(req.getParameter("rate"))
                .build();

        ExchangeRateResponseDto exchangeRateDto = exchangeRateService.addExchangeRate(createExcangeRateDto);
        resp.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(resp.getWriter(), exchangeRateDto);
    }
}