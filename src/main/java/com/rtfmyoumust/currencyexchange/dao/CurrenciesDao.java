package com.rtfmyoumust.currencyexchange.dao;

import com.rtfmyoumust.currencyexchange.customexceptions.DataAccessException;
import com.rtfmyoumust.currencyexchange.entity.Currency;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rtfmyoumust.currencyexchange.utils.DataBaseConnector.getConnection;


public class CurrenciesDao {

    private static final CurrenciesDao INSTANCE = new CurrenciesDao();

    public static CurrenciesDao getInstance() {
        return INSTANCE;
    }

    public Optional<Currency> getCurrencyByCode(String code) throws DataAccessException {
        String sql = "SELECT * FROM currencies WHERE code = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            Currency currency = null;
            if (rs.next()) {
                currency = new Currency(rs.getInt("id"),
                        rs.getString("Code"),
                        rs.getString("FullName"),
                        rs.getString("Sign"));
            }
            return Optional.ofNullable(currency);
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }

    public List<Currency> getAllCurrencies() throws DataAccessException {
        String sql = "SELECT * FROM currencies";
        List<Currency> currencies = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                Currency currency = Currency.builder()
                        .id(rs.getInt("id"))
                        .code(rs.getString("Code"))
                        .name(rs.getString("FullName"))
                        .sign(rs.getString("Sign"))
                                .build();
                currencies.add(currency);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
        return currencies;
    }

    @SneakyThrows
    public Currency addCurrency(Currency currency) {
        String sql = "INSERT INTO Currencies(code, fullName, sign) VALUES(?,?,?)";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }

    public void updateCurrency(int id, String code, String name, String symbol) throws DataAccessException {
        String sql = "UPDATE currencies SET code = ?, name = ?, sign = ? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            stmt.setString(2, name);
            stmt.setString(3, symbol);
            stmt.setInt(4, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }

    public void deleteCurrency(int id) throws DataAccessException {
        String sql = "DELETE FROM currencies WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }
}
