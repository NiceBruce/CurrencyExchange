package com.rtfmyoumust.currencyexchange.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {
    private int id;
    private String code;
    private String name;
    private String sign;
}
