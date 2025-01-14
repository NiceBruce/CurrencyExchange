package com.rtfmyoumust.currencyexchange.service;

import com.rtfmyoumust.currencyexchange.customexceptions.DataAccessException;
import com.rtfmyoumust.currencyexchange.customexceptions.EntityNotFoundException;
import com.rtfmyoumust.currencyexchange.customexceptions.ExchangeServiceException;
import com.rtfmyoumust.currencyexchange.dao.CurrenciesDao;
import com.rtfmyoumust.currencyexchange.dao.ExchangeRateDao;
import com.rtfmyoumust.currencyexchange.dto.CreateExchangeDTO;
import com.rtfmyoumust.currencyexchange.dto.ExchangeDto;
import com.rtfmyoumust.currencyexchange.entity.ExchangeRate;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExchangeService {
    public static final String USD_CODE = "USD";
    public static final ExchangeService INSTANCE = new ExchangeService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();

    public ExchangeDto getExchange(CreateExchangeDTO createExchangeDTO) throws DataAccessException, ExchangeServiceException, EntityNotFoundException {

        ExchangeDto exchangeDto = null;
        BigDecimal amount = new BigDecimal(createExchangeDTO.getAmount());

        var currencyFrom = currenciesDao.getCurrencyByCode(createExchangeDTO.getFrom())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Валюта с кодом: %s не найдена", createExchangeDTO.getFrom())));
        var currencyTo = currenciesDao.getCurrencyByCode(createExchangeDTO.getTo())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Валюта с кодом: %s не найдена", createExchangeDTO.getTo())));

        var exchangeRate = exchangeRateDao.getExchangeRateByCode(createExchangeDTO.getFrom(),
                createExchangeDTO.getTo());
        var swapExchangeRate = exchangeRateDao.getExchangeRateByCode(createExchangeDTO.getTo(),
                createExchangeDTO.getFrom());

        if (exchangeRate.isPresent()) {
            ExchangeRate exchange = exchangeRate.get();
            exchangeDto = getExchangeDto(exchange, amount, exchange.getRate().multiply(amount));
        } else if (swapExchangeRate.isPresent()) {
            ExchangeRate exchange = swapExchangeRate.get();
            exchange = ExchangeRate.builder()
                    .baseCurrency(exchange.getTargetCurrency())
                    .targetCurrency(exchange.getBaseCurrency())
                    .rate(exchange.getRate())
                    .build();
            exchangeDto = getExchangeDto(exchange, amount,
                    amount.divide(exchange.getRate(), 5, RoundingMode.CEILING));
        } else {
            var usdToBaseCurrency = exchangeRateDao.getExchangeRateByCode(USD_CODE, createExchangeDTO.getFrom())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Обменный курс для пары: %s не найден", USD_CODE + createExchangeDTO.getFrom())));
            var usdToTargetCurrency = exchangeRateDao.getExchangeRateByCode(USD_CODE, createExchangeDTO.getTo())
                    .orElseThrow(() -> new EntityNotFoundException(String.format("Обменный курс для пары: %s не найден", USD_CODE + createExchangeDTO.getTo())));
            BigDecimal rateBasedOnUsdt = usdToTargetCurrency.getRate().divide(usdToBaseCurrency.getRate(), 5, RoundingMode.CEILING);
            ExchangeRate exchangeRateBasedOnUsdt = ExchangeRate.builder()
                    .baseCurrency(currencyFrom)
                    .targetCurrency(currencyTo)
                    .rate(rateBasedOnUsdt)
                    .build();
            exchangeDto = getExchangeDto(exchangeRateBasedOnUsdt, amount, rateBasedOnUsdt.multiply(amount));
        }
        return exchangeDto;
    }

    private ExchangeDto getExchangeDto(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal convertedAmount) {
        return ExchangeDto.builder()
                .baseCurrency(exchangeRate.getBaseCurrency())
                .targetCurrency(exchangeRate.getTargetCurrency())
                .rate(exchangeRate.getRate())
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }

    public static ExchangeService getInstance() {
        return INSTANCE;
    }
}