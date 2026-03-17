
package org.system.model.dao;

import org.system.model.dto.response.TranscriptResponseDto;

import java.util.List;

public interface TranscriptDao {

    boolean insert(TranscriptResponseDto transcript);

    List<TranscriptResponseDto> findAll();

    List<TranscriptResponseDto> findById(int id);

//    TranscriptResponseDto findById(int id);

    boolean update(TranscriptResponseDto transcript);

    boolean delete(int id);
}
