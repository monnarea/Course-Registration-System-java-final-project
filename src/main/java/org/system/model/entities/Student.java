package org.system.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Student {

    private Integer id;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String email;
    private String major;
    private String academicYear;
    private LocalDate enrollmentDate;

    private Double gpa;
    private Integer totalCredits;
    private String status;
    private String telegramChatId;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private List<Course> completedCourses;

    public Student(String fullName, String email, String major) {
        this.fullName = fullName;
        this.email = email;
        this.major = major;
        this.enrollmentDate = LocalDate.now();
        this.gpa = 0.0;
        this.totalCredits = 0;
        this.status = "Active";
        this.academicYear = "Freshman";
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    public enum Gender {
        MALE , FEMALE
    }
}
