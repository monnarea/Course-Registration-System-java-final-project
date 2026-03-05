package org.system.view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.system.model.dto.response.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class View {
    public static final String green = "\u001B[32m";
    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[33m";
    public static final String purple = "\u001B[35m";
    public static final String red = "\u001B[31m";
    public static final String cyan = "\u001B[36m";
    public static final String white = "\u001B[37m";
    public static final String brightBlack  = "\u001B[90m";
    public static final String brightRed     = "\u001B[91m";
    public static final String brightGreen   = "\u001B[92m";
    public static final String brightYellow = "\u001B[93m";
    public static final String brightBlue    = "\u001B[94m";
    public static final String brightPurple = "\u001B[95m";
    public static final String brightCyan = "\u001B[96m";
    public static final String brightWhite = "\u001B[97m";
    public static final String reset = "\u001B[0m";



    public static void printCourseTable(List<CourseResponseDto> courses) {

        if (courses.isEmpty()) {
            System.out.println(red+"No courses found.");
            return;
        }

        // Create table with 11 columns (like your header)
        System.out.println(reset);
        Table table = new Table(13, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(green+"Course ID");
        table.addCell(blue+"Course Name");
        table.addCell(yellow+"Price");
        table.addCell(purple+"Credit");
        table.addCell(red+"Cap");
        table.addCell(cyan+"Start");
        table.addCell(brightGreen+"End");
        table.addCell(brightBlue+"Instructor Id");
        table.addCell(brightYellow+"Room");
        table.addCell(brightPurple+"Created");
        table.addCell(brightCyan+"Major Id");
        table.addCell(green+"Major Name");
        table.addCell(blue+"Level");

        for (CourseResponseDto c : courses) {
            table.addCell(green+String.valueOf(c.getCourse_id()));
            table.addCell(blue+c.getCourse_name());
            table.addCell(yellow+String.valueOf(c.getPrice()));
            table.addCell(purple+String.valueOf(c.getCredit_score()));
            table.addCell(red+String.valueOf(c.getCapacity()));
            table.addCell(cyan+c.getStart_date() != null ? cyan+c.getStart_date().toString() : "");
            table.addCell(brightGreen+c.getEnd_date() != null ? brightGreen+c.getEnd_date().toString() : "");
            table.addCell(brightBlue+String.valueOf(c.getInstructor_id()));
            table.addCell(brightYellow+c.getRoom() != null ? c.getRoom() : "");
            table.addCell(brightPurple+c.getCreated_at() != null ? c.getCreated_at().toString() : "");
            table.addCell(brightCyan+String.valueOf(c.getMajor_id()));
            table.addCell(green+String.valueOf(c.getMajor_name()));
            table.addCell(blue+String.valueOf(c.getLevel()));
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
        System.out.println(reset);
        Table table = new Table(11, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // First row: Major ID (1 col) + Major Name (10 cols)
        if (!courses.isEmpty()) {
            table.addCell("Major ID: " + courses.get(0).getMajor_id(),
                    new CellStyle(CellStyle.HorizontalAlign.center), 1);
            table.addCell(courses.get(0).getMajor_name(),
                    new CellStyle(CellStyle.HorizontalAlign.center), 10);
        }

        // Headers
        table.addCell(purple+"Course ID");
        table.addCell(green+"Course Name");
        table.addCell(blue+"Price");
        table.addCell(red+"Credit");
        table.addCell(cyan+"Capacity");
        table.addCell(purple+"Start");
        table.addCell(green+"End");
        table.addCell(blue+"Instructor Id");
        table.addCell(red+"Room");
        table.addCell(cyan+"Created");
        table.addCell(purple+"Level Course In Major");

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

        System.out.println(reset);
        Table table = new Table(9, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // Title spanning all columns
        table.addCell(cyan+"Road map", new CellStyle(CellStyle.HorizontalAlign.center), 9);

        // Headers
        table.addCell(yellow+"Roadmap Id",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(green+"Major Id",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(red+"Major",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(blue+"Course ID",    new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(purple+"Course Name",  new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightYellow+"Subject ID",   new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightGreen+"Subject Name", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightRed+"Price",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightBlue+"Hour",         new CellStyle(CellStyle.HorizontalAlign.center));

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
            System.out.println(reset+"No roadmap data to display.");
            return;
        }

        final String RESET = "\u001B[0m";

        int totalWidth = 60;
        String majorName = roadmapList.get(0).getMajor_name();

        Map<String, List<RoadmapResponseDto>> grouped = new LinkedHashMap<>();
        for (RoadmapResponseDto r : roadmapList) {
            grouped.computeIfAbsent(String.valueOf(r.getCourse_id()), k -> new ArrayList<>()).add(r);
        }

        // Major Name Header — cyan
        printTopBorder(totalWidth, cyan);
        printRow(cyan + majorName + RESET, totalWidth, true, cyan);
        printBottomBorder(totalWidth, cyan);
        System.out.println();

        for (List<RoadmapResponseDto> group : grouped.values()) {
            RoadmapResponseDto first = group.get(0);
            RoadmapResponseDto last  = group.get(group.size() - 1);

            int col1  = totalWidth / 3;
            int col2  = totalWidth - col1 - 1;
            int w     = totalWidth / 3;
            int wLast = totalWidth - 2 * w - 2;

            printTopBorder(totalWidth, purple);

            // Level | Course Name — yellow text
            printTwoColRow(
                    yellow + "Level: " + first.getLevel() + RESET,
                    yellow + first.getCourse_name() + RESET,
                    col1, col2, purple
            );
            printMidBorderTwoCol(totalWidth, col1, purple);

            // Subjects label — green
            printRow(green + "Subjects:" + RESET, totalWidth, false, purple);

            // Each subject — alternating white / blue
            String[] subjectColors = {white, blue};
            int colorIndex = 0;
            for (RoadmapResponseDto r : group) {
                String color = subjectColors[colorIndex % 2];
                printRow(color + "- " + r.getSub_name() + RESET, totalWidth, false, purple);
                colorIndex++;
            }

            printMidBorderThreeCol(totalWidth, w, w, purple);

            // Capacity | Hour | Price — red, green, yellow
            printThreeColRow(
                    red    + "Capacity: " + last.getCapacity() + RESET,
                    green  + "Hour: "     + last.getHour()     + RESET,
                    yellow + "Price: $"   + last.getPrice()    + RESET,
                    w, w, wLast, purple
            );

            printBottomBorder(totalWidth, purple);
            System.out.println();
        }
    }

// ── Border helpers ───────────────────────────────────────────

    private static void printTopBorder(int width, String borderColor) {
        System.out.println(borderColor + "╔" + "═".repeat(width) + "╗" + "\u001B[0m");
    }

    private static void printBottomBorder(int width, String borderColor) {
        System.out.println(borderColor + "╚" + "═".repeat(width) + "╝" + "\u001B[0m");
    }

    private static void printMidBorder(int width, String borderColor) {
        System.out.println(borderColor + "╠" + "═".repeat(width) + "╣" + "\u001B[0m");
    }

    private static void printMidBorderTwoCol(int totalWidth, int col1, String borderColor) {
        System.out.println(borderColor + "╠" + "═".repeat(col1) + "╪" + "═".repeat(totalWidth - col1 - 1) + "╣" + "\u001B[0m");
    }

    private static void printMidBorderThreeCol(int totalWidth, int w1, int w2, String borderColor) {
        System.out.println(borderColor + "╠" + "═".repeat(w1) + "╪" + "═".repeat(w2) + "╪" + "═".repeat(totalWidth - w1 - w2 - 2) + "╣" + "\u001B[0m");
    }

    private static void printRow(String text, int width, boolean center, String borderColor) {
        // Strip ANSI codes to calculate visible length for padding
        String stripped = text.replaceAll("\u001B\\[[;\\d]*m", "");
        String content;
        if (center) {
            int pad = (width - stripped.length()) / 2;
            content = " ".repeat(Math.max(0, pad)) + text;
        } else {
            content = " " + text;
        }
        // Pad based on stripped length, but print with color codes
        int visibleLen = stripped.length() + (center ? (width - stripped.length()) / 2 : 1);
        int padding = width - visibleLen;
        System.out.println(borderColor + "║" + "\u001B[0m" + content + " ".repeat(Math.max(0, padding)) + borderColor + "║" + "\u001B[0m");
    }

    private static void printTwoColRow(String left, String right, int col1, int col2, String borderColor) {
        String leftStripped  = left.replaceAll("\u001B\\[[;\\d]*m", "");
        String rightStripped = right.replaceAll("\u001B\\[[;\\d]*m", "");
        String l = " " + left  + " ".repeat(Math.max(0, col1 - leftStripped.length()  - 1));
        String r = " " + right + " ".repeat(Math.max(0, col2 - rightStripped.length() - 1));
        System.out.println(borderColor + "║" + "\u001B[0m" + l + borderColor + "║" + "\u001B[0m" + r + borderColor + "║" + "\u001B[0m");
    }


    private static void printThreeColRow(String c1, String c2, String c3, int w1, int w2, int w3, String borderColor) {
        String s1 = c1.replaceAll("\u001B\\[[;\\d]*m", "");
        String s2 = c2.replaceAll("\u001B\\[[;\\d]*m", "");
        String s3 = c3.replaceAll("\u001B\\[[;\\d]*m", "");
        String p1 = " " + c1 + " ".repeat(Math.max(0, w1 - s1.length() - 1));
        String p2 = " " + c2 + " ".repeat(Math.max(0, w2 - s2.length() - 1));
        String p3 = " " + c3 + " ".repeat(Math.max(0, w3 - s3.length() - 1));
        System.out.println(borderColor + "║" + "\u001B[0m" + p1 + borderColor + "║" + "\u001B[0m" + p2 + borderColor + "║" + "\u001B[0m" + p3 + borderColor + "║" + "\u001B[0m");
    }

    //    ---------------------------------------------------------------------------

    public static void printSubjectTable(List<SubjectResponseDto> subject){
        if (subject.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        System.out.println(reset);
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(green+"Subject ID");
        table.addCell(red+"Subject Name");
        table.addCell(blue+"Description");
        table.addCell(purple+"Hours");
        table.addCell(cyan+"Course Id");

        for (SubjectResponseDto s : subject){
            table.addCell(String.valueOf(s.getSub_id()));
            table.addCell(String.valueOf(s.getSub_name()));
            table.addCell(String.valueOf(s.getDescription()));
            table.addCell(String.valueOf(s.getHour()));
            table.addCell(String.valueOf(s.getCourseId()));
        }
        System.out.println(table.render());
    }

    public static void printSingleSubjectTable(List<SubjectResponseDto> subject){
        if (subject.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        System.out.println(reset);
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

    public static void printInstructorTable(List<InstructorResponseDto> instructors) {
        if (instructors.isEmpty()) {
            System.out.println("No instructors found.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(8, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // Headers
        table.addCell(blue+"ID");
        table.addCell(purple+"Name");
        table.addCell(green+"Gender");
        table.addCell(cyan+"Age");
        table.addCell(red+"Email");
        table.addCell(blue+"Phone");
        table.addCell(purple+"Address");
        table.addCell(green+"Qualification");

        // Rows
        for (InstructorResponseDto i : instructors) {
            table.addCell(String.valueOf(i.getInstructorId()));
            table.addCell(i.getInstructorName());
            table.addCell(i.getGender() != null ? i.getGender() : "-");
            table.addCell(String.valueOf(i.getAge()));
            table.addCell(i.getEmail());
            table.addCell(i.getPhoneNumber() != null ? i.getPhoneNumber() : "-");
            table.addCell(i.getAddress() != null ? i.getAddress() : "-");
            table.addCell(i.getQualification() != null ? i.getQualification() : "-");
        }

        System.out.println(table.render()); // ← Don't forget this!
    }

    public static void printMajorTable(List<MajorResponseDto> majorResponseDto) {
        if (majorResponseDto.isEmpty()) {
            System.out.println("No Major found.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // Headers
        table.addCell(green+"Major ID");
        table.addCell(purple+"Major Name");
        table.addCell(blue+"Description");

        // Rows
        for (MajorResponseDto m : majorResponseDto) {
            table.addCell(String.valueOf(m.getMajor_id()));
            table.addCell(m.getMajor_name());
            table.addCell(m.getDescription());
        }

        System.out.println(table.render());
    }

    public static void printCourseTimeTable(List<CourseTimeResponseDto> courseTime) {

        if (courseTime.isEmpty()) {
            System.out.println(red+"No course time found.");
            return;
        }

        // Create table with 11 columns (like your header)
        System.out.println(reset);
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(green+"Course Time ID");
        table.addCell(blue+"Course ID");
        table.addCell(yellow+"Day of Week ");
        table.addCell(purple+"Morning Time");
        table.addCell(red+"Afternoon Time");
        table.addCell(cyan+"Evening Time");

        for (CourseTimeResponseDto c : courseTime) {
            table.addCell(String.valueOf(c.getTime_id()));
            table.addCell(String.valueOf(c.getCourse_id()));
            table.addCell(c.getDay_of_week());
            table.addCell(c.getMorning());
            table.addCell(c.getAfternoon());
            table.addCell(c.getEvening());
        }

        // Print table
        System.out.println(table.render());
    }
}
