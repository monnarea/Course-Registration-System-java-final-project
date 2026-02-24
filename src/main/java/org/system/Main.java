package org.system;
import org.system.config.DatabaseConfig;
import org.system.view.MainMenu;
import java.sql.Connection;
import static org.system.view.View.*;

public class Main {
    public static void main(String[] args) {
        new MainMenu().start();
//        Test database
        Connection con = DatabaseConfig.getConnection();

        if (con != null) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Connection failed.");
        }
    }
}
