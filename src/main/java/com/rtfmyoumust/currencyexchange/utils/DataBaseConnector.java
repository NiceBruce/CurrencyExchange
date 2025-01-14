package com.rtfmyoumust.currencyexchange.utils;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class DataBaseConnector {

    private static final HikariDataSource DATA_SOURCE;

    static {
        try {
            HikariConfig config = new HikariConfig("/db.properties");
            DATA_SOURCE = new HikariDataSource(config);
        } catch (Exception e) {
            System.err.println("Error initializing HikariCP: " + e.getMessage());
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DATA_SOURCE.getConnection();
    }

    public static void closeConnection(Connection connection) throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }
}
