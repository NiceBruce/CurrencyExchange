package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.dto.CurrencyRequestDto;
import com.rtfmyoumust.currencyexchange.dto.CurrencyResponseDto;
import com.rtfmyoumust.currencyexchange.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "CurrenciesServlet", value = "/currencies")
public class СurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = CurrencyService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<CurrencyResponseDto> currencies = currencyService.getCurrencies();
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), currencies);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var currenctDto = CurrencyRequestDto.builder()
                .code(request.getParameter("code"))
                .name(request.getParameter("name"))
                .sign(request.getParameter("sign"))
                .build();

        CurrencyResponseDto currencyDto = currencyService.addCurrency(currenctDto);
        response.setStatus(HttpServletResponse.SC_CREATED);
        objectMapper.writeValue(response.getWriter(), currencyDto);
    }
}