package org.system.view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.RoadmapResponseDto;

import java.text.StringCharacterIterator;
import java.util.List;

public class View {
    public static void printCourseTable(List<CourseResponseDto> courses) {

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        // Create table with 11 columns (like your header)
        Table table = new Table(13, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Price");
        table.addCell("Credit");
        table.addCell("Cap");
        table.addCell("Start");
        table.addCell("End");
        table.addCell("Instructor");
        table.addCell("Room");
        table.addCell("Created");
        table.addCell("Major Id");
        table.addCell("Major Name");
        table.addCell("Level");

        for (CourseResponseDto c : courses) {
            table.addCell(String.valueOf(c.getCourse_id()));
            table.addCell(c.getCourse_name());
            table.addCell(String.valueOf(c.getPrice()));
            table.addCell(String.valueOf(c.getCredit_score()));
            table.addCell(String.valueOf(c.getCapacity()));
            table.addCell(c.getStart_date() != null ? c.getStart_date().toString() : "");
            table.addCell(c.getEnd_date() != null ? c.getEnd_date().toString() : "");
            table.addCell(String.valueOf(c.getInstructor_id()));
            table.addCell(c.getRoom() != null ? c.getRoom() : "");
            table.addCell(c.getCreated_at() != null ? c.getCreated_at().toString() : "");
            table.addCell(String.valueOf(c.getMajor_id()));
            table.addCell(String.valueOf(c.getMajor_name()));
            table.addCell(String.valueOf(c.getLevel()));
        }

        // Print table
        System.out.println(table.render());
    }

    public static void printSingleCourseTable(List<CourseResponseDto> courses) {

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        // 11 columns
        Table table = new Table(11, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // First row: Major ID (1 col) + Major Name (10 cols)
        if (!courses.isEmpty()) {
            table.addCell("Major ID: " + courses.get(0).getMajor_id(),
                    new CellStyle(CellStyle.HorizontalAlign.center), 1);
            table.addCell(courses.get(0).getMajor_name(),
                    new CellStyle(CellStyle.HorizontalAlign.center), 10);
        }

        // Headers
        table.addCell("ID");
        table.addCell("Name");
        table.addCell("Price");
        table.addCell("Credit");
        table.addCell("Cap");
        table.addCell("Start");
        table.addCell("End");
        table.addCell("Instructor");
        table.addCell("Room");
        table.addCell("Created");
        table.addCell("Level");

        for (CourseResponseDto c : courses) {
            table.addCell(String.valueOf(c.getCourse_id()));
            table.addCell(c.getCourse_name());
            table.addCell(String.valueOf(c.getPrice()));
            table.addCell(String.valueOf(c.getCredit_score()));
            table.addCell(String.valueOf(c.getCapacity()));
            table.addCell(c.getStart_date() != null ? c.getStart_date().toString() : "");
            table.addCell(c.getEnd_date() != null ? c.getEnd_date().toString() : "");
            table.addCell(String.valueOf(c.getInstructor_id()));
            table.addCell(c.getRoom() != null ? c.getRoom() : "");
            table.addCell(c.getCreated_at() != null ? c.getCreated_at().toString() : "");
            table.addCell(String.valueOf(c.getLevel()));
        }

        System.out.println(table.render());
    }

    public static void printRoadmapTable(List<RoadmapResponseDto> roadmapList){

        Table table = new Table(7, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell("Road map", new CellStyle(CellStyle.HorizontalAlign.center), 7);
        table.addCell("Major",new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Course ID",new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Course Name",new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Subject ID",new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Subject Name",new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Price",new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Hour",new CellStyle(CellStyle.HorizontalAlign.center));
        // new column
        for (RoadmapResponseDto roadmap : roadmapList) {
            table.addCell(roadmap.getMajor_name());
         table.addCell(String.valueOf(roadmap.getCourse_id()));
         table.addCell(roadmap.getCourse_name());
         table.addCell(String.valueOf(roadmap.getSub_id()));
         table.addCell(roadmap.getSub_name());
         table.addCell(String.valueOf(roadmap.getPrice()));
         table.addCell(String.valueOf(roadmap.getHour()));
        }

        System.out.println(table.render());
    }

    public static void printSingleRoadmapTable(List<RoadmapResponseDto> roadmapList) {
        if (roadmapList == null || roadmapList.isEmpty()) {
            System.out.println("No roadmap data to display.");
            return;
        }

        // The table has 6 columns (Course ID, Course Name, Subject ID, Subject Name, Price, Hour)
        int columns = 7;
        Table table = new Table(columns, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // Get the major name from the first element (all same major)
        String majorName = roadmapList.get(0).getMajor_name();

        // Add major name as a centered header spanning all columns
        table.addCell(majorName, new CellStyle(CellStyle.HorizontalAlign.center), columns);

        // Add column headers
        table.addCell("Level", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Course ID", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Course Name", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Subject ID", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Subject Name", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Price", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Hour", new CellStyle(CellStyle.HorizontalAlign.center));

        // Add rows
        for (RoadmapResponseDto roadmap : roadmapList) {
            table.addCell(String.valueOf(roadmap.getLevel()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(roadmap.getCourse_id()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(roadmap.getCourse_name(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(roadmap.getSub_id()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(roadmap.getSub_name(), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(roadmap.getPrice()), new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(roadmap.getHour()), new CellStyle(CellStyle.HorizontalAlign.center));
        }

        // Print the table
        System.out.println(table.render());
    }



}
