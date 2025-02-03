package com.rtfmyoumust.currencyexchange.mapper;

import com.rtfmyoumust.currencyexchange.dto.CurrencyRequestDto;
import com.rtfmyoumust.currencyexchange.entity.Currency;

public class CurrencyMapper implements Mapper<CurrencyRequestDto, Currency> {

    public static final CurrencyMapper INSTANCE = new CurrencyMapper();

    @Override
    public Currency mapFrom(CurrencyRequestDto fromDto) {
        return Currency.builder()
                .code(fromDto.getCode())
                .name(fromDto.getName())
                .sign(fromDto.getSign())
                .build();
    }
    public static CurrencyMapper getInstance() {
        return INSTANCE;
    }
}
