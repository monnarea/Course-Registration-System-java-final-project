package org.system.service;

import org.system.model.dao.TranscriptDao;
import org.system.model.dao.TranscriptDaoImpl;
import org.system.model.dto.response.TranscriptResponseDto;
import org.system.view.View;
import org.system.util.Pagination;

import java.util.List;

public class TranscriptService {

    private final TranscriptDao transcriptDao = new TranscriptDaoImpl();

    public boolean createTranscript(TranscriptResponseDto transcript) {
        if (transcript == null) {
            System.err.println("[TranscriptService] Cannot create a null transcript.");
            return false;
        }
        boolean result = transcriptDao.insert(transcript);
        if (result) {
            View.printTranscriptTable(List.of(transcript));
        }
        return result;
    }

    public List<TranscriptResponseDto> getAllTranscripts() {
        List<TranscriptResponseDto> list = transcriptDao.findAll();
        if (list != null && !list.isEmpty()) {
            View.printTranscriptTablePaginated(list);
        }
        return list;
    }

    public List<TranscriptResponseDto> getTranscriptById(int id) {
        if (id <= 0) {
            System.err.println("[TranscriptService] Invalid transcript id: " + id);
            return List.of();
        }
        List<TranscriptResponseDto> list = transcriptDao.findById(id);
        if (list != null && !list.isEmpty()) {
            View.printTranscriptTable(list);
        }
        return list;
    }

    public boolean updateTranscript(TranscriptResponseDto transcript) {
        if (transcript == null || transcript.getTranscriptId() <= 0) {
            System.err.println("[TranscriptService] Cannot update: null or missing id.");
            return false;
        }
        boolean result = transcriptDao.update(transcript);
        if (result) {
            View.printTranscriptTable(List.of(transcript));
        }
        return result;
    }

    public boolean deleteTranscript(int id) {
        if (id <= 0) {
            System.err.println("[TranscriptService] Invalid transcript id for delete: " + id);
            return false;
        }
        return transcriptDao.delete(id);
    }
}