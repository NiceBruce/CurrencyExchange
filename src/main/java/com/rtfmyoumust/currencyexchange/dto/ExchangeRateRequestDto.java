package com.rtfmyoumust.currencyexchange.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExchangeRateRequestDto {
    String baseCurrencyCode;
    String targetCurrencyCode;
    String rate;
}
