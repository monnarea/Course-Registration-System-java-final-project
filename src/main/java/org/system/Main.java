package org.system;
import org.system.config.DatabaseConfig;
import org.system.view.AdminMenu;
import org.system.view.MainMenu;
import org.system.view.StudentMenu;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) {

        new MainMenu().start();
//        Connection con = DatabaseConfig.getConnection();
//
//        if (con != null) {
//            System.out.println("Connected successfully!");
//        } else {
//            System.out.println("Connection failed.");
//        }
    }
}