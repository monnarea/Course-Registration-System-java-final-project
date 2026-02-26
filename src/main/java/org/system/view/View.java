package org.system.view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.system.model.dto.response.CourseResponseDto;
import org.system.model.dto.response.RoadmapResponseDto;
import org.system.model.dto.response.SubjectResponseDto;

import java.util.List;

public class View {
    public static void printCourseTable(List<CourseResponseDto> courses) {

        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }

        // Create table with 11 columns (like your header)
        Table table = new Table(13, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell("Course ID");
        table.addCell("Course Name");
        table.addCell("Price");
        table.addCell("Credit");
        table.addCell("Cap");
        table.addCell("Start");
        table.addCell("End");
        table.addCell("Instructor Id");
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
        table.addCell("Course ID");
        table.addCell("Course Name");
        table.addCell("Price");
        table.addCell("Credit");
        table.addCell("Capacity");
        table.addCell("Start");
        table.addCell("End");
        table.addCell("Instructor Id");
        table.addCell("Room");
        table.addCell("Created");
        table.addCell("Level Course In Major");

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

    public static void printRoadmapTable(List<RoadmapResponseDto> roadmapList) {
        if (roadmapList == null || roadmapList.isEmpty()) {
            System.out.println("No roadmap data to display.");
            return;
        }

        Table table = new Table(9, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // Title spanning all columns
        table.addCell("Road map", new CellStyle(CellStyle.HorizontalAlign.center), 9);

        // Headers
        table.addCell("Roadmap Id",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Major Id",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Major",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Course ID",    new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Course Name",  new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Subject ID",   new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Subject Name", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Price",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Hour",         new CellStyle(CellStyle.HorizontalAlign.center));

        String lastMajor  = null;
        String lastCourse = null;
        Integer lastMajorId = null;

        for (RoadmapResponseDto roadmap : roadmapList) {

            boolean sameMajor  = roadmap.getMajor_name().equals(lastMajor);
            boolean sameCourse = sameMajor && String.valueOf(roadmap.getCourse_id()).equals(lastCourse);
            boolean sameMajorId = lastMajorId != null && roadmap.getMajor_id().equals(lastMajorId);
            table.addCell(String.valueOf(roadmap.getRoadmap_id()),   new CellStyle(CellStyle.HorizontalAlign.center));
//            table.addCell(String.valueOf(roadmap.getMajor_id()),   new CellStyle(CellStyle.HorizontalAlign.center));
            // Major: blank if same as previous row
            table.addCell(
                    sameMajorId ? "" : String.valueOf(roadmap.getMajor_id()),
                    new CellStyle(CellStyle.HorizontalAlign.center)
            );
            table.addCell(
                    sameMajor ? "" : roadmap.getMajor_name(),
                    new CellStyle(CellStyle.HorizontalAlign.left)
            );

            // Course ID: blank if same course
            table.addCell(
                    sameCourse ? "" : String.valueOf(roadmap.getCourse_id()),
                    new CellStyle(CellStyle.HorizontalAlign.center)
            );

            // Course Name: blank if same course
            table.addCell(
                    sameCourse ? "" : roadmap.getCourse_name(),
                    new CellStyle(CellStyle.HorizontalAlign.left)
            );

            // Subject columns: always display
            table.addCell(String.valueOf(roadmap.getSub_id()),   new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(roadmap.getSub_name(),                 new CellStyle(CellStyle.HorizontalAlign.left));
            table.addCell(String.valueOf(roadmap.getPrice()),    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(roadmap.getHour()),     new CellStyle(CellStyle.HorizontalAlign.center));

            // Update trackers
            lastMajor  = roadmap.getMajor_name();
            lastCourse = String.valueOf(roadmap.getCourse_id());
            lastMajorId = roadmap.getMajor_id();
        }

        System.out.println(table.render());
    }

    public static void printSingleRoadmapTable(List<RoadmapResponseDto> roadmapList) {
        if (roadmapList == null || roadmapList.isEmpty()) {
            System.out.println("No roadmap data to display.");
            return;
        }

        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // Major name as title
        String majorName = roadmapList.get(0).getMajor_name();
        table.addCell(majorName, new CellStyle(CellStyle.HorizontalAlign.center), 6);

        // Headers
        table.addCell("Level",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Course Name",  new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Subject ID",   new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Subject Name", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Price",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell("Hour",         new CellStyle(CellStyle.HorizontalAlign.center));

        String lastCourse = null;
        String lastLevel  = null;

        for (RoadmapResponseDto roadmap : roadmapList) {

            boolean sameCourse = String.valueOf(roadmap.getCourse_id()).equals(lastCourse);

            // Level: blank if same course
            table.addCell(
                    sameCourse ? "" : String.valueOf(roadmap.getLevel()),
                    new CellStyle(CellStyle.HorizontalAlign.center)
            );

            // Course Name: blank if same course
            table.addCell(
                    sameCourse ? "" : roadmap.getCourse_name(),
                    new CellStyle(CellStyle.HorizontalAlign.left)
            );

            // Subject columns: always display
            table.addCell(String.valueOf(roadmap.getSub_id()),  new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(roadmap.getSub_name(),                new CellStyle(CellStyle.HorizontalAlign.left));
            table.addCell(String.valueOf(roadmap.getPrice()),   new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(roadmap.getHour()),    new CellStyle(CellStyle.HorizontalAlign.center));

            lastCourse = String.valueOf(roadmap.getCourse_id());
        }

        System.out.println(table.render());
    }

    public static void printSubjectTable(List<SubjectResponseDto> subject){
        if (subject.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell("Subject ID");
        table.addCell("Subject Name");
        table.addCell("Description");
        table.addCell("Hours");
        table.addCell("Course Id");

        for (SubjectResponseDto s : subject){
            table.addCell(String.valueOf(s.getSub_id()));
            table.addCell(String.valueOf(s.getSub_name()));
            table.addCell(String.valueOf(s.getDescription()));
            table.addCell(String.valueOf(s.getHour()));
            table.addCell(String.valueOf(s.getCourseId()));
        }
    }

    public static void printSingleSubjectTable(List<SubjectResponseDto> subject){
        if (subject.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell("Subject ID");
        table.addCell("Subject Name");
        table.addCell("Description");
        table.addCell("Hours");
        table.addCell("Course Id");

        for (SubjectResponseDto s : subject){
            table.addCell(String.valueOf(s.getSub_id()));
            table.addCell(String.valueOf(s.getSub_name()));
            table.addCell(String.valueOf(s.getDescription()));
            table.addCell(String.valueOf(s.getHour()));
            table.addCell(String.valueOf(s.getCourseId()));
        }
    }



}
