package org.system;
import org.system.config.DatabaseConfig;
import org.system.poi.ExcelSpreadSheetGenerator;
import org.system.view.MainMenu;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws Exception {
        ExcelSpreadSheetGenerator.execute();
        Connection con = DatabaseConfig.getConnection();

        if (con != null) {
            System.out.println("Connected successfully!");
        } else {
            System.out.println("Connection failed.");
        }
        new MainMenu().start();
//        Test database

    }
}
