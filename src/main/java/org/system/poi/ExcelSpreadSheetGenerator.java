package org.system.poi;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelSpreadSheetGenerator {
    private ExcelSpreadSheetGenerator() {}

    public static void execute() {
        // Create workbook
        Workbook workbook = new XSSFWorkbook();
        // Create sheet
        Sheet sheet = workbook.createSheet("Enrollment Course");

        // Header row
        generateHeaderRow(sheet, workbook);
        setColumnWidths(sheet);

        addCourseData(sheet,workbook);


        // Write to file
        writeFile(workbook);
    }

    private static void addCourseData(Sheet sheet , Workbook workbook) {
        List<CourseAndDetails> courseAndDetails = CourseAndDetailsListGenerator.get();
        for (int i = 0;i<courseAndDetails.size();i++){
            Row row = sheet.createRow(i+1);
            row.createCell( 0).setCellValue(courseAndDetails.get(i).getCourse_id());
            row.createCell(1).setCellValue(courseAndDetails.get(i).getCourse_name());
            row.createCell(2).setCellValue(courseAndDetails.get(i).getPrice());



            //Make wrap text on Description
            Cell descriptionCell = row.createCell(3);
            descriptionCell.setCellValue(courseAndDetails.get(i).getDescription());
            CellStyle wrapTextStyle = workbook.createCellStyle();
            wrapTextStyle.setWrapText(true);
            descriptionCell.setCellStyle(wrapTextStyle);



            row.createCell(4).setCellValue(courseAndDetails.get(i).getCredit_score());
            row.createCell(5).setCellValue(courseAndDetails.get(i).getCapacity());
            row.createCell(6).setCellValue(courseAndDetails.get(i).getStart_date());
            row.createCell(7).setCellValue(courseAndDetails.get(i).getEnd_date());
            row.createCell(8).setCellValue(courseAndDetails.get(i).getInstructor_id());
            row.createCell(9).setCellValue(courseAndDetails.get(i).getRoom());
            row.createCell(10).setCellValue(courseAndDetails.get(i).getMajor_id());
            row.createCell(11).setCellValue(courseAndDetails.get(i).getLevel());
            row.createCell(12).setCellValue(courseAndDetails.get(i).getDay_of_week());
            row.createCell(13).setCellValue(courseAndDetails.get(i).getMorning());
            row.createCell(14).setCellValue(courseAndDetails.get(i).getAfternoon());
            row.createCell(15).setCellValue(courseAndDetails.get(i).getEvening());

        }
    }

    private static void setColumnWidths(Sheet sheet) {

        sheet.setColumnWidth(0,5_000);
        sheet.setColumnWidth(1,5_000);
        sheet.setColumnWidth(2,5_000);
        sheet.setColumnWidth(3,5_000);
        sheet.setColumnWidth(4,5_000);
        sheet.setColumnWidth(5,5_000);
        sheet.setColumnWidth(6,5_000);
        sheet.setColumnWidth(7,5_000);
        sheet.setColumnWidth(8,5_000);
        sheet.setColumnWidth(9,5_000);
        sheet.setColumnWidth(10,5_000);
        sheet.setColumnWidth(11,5_000);
        sheet.setColumnWidth(12,5_000);
        sheet.setColumnWidth(13,5_000);
        sheet.setColumnWidth(14,5_000);
        sheet.setColumnWidth(15,5_000);
        sheet.setColumnWidth(16,5_000);


    }

    private static void generateHeaderRow(Sheet sheet, Workbook workbook) {
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Course-ID","Course","Price","Description","Credit-Score","Capacity",
                "Start-Date","End-Date","Instructor-ID","Room","Major-ID","Level","Day Of Week","Morning","Afternoon","Evening"
        };

        // Fill header row
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(getCellStyle(workbook));
        }
    }

    private static CellStyle getCellStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        Font boldFont = workbook.createFont();
        boldFont.setBold(true);//make the font bold
        headerStyle.setFont(boldFont);

        //sets cell to grey background
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        //horizontally align the text
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        return headerStyle;

    }

    private static void writeFile(Workbook workbook) {
        try (FileOutputStream fileOutputStream = new FileOutputStream("Course_Output.xlsx")) {
            workbook.write(fileOutputStream);
            // Close workbook after writing
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}