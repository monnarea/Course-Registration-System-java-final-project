package org.system.config;

import java.sql.*;

public class DatabaseConfig {

        public static Connection getConnection() {
            try {
                return DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/student",
                    "postgres",
                    "1234");
            } catch (SQLException e) {
                System.out.println("Connection failed: " + e.getMessage());
                return null;
            }
        }


//        {
//            try (Connection connection = DriverManager.getConnection(
//                    "jdbc:postgresdl://localhost:5432/Course_registation_bd",
//                    "postgres",
//                    "1234");
////                PreparedStatement statement = connection.prepareStatement(sql);
////             ResultSet rs = statement.executeQuery()
//
//            ) {
//
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }


}

