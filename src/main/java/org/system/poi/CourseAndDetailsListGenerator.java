package org.system.poi;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CourseAndDetailsListGenerator {
    private CourseAndDetailsListGenerator() {}

    // ======= Change these to match your database =======
    private static final String URL      = "jdbc:postgresql://localhost:5432/student";
    private static final String USER     = "postgres";
    private static final String PASSWORD = "128028";
    // ====================================================

    private static final String QUERY = """
            SELECT
                c.course_id,
                c.course_name,
                c.price,
                c.description,
                c.credit_score,
                c.capacity,
                c.start_date::text,
                c.end_date::text,
                c.instructor_id,
                c.room,
                c.major_id,
                c.level,
                ct.day_of_week,
                ct.morning,
                ct.afternoon,
                ct.evening
            FROM course c
            LEFT JOIN course_time ct ON c.course_id = ct.course_id
            ORDER BY c.course_id
            """;

    public static List<CourseAndDetails> get() {
        List<CourseAndDetails> list = new ArrayList<>();

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(QUERY);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                CourseAndDetails course = new CourseAndDetails(
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getDouble("price"),
                        rs.getString("description"),
                        rs.getInt("credit_score"),
                        rs.getInt("capacity"),
                        rs.getString("start_date"),
                        rs.getString("end_date"),
                        rs.getInt("instructor_id"),
                        rs.getString("room"),
                        rs.getInt("major_id"),
                        rs.getInt("level"),
                        rs.getString("day_of_week"),
                        rs.getString("morning"),
                        rs.getString("afternoon"),
                        rs.getString("evening")
                );
                list.add(course);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}