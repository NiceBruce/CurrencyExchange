package com.rtfmyoumust.currencyexchange.service;

import com.rtfmyoumust.currencyexchange.customexceptions.EntityNotFoundException;
import com.rtfmyoumust.currencyexchange.dao.CurrenciesDao;
import com.rtfmyoumust.currencyexchange.dao.ExchangeRateDao;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateRequestDto;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRateResponseDto;
import com.rtfmyoumust.currencyexchange.entity.Currency;
import com.rtfmyoumust.currencyexchange.entity.ExchangeRate;
import com.rtfmyoumust.currencyexchange.mapper.ExchangeRateToDtoMapper;

import java.math.BigDecimal;
import java.util.List;

import static com.rtfmyoumust.currencyexchange.utils.CurrencyExhangeValidator.validateExchangeRateCode;
import static com.rtfmyoumust.currencyexchange.utils.CurrencyExhangeValidator.validateRate;
import static java.util.stream.Collectors.toList;

public class ExchangeRateService {
    public static final ExchangeRateService INSTANCE = new ExchangeRateService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
    private final ExchangeRateToDtoMapper exchangeRateToDtoMapper = ExchangeRateToDtoMapper.getInstance();

    public ExchangeRateResponseDto getExchangeRate(String baseCode, String targetCode) {
        ExchangeRate exchangeRate = exchangeRateDao.getExchangeRateByCode(baseCode, targetCode).orElseThrow(
                () -> new EntityNotFoundException(
                String.format("Обменный курс для пары: %s не найден", baseCode + targetCode)));

        return exchangeRateToDtoMapper.mapFrom(exchangeRate);
    }

    public List<ExchangeRateResponseDto> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = exchangeRateDao.getAllExchangeRates();
        List<ExchangeRateResponseDto> exchangeRateDtos = exchangeRates.stream()
                .map(exchangeRate -> exchangeRateToDtoMapper.mapFrom(exchangeRate))
                .collect(toList());
        return exchangeRateDtos;
    }

    public ExchangeRateResponseDto addExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        Currency baseCurrency = currenciesDao.getCurrencyByCode(exchangeRateRequestDto.getBaseCurrencyCode()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Валюта с кодом: %s не существует в БД", exchangeRateRequestDto.getBaseCurrencyCode())));
        Currency targetCurrency = currenciesDao.getCurrencyByCode(exchangeRateRequestDto.getTargetCurrencyCode()).orElseThrow(
                () -> new EntityNotFoundException(String.format("Валюта с кодом: %s не существует в БД", exchangeRateRequestDto.getTargetCurrencyCode())));
        validateExchangeRateCode(baseCurrency.getCode(), targetCurrency.getCode());
        validateRate(exchangeRateRequestDto.getRate());

        ExchangeRate exchangeRateEntity = ExchangeRate.builder()
                .baseCurrency(baseCurrency)
                .targetCurrency(targetCurrency)
                .rate(new BigDecimal(exchangeRateRequestDto.getRate()))
                .build();

        ExchangeRate exchangeRate = exchangeRateDao.addExchangeRate(exchangeRateEntity);
        return exchangeRateToDtoMapper.mapFrom(exchangeRate);
    }

    public ExchangeRateResponseDto updateExchangeRate(ExchangeRateRequestDto exchangeRateRequestDto) {
        ExchangeRate exchangeRate = exchangeRateDao.getExchangeRateByCode(
                        exchangeRateRequestDto.getBaseCurrencyCode(), exchangeRateRequestDto.getTargetCurrencyCode())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("Обменный курс для пары: %s не найден",
                                exchangeRateRequestDto.getBaseCurrencyCode() + exchangeRateRequestDto.getTargetCurrencyCode())));

        validateRate(exchangeRateRequestDto.getRate());
        exchangeRate.setRate(new BigDecimal(exchangeRateRequestDto.getRate()));
        return exchangeRateToDtoMapper.mapFrom(exchangeRateDao.updateExchangeRate(exchangeRate));
    }

    public static ExchangeRateService getInstance() {
        return INSTANCE;
    }
}