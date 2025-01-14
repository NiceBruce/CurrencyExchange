package com.rtfmyoumust.currencyexchange.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.common.ErrorResponse;
import com.rtfmyoumust.currencyexchange.customexceptions.CustomException;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateDto;
import com.rtfmyoumust.currencyexchange.service.ExchangeRateService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "ExchangeRateServlet", value = "/currency_exchange_war_exploded/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    public static ExchangeRateService exchangeRateService = ExchangeRateService.getInstance();
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if (method.equals("PATCH")) {
            doPutch(req, resp);
        }
        super.service(req, resp);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
        String code = request.getPathInfo().substring(1);
        ExchangeRateDto exchangeRateDto;

        if (code.isEmpty()){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            ErrorResponse errorResponse = new ErrorResponse("Коды валют пары отсутствуют в адресе");
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }

        try {
            exchangeRateDto = exchangeRateService.getExchangeRate(code.substring(0, 3), code.substring(3));
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), exchangeRateDto);
        } catch (CustomException e) {
            response.setStatus(e.getErrorCode());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }

    public void doPutch(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, OPTIONS");
        String code = request.getPathInfo().substring(1);
        String rate = request.getReader().readLine().split("=")[1];
        ExchangeRateDto updatedExchangeRateDto;

        try {
            updatedExchangeRateDto = exchangeRateService.updateExchangeRate(code.substring(0, 3),
                    code.substring(3), rate);
            response.setStatus(HttpServletResponse.SC_OK);
            objectMapper.writeValue(response.getWriter(), updatedExchangeRateDto);
        } catch (CustomException e) {
            response.setStatus(e.getErrorCode());
            ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
            objectMapper.writeValue(response.getWriter(), errorResponse);
        }
    }
}