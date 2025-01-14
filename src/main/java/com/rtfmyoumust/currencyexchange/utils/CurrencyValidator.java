package com.rtfmyoumust.currencyexchange.utils;

import com.rtfmyoumust.currencyexchange.customexceptions.EntityIsExists;
import com.rtfmyoumust.currencyexchange.customexceptions.DataAccessException;
import com.rtfmyoumust.currencyexchange.customexceptions.FieldNotFoundException;
import com.rtfmyoumust.currencyexchange.customexceptions.InvalidDataException;
import com.rtfmyoumust.currencyexchange.dao.CurrenciesDao;
import com.rtfmyoumust.currencyexchange.dto.CreateCurrencyDto;
import lombok.SneakyThrows;

import java.util.Currency;

public class CurrencyValidator {
    public static final CurrencyValidator CURRENCY_VALIDATOR = new CurrencyValidator();
    private static final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    @SneakyThrows
    public boolean isValidCurrency(CreateCurrencyDto currencyDto) {
        if (isCurrencyExists(currencyDto.getCode())) {
            throw new EntityIsExists(String.format("Валюта с кодом: '%s' - уже существует", currencyDto.getCode()));
        }
        return isValidCodeCurrency(currencyDto.getCode())
        && isValidNameCurrency(currencyDto.getName()) && isValidSignCurrencies(currencyDto.getSign());
    }

    public boolean isValidCodeCurrency(String code) throws FieldNotFoundException, InvalidDataException {
        if (code.isEmpty()) {
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Код валюты'");
        }

        if (!code.matches("[A-Z]{3}")) {
            throw new InvalidDataException(String.format("Неправильный формат кода валюты: %s , код должен быть в формате [A-Z] и иметь длину 3 символа", code));
        }

        return Currency.getAvailableCurrencies().stream()
                .anyMatch(Currency-> Currency.getCurrencyCode().equals(code));
    }

    public boolean isValidNameCurrency(String name) throws FieldNotFoundException {
        if (name.isEmpty()) {
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Наименование валюты'");
        }

        return name.matches("[\\p{L}\\- ]{3,30}");
    }

    public boolean isValidSignCurrencies(String sign) throws FieldNotFoundException {
        if (sign.isEmpty()) {
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Знак валюты'");
        }

        return Currency.getAvailableCurrencies().stream()
                .anyMatch(сurrency-> сurrency.getSymbol().equals(sign));
    }

    public boolean isCurrencyExists(String code) throws DataAccessException {
        return currenciesDao.getCurrencyByCode(code).isPresent();
    }

    public static CurrencyValidator getInstance() {
        return CURRENCY_VALIDATOR;
    }
}
