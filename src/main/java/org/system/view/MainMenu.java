
package org.system.view;

import org.system.controller.AdminLoginController;
import org.system.model.dto.response.AdminResponseDto;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminMenu adminMenu = new AdminMenu();
    private final StudentMenu studentMenu = new StudentMenu();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";
    public void start() {
        System.out.println(yellow + """
                    ███████╗███╗   ██╗██████╗  ██████╗ ██╗     ██╗     ███╗   ███╗███████╗███╗   ██╗████████╗    ███████╗██╗   ██╗███████╗████████╗███████╗███╗   ███╗
                    ██╔════╝████╗  ██║██╔══██╗██╔═══██╗██║     ██║     ████╗ ████║██╔════╝████╗  ██║╚══██╔══╝    ██╔════╝╚██╗ ██╔╝██╔════╝╚══██╔══╝██╔════╝████╗ ████║
                    █████╗  ██╔██╗ ██║██████╔╝██║   ██║██║     ██║     ██╔████╔██║█████╗  ██╔██╗ ██║   ██║       ███████╗ ╚████╔╝ ███████╗   ██║   █████╗  ██╔████╔██║
                    ██╔══╝  ██║╚██╗██║██╔══██╗██║   ██║██║     ██║     ██║╚██╔╝██║██╔══╝  ██║╚██╗██║   ██║       ╚════██║  ╚██╔╝  ╚════██║   ██║   ██╔══╝  ██║╚██╔╝██║
                    ███████╗██║ ╚████║██║  ██║╚██████╔╝███████╗███████╗██║ ╚═╝ ██║███████╗██║ ╚████║   ██║       ███████║   ██║   ███████║   ██║   ███████╗██║ ╚═╝ ██║
                    ╚══════╝╚═╝  ╚═══╝╚═╝  ╚═╝ ╚═════╝ ╚══════╝╚══════╝╚═╝     ╚═╝╚══════╝╚═╝  ╚═══╝   ╚═╝       ╚══════╝   ╚═╝   ╚══════╝   ╚═╝   ╚══════╝╚═╝     ╚═╝
                    """);
        while (true) {

            System.out.println(purple +"""
                    ╔═════════════════════════════════╗
                    ║             MENU                ║
                    ╠═════════════════════════════════╣
                    ║ [1]. View As Student            ║
                    ║ [2]. Login As Admin             ║
                    ║ [0]. Exit                       ║
                    ╚═════════════════════════════════╝
                    """);
            System.out.print(yellow+"[-] Choose an option: ");


            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> studentMenu.studentStart();
                case "2" -> loginAdmin();
                case "0" -> { System.out.println("Goodbye!"); return; }
                default  -> System.out.println(red +"Invalid option.");
            }
        }
    }
    private void loginAdmin() {
        AdminLoginController adminController = new AdminLoginController();
        AdminResponseDto admin = adminController.login();

        if (admin != null) {
            new AdminMenu().AdminStart();
        } else {
            System.out.println("Access denied. Exiting.");
        }
    }
}
