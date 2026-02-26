package org.system.service;

import org.system.model.dao.SubjectDao;
import org.system.model.dao.SubjectDaoImpl;
import org.system.model.dto.response.SubjectResponseDto;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import static org.system.view.View.printSubjectTable;

public class SubjectService {
    private final Scanner scanner= new Scanner(System.in);
    private  final SubjectDao subjectDao =new SubjectDaoImpl();
    // =========================================================
    //  CREATE
    // =========================================================
    public boolean createSubject(SubjectResponseDto subject) {
        if (subject == null) {
            System.err.println("[SERVICE - CREATE] Subject cannot be null.");
            return false;
        }
        if (subject.getSub_name() == null || subject.getSub_name().trim().isEmpty()) {
            System.err.println("[SERVICE - CREATE] Subject name cannot be empty.");
            return false;
        }
        if (subject.getHour() <= 0) {
            System.err.println("[SERVICE - CREATE] Hour must be greater than 0.");
            return false;
        }
        if (subject.getCourseId() <= 0) {
            System.err.println("[SERVICE - CREATE] Invalid course_id.");
            return false;
        }

        return subjectDao.createSubject(subject);
    }

    // =========================================================
    //  READ ALL
    // =========================================================
    public void getAllSubjects() {
        List<SubjectResponseDto> list = subjectDao.getAllSubjects();
        if (list.isEmpty()) {
            System.out.println("[SERVICE - READ ALL] No subjects found.");
            return;
        }
        printSubjectTable(list);
    }

    // =========================================================
    //  READ BY ID
    // =========================================================
    public void getSubjectById(int subId) {
        if (subId <= 0) {
            System.err.println("[SERVICE - READ BY ID] Invalid sub_id: " + subId);
            return;
        }
        List<SubjectResponseDto> list = Collections.singletonList(subjectDao.getSubjectById(subId));
        if (list.isEmpty()) {
            System.out.println("[SERVICE - READ BY ID] No subject found with sub_id = " + subId);
            return;
        }
        printSubjectTable(list);
    }

    // =========================================================
    //  READ BY COURSE ID
    // =========================================================
    public void getSubjectsByCourseId(int courseId) {
        if (courseId <= 0) {
            System.err.println("[SERVICE - READ BY COURSE] Invalid course_id: " + courseId);
            return;
        }
        List<SubjectResponseDto> list = subjectDao.getSubjectsByCourseId(courseId);
        if (list.isEmpty()) {
            System.out.println("[SERVICE - READ BY COURSE] No subjects found for course_id = " + courseId);
            return;
        }
        printSubjectTable(list);
    }

    // =========================================================
    //  UPDATE
    // =========================================================
    public boolean updateSubject(SubjectResponseDto subject) {
        if (subject == null) {
            System.err.println("[SERVICE - UPDATE] Subject cannot be null.");
            return false;
        }
        if (subject.getSub_id() <= 0) {
            System.err.println("[SERVICE - UPDATE] Invalid sub_id.");
            return false;
        }
        if (subject.getSub_name() == null || subject.getSub_name().trim().isEmpty()) {
            System.err.println("[SERVICE - UPDATE] Subject name cannot be empty.");
            return false;
        }
        if (subject.getHour() <= 0) {
            System.err.println("[SERVICE - UPDATE] Hour must be greater than 0.");
            return false;
        }

        return subjectDao.updateSubject(subject);
    }

    // =========================================================
    //  DELETE
    // =========================================================
    public boolean deleteSubject(int subId) {
        if (subId <= 0) {
            System.err.println("[SERVICE - DELETE] Invalid sub_id: " + subId);
            return false;
        }

        // Check subject exists before deleting
        List<SubjectResponseDto> existing = Collections.singletonList(subjectDao.getSubjectById(subId));
        if (existing.isEmpty()) {
            System.err.println("[SERVICE - DELETE] Subject not found with sub_id: " + subId);
            return false;
        }

        return subjectDao.deleteSubject(subId);
    }

}
