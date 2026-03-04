package org.system.controller;

import org.system.model.dto.request.AdminLoginRequestDto;
import org.system.model.dto.response.AdminResponseDto;
import org.system.service.AdminService;

import java.util.Scanner;

public class AdminLoginController {

    private final AdminService adminService = new AdminService();
    private final Scanner scanner = new Scanner(System.in);
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

    // Returns logged-in admin or null if failed
    public AdminResponseDto login() {

        System.out.println(cyan+"========== Admin Login ==========");
        System.out.print(yellow+"Enter Username : ");
        String fullName = scanner.nextLine().trim();

        System.out.print(yellow+"Enter Password : ");
        String password = scanner.nextLine().trim();

        AdminLoginRequestDto requestDto = new AdminLoginRequestDto(fullName, password);

        AdminResponseDto loggedInAdmin = adminService.login(requestDto);

        if (loggedInAdmin != null) {
            System.out.println(purple+"---------------------------------");
            System.out.println(purple+"Admin ID : " + loggedInAdmin.getAdminId());
            System.out.println(purple+"Full Name: " + loggedInAdmin.getFullName());
            System.out.println(purple+"=================================");
        }

        return loggedInAdmin;
    }
}