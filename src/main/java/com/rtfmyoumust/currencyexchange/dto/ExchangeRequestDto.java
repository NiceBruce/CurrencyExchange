package com.rtfmyoumust.currencyexchange.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ExchangeRequestDto {
    String from;
    String to;
    String amount;
}
