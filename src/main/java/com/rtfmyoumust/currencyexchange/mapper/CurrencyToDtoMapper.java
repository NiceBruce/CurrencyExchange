package com.rtfmyoumust.currencyexchange.mapper;

import com.rtfmyoumust.currencyexchange.dto.CurrencyDto;
import com.rtfmyoumust.currencyexchange.entity.Currency;

public class CurrencyToDtoMapper implements Mapper<Currency, CurrencyDto> {

    public static final CurrencyToDtoMapper INSTANCE = new CurrencyToDtoMapper();

    @Override
    public CurrencyDto mapFrom(Currency currency) {
        return CurrencyDto.builder()
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
