package org.system.model.dao;

import org.system.model.dto.request.AdminLoginRequestDto;
import org.system.model.dto.response.AdminResponseDto;

import java.sql.SQLException;
import java.util.Optional;

public interface AdminDao {
    // Fetch admin row by full_name so we can verify password
    Optional<AdminResponseDto> findByFullName(String fullName) throws SQLException;

    // We also need the hash for verification — separate internal fetch
    String getPasswordHashByFullName(String fullName) throws SQLException;
}
