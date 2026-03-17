package org.system.model.dao;

import org.system.model.dao.StudentDao;
import org.system.model.dto.response.StudentResponseDto;
import org.system.config.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StudentDaoImpl implements StudentDao {

    private final Connection connection;

    public StudentDaoImpl() {
        // FIX: was "DatabaseConfig ConnectionUtil = null" which causes NullPointerException
        DatabaseConfig connectionUtil = new DatabaseConfig();
        this.connection = connectionUtil.getConnection();
    }

    // ================= INSERT =================
    @Override
    public void insert(StudentResponseDto student) {
        String sql = """
                INSERT INTO public.student
                (student_name, gender, date_of_birth, email, phone_number, address, semester, year, university)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, student.getStudent_name());
            ps.setString(2, student.getGender());
            ps.setString(3, student.getDate_of_birth());  // FIX: was getAge()
            ps.setString(4, student.getEmail());
            ps.setInt(5, student.getPhone_number());       // FIX: was getScore()
            ps.setString(6, student.getAddress());         // FIX: was getScore()
            ps.setString(7, student.getSemester());        // FIX: was getAddress()
            ps.setInt(8, student.getYear());               // FIX: was getSemester()
            ps.setString(9, student.getUniversity());      // FIX: was getYear()

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= FIND ALL =================
    @Override
    public List<StudentResponseDto> findAll() {

        List<StudentResponseDto> students = new ArrayList<>();
        String sql = "SELECT * FROM student";

        try (Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            while (rs.next()) {
                StudentResponseDto student = new StudentResponseDto(
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getString("gender"),
                        rs.getString("date_of_birth"),
                        rs.getString("email"),
                        rs.getInt("phone_number"),
                        rs.getString("address"),
                        rs.getString("semester"),
                        rs.getInt("year"),
                        rs.getString("university")
                );

                students.add(student);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return students;
    }

    // ================= FIND BY ID =================
    @Override
    public StudentResponseDto findById(Integer id) {

        String sql = "SELECT * FROM student WHERE id = ?";
        StudentResponseDto student = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student = new StudentResponseDto(
                            rs.getInt("student_id"),
                            rs.getString("student_name"),
                            rs.getString("gender"),
                            rs.getString("date_of_birth"),
                            rs.getString("email"),
                            rs.getInt("phone_number"),
                            rs.getString("address"),
                            rs.getString("semester"),
                            rs.getInt("year"),
                            rs.getString("university")
                    );
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return student;
    }

    // ================= UPDATE =================
    @Override
    public void update(StudentResponseDto student) {

        String sql = """
                UPDATE student
                SET student_name = ?, gender = ?, date_of_birth = ?, email = ?,
                    phone_number = ?, address = ?, semester = ?, year = ?, university = ?
                WHERE id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, student.getStudent_name());
            ps.setString(2, student.getGender());
            ps.setString(3, student.getDate_of_birth());
            ps.setString(4, student.getEmail());
            ps.setInt(5, student.getPhone_number());
            ps.setString(6, student.getAddress());
            ps.setString(7, student.getSemester());
            ps.setInt(8, student.getYear());
            ps.setString(9, student.getUniversity());
            ps.setInt(10, student.getStudent_id());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM student WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}