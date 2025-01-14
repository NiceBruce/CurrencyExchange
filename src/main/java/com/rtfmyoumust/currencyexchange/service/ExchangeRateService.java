package com.rtfmyoumust.currencyexchange.service;

import com.rtfmyoumust.currencyexchange.customexceptions.EntityIsExists;
import com.rtfmyoumust.currencyexchange.customexceptions.EntityNotFoundException;
import com.rtfmyoumust.currencyexchange.customexceptions.DataAccessException;
import com.rtfmyoumust.currencyexchange.customexceptions.FieldNotFoundException;
import com.rtfmyoumust.currencyexchange.dao.CurrenciesDao;
import com.rtfmyoumust.currencyexchange.dao.ExchangeRateDao;
import com.rtfmyoumust.currencyexchange.dto.CreateExchangeRateDto;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateDto;
import com.rtfmyoumust.currencyexchange.entity.Currency;
import com.rtfmyoumust.currencyexchange.entity.ExchangeRate;
import com.rtfmyoumust.currencyexchange.mapper.ExchangeRateMapper;
import com.rtfmyoumust.currencyexchange.mapper.ExchangeRateToDtoMapper;
import com.rtfmyoumust.currencyexchange.utils.ExchangeRateValidator;

import java.math.BigDecimal;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ExchangeRateService {
    public static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
    private final ExchangeRateValidator exchangeRateValidator = ExchangeRateValidator.getInstance();
    private final ExchangeRateMapper exchangeRateMapper = ExchangeRateMapper.getInstance();
    private final ExchangeRateToDtoMapper exchangeRateToDtoMapper = ExchangeRateToDtoMapper.getInstance();

    public ExchangeRateDto getExchangeRate(String baseCode, String targetCode) throws DataAccessException, EntityNotFoundException {
        ExchangeRate exchangeRate = exchangeRateDao.getExchangeRateByCode(baseCode, targetCode).orElseThrow(
                () -> new EntityNotFoundException(
                String.format("Обменный курс для пары: %s не найден", baseCode + targetCode)));

        return ExchangeRateDto.builder()
                .id(exchangeRate.getId())
                .baseCurrency(exchangeRate.getBaseCurrency())
                .targetCurrency(exchangeRate.getTargetCurrency())
                .rate(exchangeRate.getRate())
                .build();
    }

    public List<ExchangeRateDto> getAllExchangeRates() throws DataAccessException {
        List<ExchangeRate> exchangeRates = exchangeRateDao.getAllExchangeRates();
        List<ExchangeRateDto> exchangeRateDtos = exchangeRates.stream()
                .map(exchangeRate -> ExchangeRateDto.builder()
                        .id(exchangeRate.getId())
                        .baseCurrency(exchangeRate.getBaseCurrency())
                        .targetCurrency(exchangeRate.getTargetCurrency())
                        .rate(exchangeRate.getRate())
                        .build())
                .collect(toList());
        return exchangeRateDtos;
    }

    public ExchangeRateDto addExchangeRate(CreateExchangeRateDto createExchangeRateDto) throws DataAccessException, EntityNotFoundException, EntityIsExists, FieldNotFoundException {
        Currency baseCurrency = currenciesDao.getCurrencyByCode(createExchangeRateDto.getBaseCurrencyCode()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Валюта с кодом: %s не существует в БД", createExchangeRateDto.getBaseCurrencyCode())));
        Currency targetCurrency = currenciesDao.getCurrencyByCode(createExchangeRateDto.getTargetCurrencyCode()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Валюта с кодом: %s не существует в БД", createExchangeRateDto.getTargetCurrencyCode())));
        if(exchangeRateDao.getExchangeRateByCode(baseCurrency.getCode(), targetCurrency.getCode()).isPresent()) {
               throw new EntityIsExists(String.format("Валютная пара с кодом: '%s' уже существует ",
                       baseCurrency.getCode() + targetCurrency.getCode()));
        }
        exchangeRateValidator.isValidExchangeRate(createExchangeRateDto.getRate());
        ExchangeRate exchangeRateEntity = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(new BigDecimal(createExchangeRateDto.getRate()))
                .build();
        ExchangeRate exchangeRate = exchangeRateDao.addExchangeRate(exchangeRateEntity);
        return exchangeRateToDtoMapper.mapFrom(exchangeRate);
    }

    public ExchangeRateDto updateExchangeRate(String baseCode, String targetCode, String newRate) throws DataAccessException, EntityNotFoundException, EntityIsExists, FieldNotFoundException {
        ExchangeRateDto existExchangeRateDto = getExchangeRate(baseCode, targetCode);
        ExchangeRate exchangeRate = exchangeRateMapper.mapFrom(existExchangeRateDto);
        exchangeRateValidator.isValidExchangeRate(newRate);
        exchangeRateDao.updateExchangeRate(exchangeRate, newRate);
        return getExchangeRate(baseCode, targetCode);
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}