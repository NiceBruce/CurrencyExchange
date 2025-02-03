package com.rtfmyoumust.currencyexchange.dto;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRateResponseDto {
    int id;
    CurrencyResponseDto baseCurrency;
    CurrencyResponseDto targetCurrency;
    BigDecimal rate;
}
