// src/main/java/service/GradeManager.java
package service;

import model.Student;
import java.util.*;

public class GradeManager {
    private List<Student> students;

    public GradeManager() {
        this.students = new ArrayList<>();
    }

    public void addStudent(Student student) {
        if (student == null) {
            throw new IllegalArgumentException("Student cannot be null");
        }
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new IllegalArgumentException("Student ID already exists");
        }

        students.add(student);
        updateRankings();
    }

    private void updateRankings() {
        students.sort((s1, s2) ->
                Double.compare(s2.calculateAverageScore(), s1.calculateAverageScore()));

        for (int i = 0; i < students.size(); i++) {
            students.get(i).setRank(i + 1);
        }
    }

    public String generateClassReport() {
        if (students.isEmpty()) {
            return "No students registered.\n";
        }

        StringBuilder report = new StringBuilder();
        report.append("班級成績總表\n");
        report.append("==============\n\n");

        // 班級統計信息
        double classAverage = students.stream()
                .mapToDouble(Student::calculateAverageScore)
                .average()
                .orElse(0.0);

        report.append(String.format("班級總人數: %d\n", students.size()));
        report.append(String.format("班級平均分: %.1f\n\n", classAverage));

        // 個人成績報告
        for (Student student : students) {
            report.append(student.generateReport());
            report.append("\n");
        }

        return report.toString();
    }

    public List<Student> getTopStudents(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Number must be positive");
        }
        return new ArrayList<>(students.subList(0, Math.min(n, students.size())));
    }

    public List<Student> getStudents() {
        return Collections.unmodifiableList(students);
    }

    public Student findStudentById(String id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
}