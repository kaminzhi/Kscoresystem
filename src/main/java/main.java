import model.*;
import service.GradeManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Map;
import service.ExcelExporter;

public class main {
    public static void main(String[] args) {
        try {
            // Create the grade management system
            GradeManager gradeManager = new GradeManager();

            // Load student data from the JSON file
            Map<String, List<Map<String, Object>>> classData = loadClassesFromJSON("students.json");
            for (String className : classData.keySet()) {
                List<Map<String, Object>> studentData = classData.get(className);
                for (Map<String, Object> studentMap : studentData) {
                    Student student = parseStudent(studentMap, className);
                    gradeManager.addStudent(className, student);
                }
            }

            // Generate and print reports for all classes
            for (String className : gradeManager.getClassNames()) {
                System.out.println(gradeManager.generateClassReport(className));
            }

            // Print the top student from each class
            for (String className : gradeManager.getClassNames()) {
                System.out.println("Class " + className + " 1st:");
                List<Student> topStudents = gradeManager.getTopStudents(className, 1);
                for (Student student : topStudents) {
                    System.out.println(student.getName() + ": " +
                            student.calculateAverageScore() + " (Weighted Average)");
                }
            }

            // Optionally, export to Excel
            ExcelExporter.exportToExcel(gradeManager);

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Load class data from JSON file
    private static Map<String, List<Map<String, Object>>> loadClassesFromJSON(String filePath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), Map.class);
    }

    // Parse student data from JSON to Student object
    private static Student parseStudent(Map<String, Object> studentMap, String className) {
        String name = (String) studentMap.get("name");
        String id = (String) studentMap.get("id");

        Student student = new Student(name, id, className);

        // Assuming "subjects" is a list of subject data for the student
        List<Map<String, Object>> subjects = (List<Map<String, Object>>) studentMap.get("subjects");
        for (Map<String, Object> subjectMap : subjects) {
            String subjectName = (String) subjectMap.get("name");
            String type = (String) subjectMap.get("type");
            int score = (int) subjectMap.get("score");

            // Add subject based on type (core or elective)
            if ("core".equalsIgnoreCase(type)) {
                CoreSubject coreSubject = new CoreSubject(subjectName);
                coreSubject.setScore(score);
                student.addSubject(coreSubject);
            } else if ("elective".equalsIgnoreCase(type)) {
                double weight = (double) subjectMap.get("weight");
                ElectiveSubject electiveSubject = new ElectiveSubject(subjectName, weight);
                electiveSubject.setScore(score);
                student.addSubject(electiveSubject);
            }
        }
        return student;
    }
}
