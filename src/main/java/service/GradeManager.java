package service;

import model.Student;
import java.util.*;

public class GradeManager {
    private Map<String, List<Student>> classStudentsMap;

    public GradeManager() {
        this.classStudentsMap = new HashMap<>();
    }

    // Add student to the specified class
    public void addStudent(String className, Student student) {
        if (student == null || className == null || className.isEmpty()) {
            throw new IllegalArgumentException("Class name and student cannot be null");
        }

        if (!classStudentsMap.containsKey(className)) {
            classStudentsMap.put(className, new ArrayList<>());
        }

        List<Student> students = classStudentsMap.get(className);

        // Check for duplicate student IDs
        if (students.stream().anyMatch(s -> s.getId().equals(student.getId()))) {
            throw new IllegalArgumentException("Student ID already exists in class " + className);
        }

        students.add(student);
        updateRankings(className);
    }

    // Update rankings within a specific class
    private void updateRankings(String className) {
        List<Student> students = classStudentsMap.get(className);
        if (students == null || students.isEmpty()) {
            return;
        }

        students.sort((s1, s2) ->
                Double.compare(s2.calculateAverageScore(), s1.calculateAverageScore()));

        for (int i = 0; i < students.size(); i++) {
            students.get(i).setRank(i + 1);
        }
    }

    // Generate report for a specific class
    public String generateClassReport(String className) {
        List<Student> students = classStudentsMap.get(className);
        if (students == null || students.isEmpty()) {
            return "No students registered in class " + className + ".\n";
        }

        StringBuilder report = new StringBuilder();
        report.append("-----------------------------------\n");
        report.append("Class Performance Summary Table - ").append(className).append("\n");

        // Calculate class average
        double classAverage = students.stream()
                .mapToDouble(Student::calculateAverageScore)
                .average()
                .orElse(0.0);

        report.append(String.format("Class Total Member: %d\n", students.size()));
        report.append(String.format("Class Average: %.1f\n", classAverage));
        report.append("-----------------------------------\n\n");

        // Generate individual student reports
        for (Student student : students) {
            report.append(student.generateReport());
            report.append("\n");
        }
        report.append("====================================\n");

        return report.toString();
    }

    // Get top n students from a specific class
    public List<Student> getTopStudents(String className, int n) {
        List<Student> students = classStudentsMap.get(className);
        if (students == null || students.isEmpty()) {
            throw new IllegalArgumentException("No students found in class " + className);
        }
        return new ArrayList<>(students.subList(0, Math.min(n, students.size())));
    }

    // Get all students in a specific class
    public List<Student> getStudents(String className) {
        List<Student> students = classStudentsMap.get(className);
        if (students == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(students);
    }

    // Find student by ID within a specific class
    public Student findStudentById(String className, String id) {
        List<Student> students = classStudentsMap.get(className);
        if (students == null) {
            return null;
        }
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<String> getClassNames() {
        return new ArrayList<>(classStudentsMap.keySet());
    }

}
