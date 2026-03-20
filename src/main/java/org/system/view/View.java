
package org.system.view;

import org.nocrala.tools.texttablefmt.BorderStyle;
import org.nocrala.tools.texttablefmt.CellStyle;
import org.nocrala.tools.texttablefmt.Table;
import org.system.model.dao.EnrollmentDao;
import org.system.model.dto.request.EnrollmentRequestDto;
import org.system.model.dto.response.*;
import org.system.util.Pagination;

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

    // ══════════════════════════════════════════════════════════════════════════
    // COURSE
    // ══════════════════════════════════════════════════════════════════════════

    /**
     * Renders ONE page of courses (called by the paginator).
     * Unchanged rendering logic — only the list it receives is a sub-list.
     */
    public static void printCourseTable(List<CourseResponseDto> courses) {

        if (courses.isEmpty()) {
            System.out.println(red + "No courses found.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(15, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        // ── headers ──
        table.addCell(green        + "ID");
        table.addCell(blue         + "Course Name");
        table.addCell(yellow       + "Price ($)");
        table.addCell(brightYellow + "Discount (%)");
        table.addCell(brightGreen  + "After Discount ($)");
        table.addCell(purple       + "Credit");
        table.addCell(red          + "Cap");
        table.addCell(cyan         + "Start");
        table.addCell(brightGreen  + "End");
        table.addCell(brightBlue   + "Instructor ID");
        table.addCell(brightYellow + "Room");
        table.addCell(brightPurple + "Created");
        table.addCell(brightCyan   + "Major ID");
        table.addCell(green        + "Major Name");
        table.addCell(blue         + "Level");

        // ── rows ──
        for (CourseResponseDto c : courses) {
            table.addCell(green        + c.getCourse_id());
            table.addCell(blue         + c.getCourse_name());
            table.addCell(yellow       + String.format("%.2f", c.getPrice()));
            table.addCell(brightYellow + String.format("%.0f%%", c.getDiscount()));
            table.addCell(brightGreen  + String.format("%.2f", c.getPrice_after_discount()));
            table.addCell(purple       + c.getCredit_score());
            table.addCell(red          + c.getCapacity());
            table.addCell(cyan         + (c.getStart_date() != null ? c.getStart_date().toString() : ""));
            table.addCell(brightGreen  + (c.getEnd_date()   != null ? c.getEnd_date().toString()   : ""));
            table.addCell(brightBlue   + c.getInstructor_id());
            table.addCell(brightYellow + (c.getRoom()       != null ? c.getRoom()                  : ""));
            table.addCell(brightPurple + (c.getCreated_at() != null ? c.getCreated_at().toString() : ""));
            table.addCell(brightCyan   + c.getMajor_id());
            table.addCell(green        + c.getMajor_name());
            table.addCell(blue         + c.getLevel());
        }
        System.out.println(table.render());
    }

    /**
     * Paginated wrapper — call this from services that show ALL courses.
     */
    public static void printCourseTablePaginated(List<CourseResponseDto> courses) {
        Pagination.paginate(courses, View::printCourseTable);
    }

    // ──────────────────────────────────────────────────────────────────────────

    public static void printSingleCourseTable(List<CourseResponseDto> courses) {

        if (courses.isEmpty()) {
            System.out.println(red + "No courses found.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(13, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        if (!courses.isEmpty()) {
            table.addCell("Major ID: " + courses.get(0).getMajor_id(),
                    new CellStyle(CellStyle.HorizontalAlign.center), 1);
            table.addCell(courses.get(0).getMajor_name(),
                    new CellStyle(CellStyle.HorizontalAlign.center), 12);
        }

        table.addCell(purple       + "Course ID");
        table.addCell(green        + "Course Name");
        table.addCell(blue         + "Price ($)");
        table.addCell(brightYellow + "Discount (%)");
        table.addCell(brightGreen  + "After Discount ($)");
        table.addCell(red          + "Credit");
        table.addCell(cyan         + "Capacity");
        table.addCell(purple       + "Start");
        table.addCell(green        + "End");
        table.addCell(blue         + "Instructor ID");
        table.addCell(red          + "Room");
        table.addCell(cyan         + "Created");
        table.addCell(purple       + "Level");

        for (CourseResponseDto c : courses) {
            table.addCell(String.valueOf(c.getCourse_id()));
            table.addCell(c.getCourse_name());
            table.addCell(String.format("%.2f", c.getPrice()));
            table.addCell(String.format("%.0f%%", c.getDiscount()));
            table.addCell(String.format("%.2f", c.getPrice_after_discount()));
            table.addCell(String.valueOf(c.getCredit_score()));
            table.addCell(String.valueOf(c.getCapacity()));
            table.addCell(c.getStart_date() != null ? c.getStart_date().toString() : "");
            table.addCell(c.getEnd_date()   != null ? c.getEnd_date().toString()   : "");
            table.addCell(String.valueOf(c.getInstructor_id()));
            table.addCell(c.getRoom()       != null ? c.getRoom()                  : "");
            table.addCell(c.getCreated_at() != null ? c.getCreated_at().toString() : "");
            table.addCell(String.valueOf(c.getLevel()));
        }

        System.out.println(table.render());
    }

    public static void printSingleCourseTablePaginated(List<CourseResponseDto> courses) {
        Pagination.paginate(courses, View::printSingleCourseTable);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ROADMAP
    // ══════════════════════════════════════════════════════════════════════════

    public static void printRoadmapTable(List<RoadmapResponseDto> roadmapList) {
        if (roadmapList == null || roadmapList.isEmpty()) {
            System.out.println("No roadmap data to display.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(11, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(cyan + "Road Map", new CellStyle(CellStyle.HorizontalAlign.center), 11);

        table.addCell(yellow       + "Roadmap ID",         new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(green        + "Major ID",           new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(red          + "Major",              new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(blue         + "Course ID",          new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(purple       + "Course Name",        new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightYellow + "Subject ID",         new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightGreen  + "Subject Name",       new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightRed    + "Price ($)",          new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightYellow + "Discount (%)",       new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightGreen  + "After Discount ($)", new CellStyle(CellStyle.HorizontalAlign.center));
        table.addCell(brightBlue   + "Hour",               new CellStyle(CellStyle.HorizontalAlign.center));

        String  lastMajor   = null;
        String  lastCourse  = null;
        Integer lastMajorId = null;

        for (RoadmapResponseDto r : roadmapList) {

            boolean sameMajorId = lastMajorId != null && r.getMajor_id().equals(lastMajorId);
            boolean sameMajor   = r.getMajor_name().equals(lastMajor);
            boolean sameCourse  = sameMajor && String.valueOf(r.getCourse_id()).equals(lastCourse);

            table.addCell(String.valueOf(r.getRoadmap_id()),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(sameMajorId ? "" : String.valueOf(r.getMajor_id()),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(sameMajor ? "" : r.getMajor_name(),
                    new CellStyle(CellStyle.HorizontalAlign.left));
            table.addCell(sameCourse ? "" : String.valueOf(r.getCourse_id()),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(sameCourse ? "" : r.getCourse_name(),
                    new CellStyle(CellStyle.HorizontalAlign.left));
            table.addCell(String.valueOf(r.getSub_id()),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(r.getSub_name(),
                    new CellStyle(CellStyle.HorizontalAlign.left));
            table.addCell(sameCourse ? "" : String.format("%.2f", r.getPrice()),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(sameCourse ? "" : String.format("%.0f%%", r.getDiscount()),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(sameCourse ? "" : String.format("%.2f", r.getPrice_after_discount()),
                    new CellStyle(CellStyle.HorizontalAlign.center));
            table.addCell(String.valueOf(r.getHour()),
                    new CellStyle(CellStyle.HorizontalAlign.center));

            lastMajorId = r.getMajor_id();
            lastMajor   = r.getMajor_name();
            lastCourse  = String.valueOf(r.getCourse_id());
        }

        System.out.println(table.render());
    }

    public static void printRoadmapTablePaginated(List<RoadmapResponseDto> roadmapList) {
        Pagination.paginate(roadmapList, View::printRoadmapTable);
    }

    // ──────────────────────────────────────────────────────────────────────────

    public static void printSingleRoadmapTable(List<RoadmapResponseDto> roadmapList) {
        if (roadmapList == null || roadmapList.isEmpty()) {
            System.out.println(reset + "No roadmap data to display.");
            return;
        }

        final String RESET = "\u001B[0m";

        int    totalWidth = 60;
        String majorName  = roadmapList.get(0).getMajor_name();

        Map<String, List<RoadmapResponseDto>> grouped = new LinkedHashMap<>();
        for (RoadmapResponseDto r : roadmapList) {
            grouped.computeIfAbsent(String.valueOf(r.getCourse_id()), k -> new ArrayList<>()).add(r);
        }

        printTopBorder(totalWidth, cyan);
        printRow(cyan + majorName + RESET, totalWidth, true, cyan);
        printBottomBorder(totalWidth, cyan);
        System.out.println();

        for (List<RoadmapResponseDto> group : grouped.values()) {
            RoadmapResponseDto first = group.get(0);

            int col1  = totalWidth / 3;
            int col2  = totalWidth - col1 - 1;
            int w     = totalWidth / 3;
            int wLast = totalWidth - 2 * w - 2;

            printTopBorder(totalWidth, purple);

            printTwoColRow(
                    yellow + "Level: " + first.getLevel() + RESET,
                    yellow + first.getCourse_name() + RESET,
                    col1, col2, purple
            );
            printMidBorderTwoCol(totalWidth, col1, purple);

            printRow(green + "Subjects:" + RESET, totalWidth, false, purple);

            String[] subjectColors = {white, blue};
            int colorIdx = 0;
            for (RoadmapResponseDto r : group) {
                String color = subjectColors[colorIdx % 2];
                printRow(color + "- " + r.getSub_name() + RESET, totalWidth, false, purple);
                colorIdx++;
            }

            printMidBorderThreeCol(totalWidth, w, w, purple);

            printThreeColRow(
                    red    + "Capacity: " + first.getCapacity() + RESET,
                    green  + "Hour: "     + first.getHour()     + RESET,
                    yellow + "Price: $"   + String.format("%.2f", first.getPrice()) + RESET,
                    w, w, wLast, purple
            );

            printMidBorderThreeCol(totalWidth, w, w, purple);

            printThreeColRow(
                    "",
                    brightYellow + "Discount: " + String.format("%.0f%%", first.getDiscount()) + RESET,
                    brightGreen  + "Final: $"   + String.format("%.2f", first.getPrice_after_discount()) + RESET,
                    w, w, wLast, purple
            );

            printBottomBorder(totalWidth, purple);
            System.out.println();
        }
    }

    public static void printSingleRoadmapTablePaginated(List<RoadmapResponseDto> roadmapList) {
        Pagination.paginate(roadmapList, View::printSingleRoadmapTable);
    }

    // ── Border helpers ───────────────────────────────────────────────────────

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
        String stripped = text.replaceAll("\u001B\\[[;\\d]*m", "");
        String content;
        if (center) {
            int pad = (width - stripped.length()) / 2;
            content = " ".repeat(Math.max(0, pad)) + text;
        } else {
            content = " " + text;
        }
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

    // ══════════════════════════════════════════════════════════════════════════
    // SUBJECT
    // ══════════════════════════════════════════════════════════════════════════

    public static void printSubjectTable(List<SubjectResponseDto> subject) {
        if (subject.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        System.out.println(reset);
        Table table = new Table(5, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(green  + "Subject ID");
        table.addCell(red    + "Subject Name");
        table.addCell(blue   + "Description");
        table.addCell(purple + "Hours");
        table.addCell(cyan   + "Course Id");

        for (SubjectResponseDto s : subject) {
            table.addCell(green  + String.valueOf(s.getSub_id()));
            table.addCell(red    + String.valueOf(s.getSub_name()));
            table.addCell(blue   + String.valueOf(s.getDescription()));
            table.addCell(purple + String.valueOf(s.getHour()));
            table.addCell(cyan   + String.valueOf(s.getCourseId()));
        }
        System.out.println(table.render());
    }

    public static void printSubjectTablePaginated(List<SubjectResponseDto> subject) {
        Pagination.paginate(subject, View::printSubjectTable);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // INSTRUCTOR
    // ══════════════════════════════════════════════════════════════════════════

    public static void printInstructorTable(List<InstructorResponseDto> instructors) {
        if (instructors.isEmpty()) {
            System.out.println("No instructors found.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(8, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(blue   + "ID");
        table.addCell(purple + "Name");
        table.addCell(green  + "Gender");
        table.addCell(cyan   + "Age");
        table.addCell(red    + "Email");
        table.addCell(blue   + "Phone");
        table.addCell(purple + "Address");
        table.addCell(green  + "Qualification");

        for (InstructorResponseDto i : instructors) {
            table.addCell(blue   + String.valueOf(i.getInstructor_id()));
            table.addCell(purple + i.getInstructor_name());
            table.addCell(green  + i.getGender() != null ? i.getGender() : "-");
            table.addCell(cyan   + String.valueOf(i.getAge()));
            table.addCell(red    + i.getEmail());
            table.addCell(blue   + i.getPhone_number() != null ? i.getPhone_number() : "-");
            table.addCell(purple + i.getAddress() != null ? i.getAddress() : "-");
            table.addCell(green  + i.getQualification() != null ? i.getQualification() : "-");
        }

        System.out.println(table.render());
    }

    public static void printInstructorTablePaginated(List<InstructorResponseDto> instructors) {
        Pagination.paginate(instructors, View::printInstructorTable);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // MAJOR
    // ══════════════════════════════════════════════════════════════════════════

    public static void printMajorTable(List<MajorResponseDto> majorResponseDto) {
        if (majorResponseDto.isEmpty()) {
            System.out.println("No Major found.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(3, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(green  + "Major ID");
        table.addCell(purple + "Major Name");
        table.addCell(blue   + "Description");

        for (MajorResponseDto m : majorResponseDto) {
            table.addCell(green  + String.valueOf(m.getMajor_id()));
            table.addCell(purple + m.getMajor_name());
            table.addCell(blue   + m.getDescription());
        }

        System.out.println(table.render());
    }

    public static void printMajorTablePaginated(List<MajorResponseDto> majorResponseDto) {
        Pagination.paginate(majorResponseDto, View::printMajorTable);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // COURSE TIME
    // ══════════════════════════════════════════════════════════════════════════

    public static void printCourseTimeTable(List<CourseTimeResponseDto> courseTime) {
        if (courseTime.isEmpty()) {
            System.out.println(red + "No course time found.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(6, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(green  + "Course Time ID");
        table.addCell(blue   + "Course ID");
        table.addCell(yellow + "Day of Week ");
        table.addCell(purple + "Morning Time");
        table.addCell(red    + "Afternoon Time");
        table.addCell(cyan   + "Evening Time");

        for (CourseTimeResponseDto c : courseTime) {
            table.addCell(String.valueOf(c.getTime_id()));
            table.addCell(String.valueOf(c.getCourse_id()));
            table.addCell(c.getDay_of_week());
            table.addCell(c.getMorning());
            table.addCell(c.getAfternoon());
            table.addCell(c.getEvening());
        }

        System.out.println(table.render());
    }

    public static void printCourseTimeTablePaginated(List<CourseTimeResponseDto> courseTime) {
        Pagination.paginate(courseTime, View::printCourseTimeTable);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // TRANSCRIPT
    // ══════════════════════════════════════════════════════════════════════════

    public static void printTranscriptTable(List<TranscriptResponseDto> list) {

        System.out.println(reset);

        final int W  = 90;
        final int C1 = 40;
        final int C2 = 7;
        final int C3 = 13;
        final int C4 = W - C1 - C2 - C3 - 5;

        final String[] rowColors = { green, cyan, yellow, purple, brightCyan, brightGreen, brightYellow };

        final String H  = blue + "─";
        final String V  = blue + "│";
        final String TL = blue + "┌", TR = blue + "┐";
        final String BL = blue + "└", BR = blue + "┘";
        final String ML = blue + "├", MR = blue + "┤";
        final String TT = blue + "┬", CT = blue + "┼";

        java.util.function.Function<Object, String> safe = val -> val != null ? val.toString() : "-";
        java.util.function.Function<Integer, String> str = val -> String.valueOf(val);
        java.util.function.BiFunction<String, Integer, String> truncate = (s, maxLen) -> {
            if (s == null) return "-";
            return s.length() <= maxLen ? s : s.substring(0, maxLen - 1) + "…";
        };

        final int[] rowIdx = {0};

        java.util.function.BiConsumer<String, String> printLabelRow = (label, value) -> {
            String rc      = rowColors[rowIdx[0] % rowColors.length];
            rowIdx[0]++;

            String plain   = String.format("  %-16s: %s", label, value);
            int fill       = W - 2 - plain.length();
            if (fill < 0) {
                plain = plain.substring(0, W - 5) + "...";
                fill  = 0;
            }
            String colored = String.format("  " + white + "%-16s" + reset + ": " + rc + "%s" + reset, label, value);
            System.out.println(V + colored + " ".repeat(fill) + V);
        };

        for (TranscriptResponseDto t : list) {

            rowIdx[0] = 0;

            System.out.println();
            System.out.println(TL + H.repeat(W - 2) + TR + reset);

            String titleText = "TRANSCRIPT";
            int leftPad      = (W - 2 - titleText.length()) / 2;
            int rightPad     = W - 2 - leftPad - titleText.length();
            System.out.println(V + " ".repeat(leftPad) + brightWhite + titleText + reset + " ".repeat(rightPad) + V);

            System.out.println(ML + H.repeat(W - 2) + MR + reset);

            printLabelRow.accept("Transcript ID", str.apply(t.getTranscriptId()));
            printLabelRow.accept("ID",            str.apply(t.getStudentId()));
            printLabelRow.accept("Full Name",     safe.apply(t.getStudentName()));
            printLabelRow.accept("Gender",        safe.apply(t.getGender()));
            printLabelRow.accept("Date of Birth", safe.apply(t.getDateOfBirth()));

            System.out.println(ML + H.repeat(C1) + TT + H.repeat(C2) + TT + H.repeat(C3) + TT + H.repeat(C4) + MR + reset);

            System.out.printf(
                    V + " " + brightWhite + "%-" + (C1 - 2) + "s" + reset + " " + V +
                            " " + brightWhite + "%-" + (C2 - 2) + "s" + reset + " " + V +
                            " " + brightWhite + "%-" + (C3 - 2) + "s" + reset + " " + V +
                            " " + brightWhite + "%-" + (C4 - 2) + "s" + reset + " " + V + "%n",
                    "Course", "Grade", "Grade Point", "Result Status"
            );

            System.out.println(ML + H.repeat(C1) + CT + H.repeat(C2) + CT + H.repeat(C3) + CT + H.repeat(C4) + MR + reset);

            String courseIdCell   = truncate.apply("ID   : " + t.getCourseId(),               C1 - 2);
            String courseNameCell = truncate.apply("Name : " + safe.apply(t.getCourseName()), C1 - 2);
            String gradeCell      = truncate.apply(safe.apply(t.getGrade()),                  C2 - 2);
            String gradePointCell = truncate.apply(String.format("%.2f", t.getGrandePoint()), C3 - 2);
            String resultCell     = truncate.apply(safe.apply(t.getResultStatus()),           C4 - 2);

            System.out.printf(
                    V + " " + green  + "%-" + (C1 - 2) + "s" + reset + " " + V +
                            " " + cyan   + "%-" + (C2 - 2) + "s" + reset + " " + V +
                            " " + yellow + "%-" + (C3 - 2) + "s" + reset + " " + V +
                            " " + purple + "%-" + (C4 - 2) + "s" + reset + " " + V + "%n",
                    courseIdCell, gradeCell, gradePointCell, resultCell
            );

            System.out.printf(
                    V + " " + brightGreen + "%-" + (C1 - 2) + "s" + reset + " " + V +
                            " " + "%-" + (C2 - 2) + "s" + " " + V +
                            " " + "%-" + (C3 - 2) + "s" + " " + V +
                            " " + "%-" + (C4 - 2) + "s" + " " + V + "%n",
                    courseNameCell, "", "", ""
            );

            System.out.println(ML + H.repeat(W - 2) + MR + reset);

            printLabelRow.accept("Completion Date", safe.apply(t.getCompletionDate()));
            printLabelRow.accept("Remark",          safe.apply(t.getRemarks()));

            System.out.println(BL + H.repeat(W - 2) + BR + reset);
            System.out.println();
        }
    }

    public static void printTranscriptTablePaginated(List<TranscriptResponseDto> list) {
        Pagination.paginate(list, View::printTranscriptTable);
    }

    // ══════════════════════════════════════════════════════════════════════════
    // ENROLLMENT
    // ══════════════════════════════════════════════════════════════════════════

    public static void printEnrollmentTable(List<EnrollmentRequestDto> enrollmentRequestDtos) {
        if (enrollmentRequestDtos.isEmpty()) {
            System.out.println(red + "No Enrollment found.");
            return;
        }

        System.out.println(reset);
        Table table = new Table(4, BorderStyle.UNICODE_BOX_DOUBLE_BORDER);

        table.addCell(green  + "Enrollment Id");
        table.addCell(purple + "Student Id");
        table.addCell(blue   + "Course Id");
        table.addCell(cyan   + "Shift");

        for (EnrollmentRequestDto m : enrollmentRequestDtos) {
            table.addCell(String.valueOf(green  + m.getEnrollment_id()));
            table.addCell(String.valueOf(purple + m.getStudent_id()));
            table.addCell(String.valueOf(blue   + m.getCourse_id()));
            table.addCell(String.valueOf(cyan   + m.getShift()));
        }

        System.out.println(table.render());
    }

    public static void printEnrollmentTablePaginated(List<EnrollmentRequestDto> enrollmentRequestDtos) {
        Pagination.paginate(enrollmentRequestDtos, View::printEnrollmentTable);
    }
}
