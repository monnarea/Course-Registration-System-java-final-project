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
        DatabaseConfig ConnectionUtil = null;
        this.connection = ConnectionUtil.getConnection();
    }

    // ================= INSERT =================
    @Override
    public void insert(StudentResponseDto student) {
        String sql = """
                INSERT INTO student
                (student_name, gender, age, email, phone_number, score, address, semester, year)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, student.getStudent_name());
            ps.setString(2, student.getGender());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getEmail());
            ps.setInt(5, student.getPhone_number());
            ps.setInt(6, student.getScore());
            ps.setString(7, student.getAddress());
            ps.setString(8, student.getSemester());
            ps.setInt(9, student.getYear());

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
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getInt("phone_number"),
                        rs.getInt("score"),
                        rs.getString("address"),
                        rs.getString("semester"),
                        rs.getInt("year")
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

        String sql = "SELECT * FROM student WHERE student_id = ?";
        StudentResponseDto student = null;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    student = new StudentResponseDto(
                            rs.getInt("student_id"),
                            rs.getString("student_name"),
                            rs.getString("gender"),
                            rs.getInt("age"),
                            rs.getString("email"),
                            rs.getInt("phone_number"),
                            rs.getInt("score"),
                            rs.getString("address"),
                            rs.getString("semester"),
                            rs.getInt("year")
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
                SET student_name = ?, gender = ?, age = ?, email = ?,
                    phone_number = ?, score = ?, address = ?, semester = ?, year = ?
                WHERE student_id = ?
                """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, student.getStudent_name());
            ps.setString(2, student.getGender());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getEmail());
            ps.setInt(5, student.getPhone_number());
            ps.setInt(6, student.getScore());
            ps.setString(7, student.getAddress());
            ps.setString(8, student.getSemester());
            ps.setInt(9, student.getYear());
            ps.setInt(10, student.getStudent_id());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // ================= DELETE =================
    @Override
    public void delete(Integer id) {

        String sql = "DELETE FROM student WHERE student_id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
