package com.backend.design.pattern.sqlQueryOptimization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MakeSqlConnection {

    public void MakeConnection() {
        String dbUrl = "jdbc:mysql://localhost:3306/";
        String dbName = "studio";
        String user = "root";
        String password = "Saurabh@1234";

        // before making query load your mysql driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.err.println("MySQL JDBC Driver found");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Add it to the classpath.");
            e.printStackTrace();
            return;
        }

        // Step 1: Create the database if it doesn’t exist
        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
                Statement stmt = conn.createStatement()) {
            String createDatabaseSQL = "CREATE DATABASE IF NOT EXISTS " + dbName;
            stmt.executeUpdate(createDatabaseSQL);
            System.out.println("Database '" + dbName + "' ensured.");
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Step 2: Connect to the newly created database
        String dbConnectionUrl = "jdbc:mysql://localhost:3306/" + dbName + "?useSSL=false&serverTimezone=UTC";

        // Step 3: Create the artist table
        String createTableSQL = "CREATE TABLE IF NOT EXISTS studio (" + "studio_id INT AUTO_INCREMENT PRIMARY KEY, "
                + "studio_name VARCHAR(255) NOT NULL, " + "favorite_by JSON" + ")";

        try (Connection conn = DriverManager.getConnection(dbConnectionUrl, user, password);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTableSQL);
            System.out.println("Studio table created successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String insertDataSQL = "INSERT INTO studio (studio_name, favorite_by) VALUES " + "('Saurabh', '[1]'),"
                + "('Yash', '[4, 5]')," + "('Okay', '[6, 7, 1 , 2]')";

        try (Connection conn = DriverManager.getConnection(dbConnectionUrl, user, password);
                Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertDataSQL);
            System.out.println("Dummy data inserted successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
