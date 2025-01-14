package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.common.ErrorResponse;
import com.rtfmyoumust.currencyexchange.customexceptions.CustomException;
import com.rtfmyoumust.currencyexchange.dto.CreateExchangeDTO;
import com.rtfmyoumust.currencyexchange.dto.ExchangeDto;
import com.rtfmyoumust.currencyexchange.service.ExchangeService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ExchangeServlet", value = "/currency_exchange_war_exploded/exchange/*")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService = ExchangeService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
        CreateExchangeDTO createExchangeDTO = CreateExchangeDTO.builder()
                .from(request.getParameter("from"))
                .to(request.getParameter("to"))
                .amount(request.getParameter("amount"))
                .build();

        try {
            ExchangeDto exchangeDto = exchangeService.getExchange(createExchangeDTO);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), exchangeDto);
        } catch (CustomException e) {
            response.setStatus(e.getErrorCode());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}