package org.system.controller;

import org.system.model.dto.request.AdminLoginRequestDto;
import org.system.model.dto.response.AdminResponseDto;
import org.system.service.AdminService;

import java.util.Scanner;

public class AdminLoginController {

    private final AdminService adminService = new AdminService();
    private final Scanner scanner = new Scanner(System.in);

    // Returns logged-in admin or null if failed
    public AdminResponseDto login() {

        System.out.println("========== Admin Login ==========");
        System.out.print("Enter Username : ");
        String fullName = scanner.nextLine().trim();

        System.out.print("Enter Password : ");
        String password = scanner.nextLine().trim();

        AdminLoginRequestDto requestDto = new AdminLoginRequestDto(fullName, password);

        AdminResponseDto loggedInAdmin = adminService.login(requestDto);

        if (loggedInAdmin != null) {
            System.out.println("---------------------------------");
            System.out.println("Admin ID : " + loggedInAdmin.getAdminId());
            System.out.println("Full Name: " + loggedInAdmin.getFullName());
            System.out.println("=================================");
        }

        return loggedInAdmin;
    }
}