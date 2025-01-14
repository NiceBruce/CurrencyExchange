package com.rtfmyoumust.currencyexchange.mapper;

public interface Mapper<F, T>{
    T mapFrom(F from);
}