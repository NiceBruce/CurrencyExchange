package com.rtfmyoumust.currencyexchange.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CreateExchangeDTO {
    String from;
    String to;
    String amount;
}
