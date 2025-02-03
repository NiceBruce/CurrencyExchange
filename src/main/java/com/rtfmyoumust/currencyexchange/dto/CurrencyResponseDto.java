package com.rtfmyoumust.currencyexchange.dto;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class CurrencyResponseDto {
    int id;
    String code;
    String name;
    String sign;
}