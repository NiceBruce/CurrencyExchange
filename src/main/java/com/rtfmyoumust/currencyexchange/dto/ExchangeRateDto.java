package com.rtfmyoumust.currencyexchange.dto;

import com.rtfmyoumust.currencyexchange.entity.Currency;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class ExchangeRateDto {
    int id;
    Currency baseCurrency;
    Currency targetCurrency;
    BigDecimal rate;
}
