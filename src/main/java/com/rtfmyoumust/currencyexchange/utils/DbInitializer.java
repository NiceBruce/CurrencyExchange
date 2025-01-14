package com.rtfmyoumust.currencyexchange.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbInitializer {
    public static void main(String[] args) {
        String dbURl = "jdbc:sqlite:src/main/resources/db/currencyEchange.db";
        String sqlFilePath = "src/main/resources/db/init.sql";

        try (Connection connection = DriverManager.getConnection(dbURl)) {
            System.out.println("Connected to database");
            StringBuilder sqlScript = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new FileReader(sqlFilePath))) {
                String line;
                while((line = reader.readLine()) != null) {
                    sqlScript.append(line).append("\n");
                }
            }

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(sqlScript.toString());
                System.out.println("SQL-script is done!");
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }

    }
}
