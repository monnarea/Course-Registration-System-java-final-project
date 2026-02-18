package org.system.model.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class Students {

    private Integer id;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String email;
    private String major;
    private Integer academicYear;
    private LocalDate enrollmentDate;

    private Double gpa;
    private Integer totalCredits;
    private String status;
    private Long telegramChatId;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private List<Course> completedCourses;

//    public Student(String fullName, String email, String major) {
//        this.fullName = fullName;
//        this.email = email;
//        this.major = major;
//        this.enrollmentDate = LocalDate.now();
//        this.gpa = 0.0;
//        this.totalCredits = 0;
//        this.status = "Active";
//        this.academicYear = getAcademicYear();
//        this.createdAt = LocalDate.now();
//        this.updatedAt = LocalDate.now();
//    }

    public enum Gender {
        MALE , FEMALE
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(status);
    }

    public boolean hasTelegramConnected() {
        return telegramChatId != null && telegramChatId > 0;
    }

    public boolean canEnrollInCourse(int courseCredits) {
        return isActive() && courseCredits > 0;
    }

    public String getAcademicYear() {
        if (academicYear == null) return "Unknown";

        switch (academicYear) {
            case 1: return "1st Year";
            case 2: return "2nd Year";
            case 3: return "3rd Year";
            case 4: return "4th Year";
            default: return academicYear + "th Year";
        }
    }

    public String getFormattedGpa() {
        if (gpa == null) return "N/A";
        return String.format("%.2f", gpa);
    }



}
