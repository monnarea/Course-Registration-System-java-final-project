package org.system.config;

import java.sql.*;

public class DatabaseConfig {

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/student",
                    "postgres",
                    "128028");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }
}