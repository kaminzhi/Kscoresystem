// src/main/java/model/Student.java
package model;

import java.util.*;

public class Student {
    private String name;
    private String id;
    private String className;
    private List<Subject> subjects;
    private int rank;

    public Student(String name, String id, String className) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }

        this.name = name;
        this.id = id;
        this.className = className;
        this.subjects = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        if (subject == null) {
            throw new IllegalArgumentException("Subject cannot be null");
        }
        subjects.add(subject);
    }

    public double calculateTotalScore() {
        return subjects.stream()
                .mapToDouble(Subject::calculateWeightedScore)
                .sum();
    }

    public double calculateAverageScore() {
        if (subjects.isEmpty()) {
            return 0.0;
        }

        double totalWeight = subjects.stream()
                .mapToDouble(Subject::getWeight)
                .sum();
        return calculateTotalScore() / totalWeight;
    }

    public void setRank(int rank) {
        if (rank < 1) {
            throw new IllegalArgumentException("Rank must be positive");
        }
        this.rank = rank;
    }

    public String generateReport() {
        StringBuilder report = new StringBuilder();
        report.append("Grade Report\n");
        report.append("==============\n");
        report.append(String.format("Name: %s\n", name));
        report.append(String.format("ID: %s\n", id));
        report.append(String.format("Class: %s\n", className));
        report.append("Subject Grade:\n");

        for (Subject subject : subjects) {
            report.append(String.format("- %s: %.1f (weight[權重]: %.1f)\n",
                    subject.getName(),
                    subject.getScore(),
                    subject.getWeight()));
        }

        report.append(String.format("Weighted Total Score(加權總分): %.1f\n", calculateTotalScore()));
        report.append(String.format("Weighted Average(加權平均): %.1f\n", calculateAverageScore()));
        report.append(String.format("Class Rank(班級排名): %d\n", rank));

        return report.toString();
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public List<Subject> getSubjects() {
        return Collections.unmodifiableList(subjects);
    }

    public int getRank() {
        return rank;
    }
}