package com.rtfmyoumust.currencyexchange.mapper;

import com.rtfmyoumust.currencyexchange.dto.CreateCurrencyDto;
import com.rtfmyoumust.currencyexchange.entity.Currency;

public class CreateCurrencyMapper implements Mapper<CreateCurrencyDto, Currency> {

    public static final CreateCurrencyMapper INSTANCE = new CreateCurrencyMapper();

    @Override
    public Currency mapFrom(CreateCurrencyDto fromDto) {
        return Currency.builder()
                .code(fromDto.getCode())
                .name(fromDto.getName())
                .sign(fromDto.getSign())
                .build();
    }
    public static CreateCurrencyMapper getInstance() {
        return INSTANCE;
    }
}
