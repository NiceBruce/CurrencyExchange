package com.rtfmyoumust.currencyexchange.dao;

import com.rtfmyoumust.currencyexchange.customexceptions.DataAccessException;
import com.rtfmyoumust.currencyexchange.customexceptions.EntityIsExists;
import com.rtfmyoumust.currencyexchange.entity.Currency;
import com.rtfmyoumust.currencyexchange.entity.ExchangeRate;
import org.sqlite.SQLiteErrorCode;
import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.rtfmyoumust.currencyexchange.utils.DataBaseConnector.getConnection;

public class ExchangeRateDao {

    private static final String FIND_ALL = "SELECT ExchangeRates.ID, b.id AS baseID, b.Code baseCode, b.FullName baseName, b.Sign AS baseSign,\n" +
            "t.id AS targetID, t.Code AS targetCode, t.FullName AS targetName, t.Sign AS targetSign," +
            "ExchangeRates.Rate FROM ExchangeRates\n" +
            "LEFT JOIN Currencies AS b ON ExchangeRates.BaseCurrencyId = b.ID\n" +
            "LEFT JOIN Currencies AS t ON ExchangeRates.TargetCurrencyId = t.ID\n";
    private static final String FIND_BY_CODE ="WHERE b.code LIKE ? AND t.code LIKE ?";
    private static final String SAVE = "INSERT INTO ExchangeRates(BaseCurrencyId, TargetCurrencyId, rate) VALUES(?,?,?)";
    private static final String UPDATE = "UPDATE ExchangeRates SET Rate=? WHERE id=?";

    private static final ExchangeRateDao INSTANCE = new ExchangeRateDao();

    public static ExchangeRateDao getInstance() {
        return INSTANCE;
    }

    public Optional<ExchangeRate> getExchangeRateByCode(String baseCode, String targetCode) {
        ExchangeRate exchangeRate = null;
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(FIND_ALL + FIND_BY_CODE)) {
            stmt.setString(1, baseCode);
            stmt.setString(2, targetCode);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                exchangeRate = buildExchangeRate(rs);
            }
            return Optional.ofNullable(exchangeRate);
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }

    public List<ExchangeRate> getAllExchangeRates() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(FIND_ALL)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                ExchangeRate exchangeRate = buildExchangeRate(rs);
                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
        return exchangeRates;
    }

    public ExchangeRate addExchangeRate(ExchangeRate exchangeRate) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(SAVE)) {
            stmt.setInt(1, exchangeRate.getBaseCurrency().getId());
            stmt.setInt(2, exchangeRate.getTargetCurrency().getId());
            stmt.setBigDecimal(3, exchangeRate.getRate());
            stmt.execute();

            var genetatedKeys = stmt.getGeneratedKeys();
            if (genetatedKeys.next()) {
                exchangeRate.setId(genetatedKeys.getInt(1));
            }
            return exchangeRate;
        } catch (SQLException e) {
            if (e instanceof SQLiteException) {
                SQLiteException sqliteException = (SQLiteException) e;
                if (sqliteException.getResultCode().code == SQLiteErrorCode.SQLITE_CONSTRAINT_UNIQUE.code) {
                    throw new EntityIsExists(String.format("Обменный курс с кодом: '%s' - уже существует",
                            exchangeRate.getBaseCurrency().getCode() + exchangeRate.getTargetCurrency().getCode()));
                }
            }
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }

    public ExchangeRate updateExchangeRate(ExchangeRate exchangeRate) {
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(UPDATE)) {
            stmt.setBigDecimal(1, exchangeRate.getRate());
            stmt.setInt(2, exchangeRate.getId());
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated != 1) {
                throw new DataAccessException(String.format("Ошибка обновления валютной пары: %s",
                        exchangeRate.getBaseCurrency().getCode() + exchangeRate.getTargetCurrency().getCode()));
            }
            return exchangeRate;
        } catch (SQLException e) {
            throw new DataAccessException("Ошибка при работе с базой данных");
        }
    }

    private ExchangeRate buildExchangeRate(ResultSet rs) throws SQLException {
        return ExchangeRate.builder()
                .id(rs.getInt("id"))
                .baseCurrency(Currency.builder()
                        .id(rs.getInt("baseID"))
                        .code(rs.getString("baseCode"))
                        .name(rs.getString("baseName"))
                        .sign(rs.getString("baseID"))
                        .build())
                .targetCurrency(Currency.builder()
                        .id(rs.getInt("targetID"))
                        .code(rs.getString("targetCode"))
                        .name(rs.getString("targetName"))
                        .sign(rs.getString("targetID"))
                        .build())
                .rate(rs.getBigDecimal("rate"))
                .build();
    }
}