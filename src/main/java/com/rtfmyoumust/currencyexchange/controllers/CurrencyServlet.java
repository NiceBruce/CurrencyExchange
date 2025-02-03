package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.customexceptions.FieldNotFoundException;
import com.rtfmyoumust.currencyexchange.dto.CurrencyResponseDto;
import com.rtfmyoumust.currencyexchange.service.CurrencyService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "CurrencyServlet", value = "/currency/*")
public class CurrencyServlet extends HttpServlet {

    private final CurrencyService currencyService = CurrencyService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getPathInfo().substring(1);
        if (code.isEmpty()){
            throw new FieldNotFoundException("Код валюты отсутствует в адресе");
        }
        CurrencyResponseDto currencyDTO = currencyService.getCurrencyByCode(code);
        objectMapper.writeValue(response.getWriter(), currencyDTO);
    }
}