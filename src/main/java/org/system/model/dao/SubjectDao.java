package org.system.model.dao;

import org.system.model.dto.response.SubjectResponseDto;

import javax.security.auth.Subject;
import java.util.List;

public interface SubjectDao {
    // CREATE
    boolean createSubject(SubjectResponseDto subject);

    // READ ALL
    List<SubjectResponseDto> getAllSubjects();

    // READ BY ID
    SubjectResponseDto getSubjectById(int subId);

    // READ BY COURSE ID
    List<SubjectResponseDto> getSubjectsByCourseId(int courseId);

    // UPDATE
    boolean updateSubject(SubjectResponseDto subject);

    // DELETE
    boolean deleteSubject(int subId);
}
