package org.system.view;

import org.system.controller.AdminLoginController;
import org.system.model.dto.response.AdminResponseDto;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final AdminMenu adminMenu = new AdminMenu();
    private final StudentMenu studentMenu = new StudentMenu();
    public void start() {
        while (true) {
            System.out.println("""
                    ╔═════════════════════════════════╗
                    ║   COURSE REGISTRATION SYSTEM    ║
                    ╠═════════════════════════════════╣
                    ║ 1. View As Student              ║
                    ║ 2. Login As Admin               ║
                    ║ 0. Exit                         ║
                    ╚═════════════════════════════════╝
                    """);
            System.out.print("Choice: ");


            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> studentMenu.studentStart();
                case "2" -> loginAdmin();
                case "0" -> { System.out.println("Goodbye!"); return; }
                default  -> System.out.println("Invalid option.");
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
