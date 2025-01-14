package com.rtfmyoumust.currencyexchange.mapper;

import com.rtfmyoumust.currencyexchange.dto.ExchangeRateDto;
import com.rtfmyoumust.currencyexchange.entity.ExchangeRate;

public class ExchangeRateMapper implements Mapper<ExchangeRateDto, ExchangeRate>{
    public static final ExchangeRateMapper INSTANCE = new ExchangeRateMapper();

    @Override
    public ExchangeRate mapFrom(ExchangeRateDto excangeRateDto) {
        return ExchangeRate.builder()
                .id(excangeRateDto.getId())
                .baseCurrency(excangeRateDto.getBaseCurrency())
                .targetCurrency(excangeRateDto.getTargetCurrency())
                .rate(excangeRateDto.getRate())
                .build();
    }

    public static ExchangeRateMapper getInstance() {
        return INSTANCE;
    }
}