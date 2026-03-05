package org.system.model.dao;

import org.system.config.DatabaseConfig;
import org.system.model.dto.response.AdminResponseDto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AdminDaoImpl implements AdminDao {

    @Override
    public Optional<AdminResponseDto> findByFullName(String fullName) throws SQLException {

        String sql = "SELECT admin_id, full_name FROM admin WHERE full_name = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, fullName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    AdminResponseDto dto = new AdminResponseDto(
                            rs.getInt("admin_id"),
                            rs.getString("full_name")
                    );
                    return Optional.of(dto);
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error finding admin: " + fullName, e);
        }

        return Optional.empty();
    }

    @Override
    public String getPasswordHashByFullName(String fullName) throws SQLException {

        String sql = "SELECT password_hash FROM admin WHERE full_name = ?";

        try (Connection connection = DatabaseConfig.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, fullName);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("password_hash");
                }
            }

        } catch (SQLException e) {
            throw new SQLException("Error fetching password hash for: " + fullName, e);
        }

        return null;
    }
}
