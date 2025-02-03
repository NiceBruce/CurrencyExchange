package com.rtfmyoumust.currencyexchange.service;

import com.rtfmyoumust.currencyexchange.customexceptions.*;
import com.rtfmyoumust.currencyexchange.dao.CurrenciesDao;
import com.rtfmyoumust.currencyexchange.dto.CurrencyRequestDto;
import com.rtfmyoumust.currencyexchange.dto.CurrencyResponseDto;
import com.rtfmyoumust.currencyexchange.entity.Currency;
import com.rtfmyoumust.currencyexchange.mapper.CurrencyMapper;
import com.rtfmyoumust.currencyexchange.mapper.CurrencyToDtoMapper;

import java.util.List;

import static com.rtfmyoumust.currencyexchange.utils.CurrencyExhangeValidator.validateCodeCurrency;
import static com.rtfmyoumust.currencyexchange.utils.CurrencyExhangeValidator.validateCurrency;
import static java.util.stream.Collectors.toList;

public class CurrencyService {

    public static final CurrencyService CURRENCY_SERVICE = new CurrencyService();

    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
    private final CurrencyMapper createCurrencyMapper = CurrencyMapper.getInstance();
    private final CurrencyToDtoMapper currencyToDtoMapper = CurrencyToDtoMapper.getInstance();

    public static CurrencyService getInstance() {
        return CURRENCY_SERVICE;
    }

    public CurrencyResponseDto getCurrencyByCode(String code) throws DataAccessException, EntityNotFoundException {
        validateCodeCurrency(code);
        Currency currency = currenciesDao.getCurrencyByCode(code).orElseThrow(
                () -> new EntityNotFoundException(String.format("Валюта с кодом: %s не найдена", code)));
        return currencyToDtoMapper.mapFrom(currency);
    }

    public List<CurrencyResponseDto> getCurrencies() throws DataAccessException {
        List<Currency> currencies = currenciesDao.getAllCurrencies();
        return currencies.stream()
                .map(currency -> currencyToDtoMapper.mapFrom(currency))
                .collect(toList());
    }

    public CurrencyResponseDto addCurrency(CurrencyRequestDto requestCurrencyDto) throws CustomException {
        validateCurrency(requestCurrencyDto);
        var currencyEntity = createCurrencyMapper.mapFrom(requestCurrencyDto);
        Currency currency = currenciesDao.addCurrency(currencyEntity);
        return currencyToDtoMapper.mapFrom(currency);
    }
}