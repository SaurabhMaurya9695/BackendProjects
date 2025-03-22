package com.backend.design.pattern.sqlQueryOptimization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FetchingData {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/tatto";
    private static final String USER = "root";
    private static final String PASSWORD = "Saurabh@1234";

    public static void getLikedEntities(int userId, String role) {
        String query = "SELECT 'artist' AS source_table, artist_id AS id, artist_name AS name, role FROM artist "
                + "WHERE JSON_CONTAINS(favorite_by, '1')" + "UNION ALL "
                + "SELECT 'studio' AS source_table, studio_id AS id, studio_name AS name, role "
                + "FROM studio WHERE JSON_CONTAINS(favorite_by, '1') ";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
                PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            // Print table header
            System.out.println("+------------+----+-------------+--------+");
            System.out.println("| Table      | ID | Name        | Role   |");
            System.out.println("+------------+----+-------------+--------+");

            while (rs.next()) {
                String sourceTable = rs.getString("source_table");
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String okay = rs.getString("role");

                System.out.printf("| %-10s | %-2d | %-11s | %-6s |\n", sourceTable, id, name, okay);
            }

            // Print table footer
            System.out.println("+------------+----+-------------+--------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
