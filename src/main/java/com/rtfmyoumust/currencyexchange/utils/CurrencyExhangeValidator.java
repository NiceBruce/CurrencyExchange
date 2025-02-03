package com.rtfmyoumust.currencyexchange.utils;

import com.rtfmyoumust.currencyexchange.customexceptions.FieldNotFoundException;
import com.rtfmyoumust.currencyexchange.customexceptions.InvalidDataException;
import com.rtfmyoumust.currencyexchange.dto.CurrencyRequestDto;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyExhangeValidator {
    public static final CurrencyExhangeValidator CURRENCY_VALIDATOR = new CurrencyExhangeValidator();
    public static final Set<String> AVAILABLE_CURRENCIES_CODE = Currency.getAvailableCurrencies().stream()
            .map(Currency::getCurrencyCode).collect(Collectors.toSet());
    public static final Set<String> AVAILABLE_CURRENCIES_SIGN = Currency.getAvailableCurrencies().stream()
            .map(Currency::getSymbol).collect(Collectors.toSet());

    @SneakyThrows
    public static void validateCurrency(CurrencyRequestDto currencyDto) {
        String code = currencyDto.getCode();
        String name = currencyDto.getName();
        String sign = currencyDto.getSign();
        validateCodeCurrency(code);
        validateNameCurrency(name);
        validateSignCurrencies(code, sign);
    }

    public static void validateCodeCurrency(String code) {
        if (code.isEmpty()) {
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Код валюты'");
        }

        if (!code.matches("[A-Z]{3}")) {
            throw new InvalidDataException(String.format("Неправильный формат кода валюты: %s," +
                    " код должен быть в формате [A-Z] и иметь длину 3 символа", code));
        }

        if (!AVAILABLE_CURRENCIES_CODE.contains(code)) {
            throw new InvalidDataException(String.format("Неправильный формат кода валюты: %s," +
                    " код должен соответствовать стандарту ISO 4217", code));
        }
    }

    public static void validateRate(String rate) {
        try {
            BigDecimal exchangeRate = new BigDecimal(rate);
            if (exchangeRate.signum() <= 0) {
                throw new InvalidDataException("Курс обмена должен быть положительным числом");
            }
        } catch (NumberFormatException e) {
            if (!rate.matches("\\d*")) {
                throw new InvalidDataException("'Курс обмена' должен должен иметь числовой формат");
            }
            if (rate.contains(",")) {
                throw new InvalidDataException("В качестве разделителя для 'курса обмена' должна использоваться '.' вместо ','");
            }
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Курс обмена'");
        }
    }

    public static void validateAmount(String amount) {
        try {
            BigDecimal exchangeRate = new BigDecimal(amount);
            if (exchangeRate.signum() <= 0) {
                throw new InvalidDataException("'Количество валюты' должно быть положительным числом");
            }
        } catch (NumberFormatException e) {
            if (!amount.matches("\\d*")) {
                throw new InvalidDataException("'Количество валюты' должно иметь числовой формат");
            }
            if (amount.contains(",")) {
                throw new InvalidDataException("В качестве разделителя для 'количества валюты' должна использоваться '.' вместо ','");
            }
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Количество валюты'");
        }
    }

    public static void validateExchangeRateCode(String baseCurrencyCode, String targetCurrencyCode) {
        if (baseCurrencyCode.equals(targetCurrencyCode)) {
            throw new InvalidDataException("Валюты обмена должны отличаться друг от друга");
        }
    }

    private static void validateNameCurrency(String name) {
        if (name.isEmpty()) {
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Наименование валюты'");
        }

        if (!name.matches("[a-zA-Z ]{3,30}")) {
            throw new InvalidDataException(String.format("Неправильный формат имени валюты: %s, " +
                    " имя должно быть на латинице и иметь длину от 3 до 30 символов", name));
        }
    }

    private static void validateSignCurrencies(String code, String sign) {
        if (sign.isEmpty()) {
            throw new FieldNotFoundException("Отсутствует нужное поле формы - 'Знак валюты'");
        }

        if (!AVAILABLE_CURRENCIES_SIGN.contains(sign)) {
            throw new InvalidDataException(String.format("Неправильный формат символа валюты: '%s'" +
                    " - не соответствует ни одной существующей валюте!", sign));
        }

        if (!Currency.getInstance(code).getSymbol().equals(sign)) {
            throw new InvalidDataException(String.format("Неправильный формат символа валюты: '%s', " +
                    "символ должен соответствовать переданному коду: '%s'", sign, code));
        }
    }

    public static CurrencyExhangeValidator getInstance() {
        return CURRENCY_VALIDATOR;
    }
}