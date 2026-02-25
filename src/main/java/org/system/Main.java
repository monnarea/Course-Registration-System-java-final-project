package org.system;
import org.system.config.DatabaseConfig;
import org.system.view.StudentMenu;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {
        new StudentMenu().start();
//        Test database
        Connection con = DatabaseConfig.getConnection();

        if (con != null) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Connection failed.");
        }
    }
}
