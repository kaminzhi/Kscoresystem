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
            List<Map<String, Object>> studentData = loadStudentsFromJSON("students.json");
            for (Map<String, Object> studentMap : studentData) {
                Student student = parseStudent(studentMap);
                gradeManager.addStudent(student);
            }

            // 輸出成績報告
            System.out.println(gradeManager.generateClassReport());

            // 輸出前第一名學生
            System.out.println("第1名學生:");
            List<Student> topStudents = gradeManager.getTopStudents(1);
            for (Student student : topStudents) {
                System.out.println(student.getName() + ": " +
                        student.calculateAverageScore());
            }

        } catch (Exception e) {
            System.err.println("錯誤: " + e.getMessage());
        }
    }

    // 從 JSON 檔案讀取資料
    private static List<Map<String, Object>> loadStudentsFromJSON(String filePath) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(new File(filePath), List.class);
    }

    // 將 JSON 資料解析為 Student 物件
    private static Student parseStudent(Map<String, Object> studentMap) {
        String name = (String) studentMap.get("name");
        String id = (String) studentMap.get("id");
        String className = (String) studentMap.get("class");

        Student student = new Student(name, id, className);

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
