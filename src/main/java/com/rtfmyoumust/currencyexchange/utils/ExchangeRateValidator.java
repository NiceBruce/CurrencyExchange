package com.rtfmyoumust.currencyexchange.utils;

import com.rtfmyoumust.currencyexchange.customexceptions.EntityIsExists;
import com.rtfmyoumust.currencyexchange.customexceptions.DataAccessException;
import com.rtfmyoumust.currencyexchange.customexceptions.FieldNotFoundException;
import com.rtfmyoumust.currencyexchange.dao.ExchangeRateDao;
import com.rtfmyoumust.currencyexchange.dto.CreateExchangeRateDto;
import lombok.SneakyThrows;

import java.math.BigDecimal;

public class ExchangeRateValidator {
    public static final ExchangeRateValidator INSTANCE = new ExchangeRateValidator();
    public static final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    public static final CurrencyValidator currencyValidator = CurrencyValidator.getInstance();
    public static ExchangeRateValidator getInstance() {
        return INSTANCE;
    }
    // одна или две валюты НЕ СУЩЕСТВУЮТ в БД - 404
    // валютная пара с переданным кодом СУЩЕСТВУЕТ в БД - 400
    // ОТСУТСТВУЕТ поле формы - 409

    @SneakyThrows
    public boolean isValidExchangeRate(CreateExchangeRateDto createExcangeRateDto) {
        return isCurrenciesNotExist(createExcangeRateDto);
    }

    public boolean isValidExchangeRate(String rate) throws FieldNotFoundException {
        try {
            new BigDecimal(rate);
            return true;
        } catch (NumberFormatException e) {
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Курс обмена'");
        }
    }

    public boolean isCurrenciesNotExist(CreateExchangeRateDto createExcangeRateDto) throws DataAccessException, EntityIsExists {
        return false;
    }

    public boolean isExchangeRateNotExist(String baseCurrencyCode, String targetCurrencyCode) {
        return false;
    }
}
