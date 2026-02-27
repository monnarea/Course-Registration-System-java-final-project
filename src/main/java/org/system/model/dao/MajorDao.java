package org.system.model.dao;

import org.system.model.dto.request.MajorRequestDto;
import org.system.model.dto.response.MajorResponseDto;

import java.sql.SQLException;
import java.util.List;

public interface MajorDao {
    List<MajorResponseDto> getAll() throws SQLException;
    List<MajorResponseDto> getById(int majorId) throws SQLException;
    MajorResponseDto create(MajorRequestDto majorRequestDto) throws SQLException;
    MajorResponseDto update(int major_id, MajorRequestDto majorRequestDto) throws SQLException;
    boolean delete(int major_id) throws SQLException;
}
