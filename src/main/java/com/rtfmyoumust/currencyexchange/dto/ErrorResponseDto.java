package com.rtfmyoumust.currencyexchange.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ErrorResponseDto {
    int error;
    String message;
}
