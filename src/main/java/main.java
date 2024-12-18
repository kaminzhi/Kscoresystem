import model.*;
import service.GradeManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.util.List;
import java.util.Map;

public class main {
    public static void main(String[] args) {
        try {
            // 創建成績管理系統
            GradeManager gradeManager = new GradeManager();

            // 從 JSON 檔案匯入學生資料
            Map<String, List<Map<String, Object>>> classData = loadClassesFromJSON("students.json");
            for (String className : classData.keySet()) {
                List<Map<String, Object>> studentData = classData.get(className);
                for (Map<String, Object> studentMap : studentData) {
                    Student student = parseStudent(studentMap, className);
                    gradeManager.addStudent(className, student);
                }
            }

            // 輸出所有班級的成績報告
            for (String className : gradeManager.getClassNames()) {
                System.out.println(gradeManager.generateClassReport(className));
            }

            // 輸出每個班級的第一名學生
            for (String className : gradeManager.getClassNames()) {
                System.out.println("Class " + className + " 1st:");
                List<Student> topStudents = gradeManager.getTopStudents(className, 1);
                for (Student student : topStudents) {
                    System.out.println(student.getName() + ": " + student.calculateAverageScore() + "(Weighted Average)");
                }
            }

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // 從 JSON 檔案讀取資料
    private static Map<String, List<Map<String, Object>>> loadClassesFromJSON(String filePath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), Map.class);
    }

    // 將 JSON 資料解析為 Student 物件，並將班級名稱加入
    private static Student parseStudent(Map<String, Object> studentMap, String className) {
        String name = (String) studentMap.get("name");
        String id = (String) studentMap.get("id");

        Student student = new Student(name, id, className);  // 確保 Student 構造方法正確

        List<Map<String, Object>> subjects = (List<Map<String, Object>>) studentMap.get("subjects");
        for (Map<String, Object> subjectMap : subjects) {
            String subjectName = (String) subjectMap.get("name");
            String type = (String) subjectMap.get("type");
            int score = (int) subjectMap.get("score");

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
