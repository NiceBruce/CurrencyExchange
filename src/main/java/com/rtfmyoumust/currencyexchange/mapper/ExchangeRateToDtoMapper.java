package com.rtfmyoumust.currencyexchange.mapper;

import com.rtfmyoumust.currencyexchange.dto.ExchangeRateResponseDto;
import com.rtfmyoumust.currencyexchange.entity.ExchangeRate;

public class ExchangeRateToDtoMapper implements Mapper<ExchangeRate, ExchangeRateResponseDto> {
    public static final ExchangeRateToDtoMapper INSTANCE = new ExchangeRateToDtoMapper();
    private final CurrencyToDtoMapper currencyToDtoMapper = CurrencyToDtoMapper.getInstance();

    @Override
    public ExchangeRateResponseDto mapFrom(ExchangeRate exchangeRate) {
        return ExchangeRateResponseDto.builder()
                .id(exchangeRate.getId())
                .baseCurrency(currencyToDtoMapper.mapFrom(exchangeRate.getBaseCurrency()))
                .targetCurrency(currencyToDtoMapper.mapFrom(exchangeRate.getTargetCurrency()))
                .rate(exchangeRate.getRate())
                .build();
    }

    public static ExchangeRateToDtoMapper getInstance() {
        return INSTANCE;
    }
}
