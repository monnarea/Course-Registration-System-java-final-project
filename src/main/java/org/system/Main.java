    package org.system;

    import org.system.poi.ExcelSpreadSheetGenerator;
    import org.system.view.MainMenu;

    public class Main {
        public static void main(String[] args) {
            ExcelSpreadSheetGenerator.execute();

            new MainMenu().start();

        }
    }