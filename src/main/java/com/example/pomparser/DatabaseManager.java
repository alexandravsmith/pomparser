package com.example.pomparser;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseManager {
    private static final String URL = System.getenv("DB_URL") != null ? System.getenv("DB_URL") : "dummy-url";
    private static final String USER = System.getenv("DB_USER") != null ? System.getenv("DB_USER") : "dummy-user";
    private static final String PASSWORD = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "dummy-password";

    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the database successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public boolean urlExists(String url) {
        String sql = "SELECT COUNT(*) FROM websites WHERE url = ?";
        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, url);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void updateWebsite(Map<String, Object> columnValues) {
        StringBuilder setClause = new StringBuilder();
        Object[] values = new Object[columnValues.size()];
        int index = 0;

        for (Map.Entry<String, Object> entry : columnValues.entrySet()) {
            if (!entry.getKey().equals("url")) {
                setClause.append(entry.getKey()).append(" = ?, ");
                values[index++] = entry.getValue();
            }
        }

        // Remove the trailing comma and space
        setClause.setLength(setClause.length() - 2);

        String sql = "UPDATE websites SET " + setClause + " WHERE url = ?";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < index; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.setString(index + 1, (String) columnValues.get("url"));
            pstmt.executeUpdate();
            System.out.println("Data updated successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void insertWebsite(Map<String, Object> columnValues) {
        StringBuilder columns = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();
        Object[] values = new Object[columnValues.size()];
        int index = 0;

        for (Map.Entry<String, Object> entry : columnValues.entrySet()) {
            columns.append(entry.getKey()).append(", ");
            placeholders.append("?, ");
            values[index++] = entry.getValue();
        }

        // Remove the trailing comma and space
        columns.setLength(columns.length() - 2);
        placeholders.setLength(placeholders.length() - 2);

        String sql = "INSERT INTO websites(" + columns + ") VALUES(" + placeholders + ")";

        try (Connection conn = this.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setObject(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("Data inserted successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
