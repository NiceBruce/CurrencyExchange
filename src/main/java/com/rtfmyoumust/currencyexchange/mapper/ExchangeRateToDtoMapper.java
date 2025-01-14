package com.rtfmyoumust.currencyexchange.mapper;

import com.rtfmyoumust.currencyexchange.dto.ExchangeRateDto;
import com.rtfmyoumust.currencyexchange.entity.ExchangeRate;

public class ExchangeRateToDtoMapper implements Mapper<ExchangeRate, ExchangeRateDto> {
    public static final ExchangeRateToDtoMapper INSTANCE = new ExchangeRateToDtoMapper();

    @Override
    public ExchangeRateDto mapFrom(ExchangeRate exchangeRate) {
        return ExchangeRateDto.builder()
                .id(exchangeRate.getId())
                .baseCurrency(exchangeRate.getBaseCurrency())
                .targetCurrency(exchangeRate.getTargetCurrency())
                .rate(exchangeRate.getRate())
                .build();
    }

    public static ExchangeRateToDtoMapper getInstance() {
        return INSTANCE;
    }
}
