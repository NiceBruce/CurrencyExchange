package com.rtfmyoumust.currencyexchange.mapper;

import com.rtfmyoumust.currencyexchange.dto.CurrencyResponseDto;
import com.rtfmyoumust.currencyexchange.entity.Currency;

public class CurrencyToDtoMapper implements Mapper<Currency, CurrencyResponseDto> {

    public static final CurrencyToDtoMapper INSTANCE = new CurrencyToDtoMapper();

    @Override
    public CurrencyResponseDto mapFrom(Currency currency) {
        return CurrencyResponseDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .sign(currency.getSign())
                .build();
    }

    public static CurrencyToDtoMapper getInstance() {
        return INSTANCE;
    }
}
