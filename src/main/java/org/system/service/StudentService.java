package org.system.service;

import org.system.config.DatabaseConfig;
import org.system.model.dto.request.StudentRequestDto;

import java.sql.*;
import java.util.*;

public class StudentService {

    private final Scanner sc = new Scanner(System.in);

    // ─────────────────────────────────────────────────────────────────
    // CREATE — returns the generated student_id so the bot can use it
    // ─────────────────────────────────────────────────────────────────
    public int createStudent() {
        StudentRequestDto student = new StudentRequestDto();

        System.out.print("Enter Name         : ");
        student.setStudent_name(sc.nextLine());

        System.out.print("Enter Gender (Male/Female): ");
        student.setGender(sc.nextLine());

        System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
        student.setDate_of_birth(sc.nextLine());

        // ── Email with duplicate check loop ───────────────────────────
        while (true) {
            System.out.print("Enter Email        : ");
            String email = sc.nextLine().trim();

            if (isEmailTaken(email)) {
                System.out.println("❌ Email \"" + email + "\" is already registered. Please use a different email.");
                continue; // ask again
            }
            student.setEmail(email);
            break;
        }
        // ─────────────────────────────────────────────────────────────

        System.out.print("Enter Phone Number : ");
        student.setPhone_number(sc.nextLine());

        System.out.print("Enter Address      : ");
        student.setAddress(sc.nextLine());

        System.out.print("Enter Semester     : ");
        student.setSemester(sc.nextLine());

        System.out.print("Enter Year         : ");
        student.setYear(sc.nextLine());

        System.out.print("Enter University   : ");
        student.setUniversity(sc.nextLine());

        // RETURNING id → gets the auto-generated PK back from PostgreSQL
        String sql = "INSERT INTO student " +
                "(student_name, gender, date_of_birth, email, phone_number, address, semester, year, university) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, student.getStudent_name());
            ps.setString(2, student.getGender());
            ps.setDate  (3, java.sql.Date.valueOf(student.getDate_of_birth()));
            ps.setString(4, student.getEmail());
            ps.setString(5, student.getPhone_number());
            ps.setString(6, student.getAddress());
            ps.setString(7, student.getSemester());
            ps.setString(8, student.getYear());
            ps.setString(9, student.getUniversity());

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int studentId = rs.getInt("id");
                System.out.println("✅ Student created successfully! Your Student ID: " + studentId);
                return studentId;
            }

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid date format! Use yyyy-MM-dd (e.g. 2001-05-12).");
        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    // ── Check if email already exists in the student table ────────────────────
    private boolean isEmailTaken(String email) {
        String sql = "SELECT 1 FROM student WHERE email = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            return rs.next(); // true = email found = taken

        } catch (Exception e) {
            System.out.println("❌ Error checking email: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // READ ALL
    // ─────────────────────────────────────────────────────────────────
    public List<StudentRequestDto> getAllStudents() {
        List<StudentRequestDto> students = new ArrayList<>();

        String sql = "SELECT id, student_name, gender, date_of_birth, email, phone_number, " +
                "address, semester, year, university FROM student ORDER BY id";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                StudentRequestDto s = new StudentRequestDto();
                s.setStudent_id  (rs.getInt   ("id"));
                s.setStudent_name(rs.getString ("student_name"));
                s.setGender      (rs.getString ("gender"));
                s.setDate_of_birth(rs.getString("date_of_birth"));
                s.setEmail       (rs.getString ("email"));
                s.setPhone_number(rs.getString ("phone_number"));
                s.setAddress     (rs.getString ("address"));
                s.setSemester    (rs.getString ("semester"));
                s.setYear        (rs.getString ("year"));
                s.setUniversity  (rs.getString ("university"));
                students.add(s);
            }

            if (students.isEmpty()) {
                System.out.println("⚠️ No students found.");
            } else {
                System.out.println("\n┌────┬──────────────────────┬─────────┬──────────────┬───────────────────────────────┬──────────────┬──────────────────────┬──────────┬──────┬────────────────────┐");
                System.out.printf ("│ %-2s │ %-20s │ %-7s │ %-12s │ %-29s │ %-12s │ %-20s │ %-8s │ %-4s │ %-18s │%n",
                        "ID", "Name", "Gender", "DOB", "Email", "Phone", "Address", "Semester", "Year", "University");
                System.out.println("├────┼──────────────────────┼─────────┼──────────────┼───────────────────────────────┼──────────────┼──────────────────────┼──────────┼──────┼────────────────────┤");

                for (StudentRequestDto s : students) {
                    System.out.printf("│ %-2s │ %-20s │ %-7s │ %-12s │ %-29s │ %-12s │ %-20s │ %-8s │ %-4s │ %-18s │%n",
                            s.getStudent_id(),
                            truncate(s.getStudent_name(), 20),
                            truncate(s.getGender(), 7),
                            s.getDate_of_birth() != null ? s.getDate_of_birth() : "N/A",
                            truncate(s.getEmail(), 29),
                            truncate(s.getPhone_number(), 12),
                            truncate(s.getAddress(), 20),
                            truncate(s.getSemester(), 8),
                            s.getYear() != null ? s.getYear() : "N/A",
                            truncate(s.getUniversity(), 18)
                    );
                }
                System.out.println("└────┴──────────────────────┴─────────┴──────────────┴───────────────────────────────┴──────────────┴──────────────────────┴──────────┴──────┴────────────────────┘");
                System.out.println("Total records: " + students.size());
            }

        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return students;
    }

    // ─────────────────────────────────────────────────────────────────
    // READ BY ID
    // ─────────────────────────────────────────────────────────────────
    public boolean getStudentById(int id) {
        String sql = "SELECT * FROM student WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("\n🔍 Student Found:");
                System.out.println("  ID           : " + rs.getInt   ("id"));
                System.out.println("  Name         : " + rs.getString("student_name"));
                System.out.println("  Gender       : " + rs.getString("gender"));
                System.out.println("  Date of Birth: " + rs.getString("date_of_birth"));
                System.out.println("  Email        : " + rs.getString("email"));
                System.out.println("  Phone        : " + rs.getString("phone_number"));
                System.out.println("  Address      : " + rs.getString("address"));
                System.out.println("  Semester     : " + rs.getString("semester"));
                System.out.println("  Year         : " + rs.getString("year"));
                System.out.println("  University   : " + rs.getString("university"));
                return true;
            } else {
                System.out.println("⚠️ No student found with ID: " + id);
                return false;
            }
        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // UPDATE
    // ─────────────────────────────────────────────────────────────────
    public void updateStudent(int id) {
        if (!getStudentById(id)) return;
        System.out.println("\n✏️  Enter new details:");

        StudentRequestDto updated = new StudentRequestDto();
        System.out.print("New Name          : "); updated.setStudent_name(sc.nextLine());
        System.out.print("New Gender        : "); updated.setGender(sc.nextLine());
        System.out.print("New Date of Birth : "); updated.setDate_of_birth(sc.nextLine());

        // ── Email with duplicate check (allow same email as current student) ──
        while (true) {
            System.out.print("New Email         : ");
            String email = sc.nextLine().trim();

            // Check if taken by a DIFFERENT student
            if (isEmailTakenByOther(email, id)) {
                System.out.println("❌ Email \"" + email + "\" is already used by another student. Please use a different email.");
                continue;
            }
            updated.setEmail(email);
            break;
        }
        // ─────────────────────────────────────────────────────────────

        System.out.print("New Phone Number  : "); updated.setPhone_number(sc.nextLine());
        System.out.print("New Address       : "); updated.setAddress(sc.nextLine());
        System.out.print("New Semester      : "); updated.setSemester(sc.nextLine());
        System.out.print("New Year          : "); updated.setYear(sc.nextLine());
        System.out.print("New University    : "); updated.setUniversity(sc.nextLine());

        String sql = "UPDATE student SET student_name=?, gender=?, date_of_birth=?, email=?, " +
                "phone_number=?, address=?, semester=?, year=?, university=? WHERE id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, updated.getStudent_name());
            ps.setString(2, updated.getGender());
            ps.setDate  (3, java.sql.Date.valueOf(updated.getDate_of_birth()));
            ps.setString(4, updated.getEmail());
            ps.setString(5, updated.getPhone_number());
            ps.setString(6, updated.getAddress());
            ps.setString(7, updated.getSemester());
            ps.setString(8, updated.getYear());
            ps.setString(9, updated.getUniversity());
            ps.setInt   (10, id);

            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "✅ Student updated successfully!" : "⚠️ Update failed.");

        } catch (IllegalArgumentException e) {
            System.out.println("❌ Invalid date format! Use yyyy-MM-dd.");
        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ── Check if email is taken by a DIFFERENT student (used in update) ───────
    private boolean isEmailTakenByOther(String email, int currentStudentId) {
        String sql = "SELECT 1 FROM student WHERE email = ? AND id != ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setInt   (2, currentStudentId);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            System.out.println("❌ Error checking email: " + e.getMessage());
            return false;
        }
    }

    // ─────────────────────────────────────────────────────────────────
    // DELETE
    // ─────────────────────────────────────────────────────────────────
    public void deleteStudent(int id) {
        if (!getStudentById(id)) return;
        System.out.print("⚠️ Delete student ID " + id + "? (yes/no): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("yes")) {
            System.out.println("🚫 Delete cancelled.");
            return;
        }
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            System.out.println(rows > 0 ? "✅ Student deleted successfully!" : "⚠️ Delete failed.");

        } catch (Exception e) {
            System.out.println("❌ Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // ── HELPER: truncate long text ────────────────────────────────────────────
    private String truncate(String value, int maxLength) {
        if (value == null) return "N/A";
        return value.length() <= maxLength ? value : value.substring(0, maxLength - 1) + "…";
    }
}