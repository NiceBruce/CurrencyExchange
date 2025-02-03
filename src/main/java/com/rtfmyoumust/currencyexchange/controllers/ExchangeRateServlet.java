package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.common.ErrorResponse;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateRequestDto;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateResponseDto;
import com.rtfmyoumust.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ExchangeRateServlet", value = "/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    public static ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
                doPutch(req, resp);
        } else {
            super.service(req, resp);
        }

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getPathInfo().substring(1);
        ExchangeRateResponseDto exchangeRateDto;

        if (code.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("Коды валют пары отсутствуют в адресе");
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }

        exchangeRateDto = exchangeRateService.getExchangeRate(code.substring(0, 3), code.substring(3));
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), exchangeRateDto);
    }

    public void doPutch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getPathInfo().substring(1);
        String newRate = request.getReader().readLine().split("=")[1];
        ExchangeRateRequestDto exchangeRateRequestDto = ExchangeRateRequestDto.builder()
                .baseCurrencyCode(code.substring(0, 3))
                .targetCurrencyCode(code.substring(3))
                .rate(newRate)
                .build();
        ExchangeRateResponseDto updatedExchangeRateDto = exchangeRateService.updateExchangeRate(exchangeRateRequestDto);
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), updatedExchangeRateDto);
    }
}