package org.system.service;

import org.system.model.dao.AdminDao;
import org.system.model.dao.AdminDaoImpl;
import org.system.model.dto.request.AdminLoginRequestDto;
import org.system.model.dto.response.AdminResponseDto;
import org.system.util.PasswordUtil;

import java.sql.SQLException;
import java.util.Optional;

public class AdminService {

    private final AdminDao adminDao = new AdminDaoImpl();
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";

    public AdminResponseDto login(AdminLoginRequestDto requestDto) {

        try {
            // Step 1: Check if admin exists by full_name
            Optional<AdminResponseDto> adminOpt = adminDao.findByFullName(requestDto.getFullName());

            if (adminOpt.isEmpty()) {
                System.out.println(red+"Login failed: username not found.");
                return null;
            }

            // Step 2: Fetch the stored hash
            String storedHash = adminDao.getPasswordHashByFullName(requestDto.getFullName());

            // Step 3: Verify password
            if (storedHash == null || !PasswordUtil.verifyPassword(requestDto.getPasswordHast(), storedHash)) {
                System.out.println(red+"Login failed: incorrect password.");
                return null;
            }

            // Step 4: Return admin info on success
            System.out.println(green+"Login successful! Welcome, " + adminOpt.get().getFullName());
            return adminOpt.get();

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
