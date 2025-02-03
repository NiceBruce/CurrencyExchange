package com.rtfmyoumust.currencyexchange.dao;

import com.rtfmyoumust.currencyexchange.customexceptions.DataAccessException;
import com.rtfmyoumust.currencyexchange.customexceptions.EntityIsExists;
import com.rtfmyoumust.currencyexchange.entity.Currency;
import lombok.SneakyThrows;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rtfmyoumust.currencyexchange.utils.DataBaseConnector.getConnection;

public class CurrenciesDao {
    public static final String GET_CURRENCY_BY_CODE = "SELECT * FROM currencies WHERE code = ?";
    public static final String GET_ALL_CURRENCIES = "SELECT * FROM currencies";
    public static final String SAVE_CURRENCY = "INSERT INTO Currencies(code, fullName, sign) VALUES(?,?,?)";

    private static final CurrenciesDao INSTANCE = new CurrenciesDao();
    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }

    public Optional<Currency> getCurrencyByCode(String code) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(GET_CURRENCY_BY_CODE)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            Currency currency = null;
            if (rs.next()) {
                currency = buildCurrency(rs);
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }

    public List<Currency> getAllCurrencies() {
        List<Currency> currencies = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(GET_ALL_CURRENCIES)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Currency currency = buildCurrency(rs);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
        return currencies;
    }

    @SneakyThrows
    public Currency addCurrency(Currency currency) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SAVE_CURRENCY)) {
            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getName());
            stmt.setString(3, currency.getSign());
            stmt.execute();

            var genetatedKeys = stmt.getGeneratedKeys();
            if (genetatedKeys.next()) {
                currency.setId(genetatedKeys.getInt(1));
            }
            return currency;
        } catch (SQLException e) {
            if (e instanceof SQLiteException) {
                SQLiteException sqliteException = (SQLiteException) e;
                if (sqliteException.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new EntityIsExists(String.format("Валюта с кодом '%s, наименованием '%s' или знаком '%s' уже существует",
                            currency.getCode(), currency.getName(), currency.getSign()));
                }
            }
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }

    private Currency buildCurrency(ResultSet rs) throws SQLException {
        return Currency.builder()
                .id(rs.getInt("id"))
                .code(rs.getString("Code"))
                .name(rs.getString("FullName"))
                .sign(rs.getString("Sign"))
                .build();
    }
}