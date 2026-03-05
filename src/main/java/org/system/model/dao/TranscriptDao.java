package org.system.model.dao;

import org.system.model.dto.response.TranscriptResponseDto;

import java.util.List;

public interface TranscriptDao {

    void insert(TranscriptResponseDto transcript);

    List<TranscriptResponseDto> findAll();

    TranscriptResponseDto findById(int id);

    void update(TranscriptResponseDto transcript);

    void delete(int id);
}