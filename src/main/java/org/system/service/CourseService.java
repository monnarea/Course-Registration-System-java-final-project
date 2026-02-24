package org.system.service;

import org.system.model.dao.CourseDaoImpl;
import org.system.model.dto.response.CourseResponseDto;

import java.sql.SQLException;
import java.util.List;

import static org.system.view.View.printCourseTable;
import static org.system.view.View.printSingleCourseTable;

public class CourseService {

    private final CourseDaoImpl courseDao = new CourseDaoImpl();
    private final CourseDaoImpl singleCourseDao = new CourseDaoImpl();

    public void displayAllCourse(){

        try {
            List<CourseResponseDto> allCourses = courseDao.getAll();

            if (allCourses.isEmpty()) {
                System.out.println("No courses found in database.");
            } else {
                System.out.println("Total courses found: " + allCourses.size());

                printCourseTable(allCourses);

            }
        } catch (SQLException e) {
            System.err.println("getAll() failed: " + e.getMessage());
        }

        System.out.println();

    }

    public void displaySingleCourse(int course_id){
                try {
                    List<CourseResponseDto> result = singleCourseDao.getByMajorId(course_id);// change 1 to any ID

                    if (result.isEmpty()) {
                        System.out.println("No course found.");
                    } else {
                        printSingleCourseTable(result); // reuse same printTable method
                    }
                } catch (SQLException e) {
                    System.err.println("getById() failed: " + e.getMessage());
                }


    }



}
