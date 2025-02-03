package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRequestDto;
import com.rtfmyoumust.currencyexchange.dto.ExchangeResponseDto;
import com.rtfmyoumust.currencyexchange.service.ExchangeService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ExchangeServlet", value = "/exchange")
public class ExchangeServlet extends HttpServlet {
    private final ExchangeService exchangeService = ExchangeService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");

        ExchangeRequestDto createExchangeDTO = ExchangeRequestDto.builder()
                .from(request.getParameter("from"))
                .to(request.getParameter("to"))
                .amount(request.getParameter("amount"))
                .build();

        ExchangeResponseDto exchangeDto = exchangeService.getExchange(createExchangeDTO);
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), exchangeDto);
    }
}