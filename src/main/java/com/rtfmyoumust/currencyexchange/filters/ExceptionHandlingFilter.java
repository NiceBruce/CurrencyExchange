package com.rtfmyoumust.currencyexchange.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rtfmyoumust.currencyexchange.customexceptions.CustomException;
import com.rtfmyoumust.currencyexchange.dto.ErrorResponseDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class ExceptionHandlingFilter  extends HttpFilter {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        try {
            super.doFilter(req, res, chain);
        } catch (CustomException e) {
            res.setStatus(e.getErrorCode());
            objectMapper.writeValue(res.getWriter(), ErrorResponseDto.builder()
                    .error(e.getErrorCode())
                    .message(e.getMessage())
                    .build());
        }
    }
}