package com.rtfmyoumust.currencyexchange.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateCurrencyDto {
    String code;
    String name;
    String sign;
}
