package com.rtfmyoumust.currencyexchange.service;

import com.rtfmyoumust.currencyexchange.customexceptions.EntityNotFoundException;
import com.rtfmyoumust.currencyexchange.dao.CurrenciesDao;
import com.rtfmyoumust.currencyexchange.dao.ExchangeRateDao;
import com.rtfmyoumust.currencyexchange.dto.ExchangeRequestDto;
import com.rtfmyoumust.currencyexchange.dto.ExchangeResponseDto;
import com.rtfmyoumust.currencyexchange.entity.ExchangeRate;
import com.rtfmyoumust.currencyexchange.mapper.CurrencyToDtoMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static com.rtfmyoumust.currencyexchange.utils.CurrencyExhangeValidator.validateAmount;
import static com.rtfmyoumust.currencyexchange.utils.CurrencyExhangeValidator.validateExchangeRateCode;

public class ExchangeService {
    public static final String USD_CODE = "USD";
    public static final int RATE_SCALE = 5;
    public static final int AMOUNT_SCALE = 2;
    public static final ExchangeService INSTANCE = new ExchangeService();
    private final ExchangeRateDao exchangeRateDao = ExchangeRateDao.getInstance();
    private final CurrenciesDao currenciesDao = CurrenciesDao.getInstance();
    private final CurrencyToDtoMapper currencyToDtoMapper = CurrencyToDtoMapper.getInstance();

    public static ExchangeService getInstance() {
        return INSTANCE;
    }

    public ExchangeResponseDto getExchange(ExchangeRequestDto exchangeRequestDto) {
        validateAmount(exchangeRequestDto.getAmount());
        validateExchangeRateCode(exchangeRequestDto.getFrom(), exchangeRequestDto.getTo());
        BigDecimal amount = new BigDecimal(exchangeRequestDto.getAmount());

        var exchangeRate = exchangeRateDao.getExchangeRateByCode(exchangeRequestDto.getFrom(),
                exchangeRequestDto.getTo());

        ExchangeRate exchange = exchangeRate.or(() -> getReverseExchangeRate(exchangeRequestDto))
                .orElseGet(() -> getCrossExchangeRate(exchangeRequestDto).get());

        BigDecimal convertedAmount = exchange.getRate().multiply(amount).setScale(AMOUNT_SCALE, RoundingMode.HALF_EVEN);
        return getExchangeDto(exchange, amount, convertedAmount);
    }

    private ExchangeResponseDto getExchangeDto(ExchangeRate exchangeRate, BigDecimal amount, BigDecimal convertedAmount) {
        return ExchangeResponseDto.builder()
                .baseCurrency(currencyToDtoMapper.mapFrom(exchangeRate.getBaseCurrency()))
                .targetCurrency(currencyToDtoMapper.mapFrom(exchangeRate.getTargetCurrency()))
                .rate(exchangeRate.getRate())
                .amount(amount)
                .convertedAmount(convertedAmount)
                .build();
    }

    private Optional<ExchangeRate> getReverseExchangeRate(ExchangeRequestDto exchangeRequestDto) {
        var swapExchangeRate = exchangeRateDao.getExchangeRateByCode(exchangeRequestDto.getTo(),
                exchangeRequestDto.getFrom());
        if (!swapExchangeRate.isPresent()) {
            return swapExchangeRate;
        } else {
            BigDecimal indirectRate = BigDecimal.ONE.divide(swapExchangeRate.get().getRate(), RATE_SCALE, RoundingMode.HALF_EVEN);
            return Optional.ofNullable(ExchangeRate.builder()
                    .baseCurrency(swapExchangeRate.get().getTargetCurrency())
                    .targetCurrency(swapExchangeRate.get().getBaseCurrency())
                    .rate(indirectRate)
                    .build());
        }
    }

    private Optional<ExchangeRate>  getCrossExchangeRate(ExchangeRequestDto exchangeRequestDto) {
        var currencyFrom = currenciesDao.getCurrencyByCode(exchangeRequestDto.getFrom())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Валюта с кодом: %s не найдена",
                        exchangeRequestDto.getFrom())));
        var currencyTo = currenciesDao.getCurrencyByCode(exchangeRequestDto.getTo())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Валюта с кодом: %s не найдена",
                        exchangeRequestDto.getTo())));

        var usdToBaseCurrency = exchangeRateDao.getExchangeRateByCode(USD_CODE, exchangeRequestDto.getFrom())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Обменный курс для пары: %s не найден",
                        USD_CODE + exchangeRequestDto.getFrom())));
        var usdToTargetCurrency = exchangeRateDao.getExchangeRateByCode(USD_CODE, exchangeRequestDto.getTo())
                .orElseThrow(() -> new EntityNotFoundException(String.format("Обменный курс для пары: %s не найден",
                        USD_CODE + exchangeRequestDto.getTo())));

        BigDecimal rateBasedOnUsdt = usdToTargetCurrency.getRate().divide(usdToBaseCurrency.getRate(), RATE_SCALE, RoundingMode.CEILING);
        return Optional.ofNullable(ExchangeRate.builder()
                .baseCurrency(currencyFrom)
                .targetCurrency(currencyTo)
                .rate(rateBasedOnUsdt)
                .build());
    }
}