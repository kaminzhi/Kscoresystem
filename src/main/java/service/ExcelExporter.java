package service;

import model.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExcelExporter {

    // Method to export grade data to Excel
    public static void exportToExcel(GradeManager gradeManager) throws IOException {
        // Create the output directory in lowercase if it doesn't exist
        File outputDir = new File("output");
        if (!outputDir.exists()) {
            outputDir.mkdir();
        }

        // Create a new Excel workbook
        Workbook workbook = new XSSFWorkbook();
        DecimalFormat decimalFormat = new DecimalFormat("#.##"); // Format for 2 decimal places

        // Iterate over each class and create a sheet for that class
        for (String className : gradeManager.getClassNames()) {
            Sheet sheet = workbook.createSheet(className);

            // Create header row for each class
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Rank");
            headerRow.createCell(1).setCellValue("Student Name");
            headerRow.createCell(2).setCellValue("Student ID");

            // Add columns for each subject dynamically
            int subjectColumnStart = 3;  // Start placing subjects from column 3
            List<Student> students = gradeManager.getStudents(className); // Get students for the class
            if (!students.isEmpty()) {
                Student firstStudent = students.get(0);
                int subjectIndex = 0;
                for (Subject subject : firstStudent.getSubjects()) {
                    headerRow.createCell(subjectColumnStart + subjectIndex).setCellValue(subject.getName());
                    subjectIndex++;
                }
            }

            // Add columns for weighted average
            int columnOffset = subjectColumnStart + students.get(0).getSubjects().size();
            headerRow.createCell(columnOffset).setCellValue("Weighted Average");

            // Fill in student data for each class
            int rowIndex = 1;
            for (Student student : students) {
                Row row = sheet.createRow(rowIndex++);

                // Rank
                row.createCell(0).setCellValue(student.getRank());

                // Student Name and ID
                row.createCell(1).setCellValue(student.getName());
                row.createCell(2).setCellValue(student.getId());

                // Subjects
                int subjectIndex = 0;
                for (Subject subject : student.getSubjects()) {
                    row.createCell(subjectColumnStart + subjectIndex).setCellValue(subject.getScore());
                    subjectIndex++;
                }

                // Weighted Average (formatted to 2 decimal places)
                row.createCell(columnOffset).setCellValue(Double.parseDouble(decimalFormat.format(student.calculateAverageScore())));
            }

            // Auto-size the columns to fit content
            for (int i = 0; i < columnOffset + 1; i++) {
                sheet.autoSizeColumn(i);
            }
        }

        // Generate a filename based on the current timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "output/Report_" + timestamp + ".xlsx";

        // Write the output to a file
        try (FileOutputStream fileOut = new FileOutputStream(new File(fileName))) {
            workbook.write(fileOut);
        } finally {
            workbook.close();
        }

        System.out.println("Excel file '" + fileName + "' has been created.");
    }
}
