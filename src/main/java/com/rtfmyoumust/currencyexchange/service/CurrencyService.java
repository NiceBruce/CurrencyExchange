package com.rtfmyoumust.currencyexchange.service;

import com.rtfmyoumust.currencyexchange.customexceptions.*;
import com.rtfmyoumust.currencyexchange.dao.CurrenciesDao;
import com.rtfmyoumust.currencyexchange.dto.CreateCurrencyDto;
import com.rtfmyoumust.currencyexchange.dto.CurrencyDto;
import com.rtfmyoumust.currencyexchange.entity.Currency;
import com.rtfmyoumust.currencyexchange.mapper.CreateCurrencyMapper;
import com.rtfmyoumust.currencyexchange.mapper.CurrencyToDtoMapper;
import com.rtfmyoumust.currencyexchange.utils.CurrencyValidator;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class CurrencyService {

    public static final CurrencyService CURRENCY_SERVICE = new CurrencyService();

    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
    private final CreateCurrencyMapper createCurrencyMapper = CreateCurrencyMapper.getInstance();
    private final CurrencyToDtoMapper currencyToDtoMapper = CurrencyToDtoMapper.getInstance();
    private final CurrencyValidator currencyValidator = CurrencyValidator.getInstance();

    public CurrencyDto getCurrencyByCode(String code) throws DataAccessException, EntityNotFoundException {
        Currency currency = currenciesDao.getCurrencyByCode(code).orElseThrow(
                () -> new EntityNotFoundException(String.format("Валюта с кодом: %s не найдена", code)));
        return CurrencyDto.builder()
                .id(currency.getId())
                .code(currency.getCode())
                .name(currency.getName())
                .sign(currency.getSign())
                .build();
    }

    public List<CurrencyDto> getCurrencies() throws DataAccessException {
        List<Currency> currencies = currenciesDao.getAllCurrencies();
        List<CurrencyDto> currencyDtos = currencies.stream()
                .map(currency -> CurrencyDto.builder()
                        .id(currency.getId())
                        .code(currency.getCode())
                        .name(currency.getName())
                        .sign(currency.getSign())
                        .build())
                .collect(toList());
        return currencyDtos;
    }


    public CurrencyDto addCurrency(CreateCurrencyDto createCurrencyDto) throws CustomException {
        CurrencyDto currencyDto = null;
        if (currencyValidator.isValidCurrency(createCurrencyDto)){
            var currencyEntity = createCurrencyMapper.mapFrom(createCurrencyDto);
            Currency currency = currenciesDao.addCurrency(currencyEntity);
            currencyDto = currencyToDtoMapper.mapFrom(currency);
        }

        return currencyDto;
    }

    public static CurrencyService getInstance() {
        return CURRENCY_SERVICE;
    }
}
