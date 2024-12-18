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


    // 新增：從 JSON 資料設置科目
    public void addSubjectsFromJson(List<Map<String, Object>> subjectData) {
        for (Map<String, Object> subjectMap : subjectData) {
            String subjectName = (String) subjectMap.get("name");
            String type = (String) subjectMap.get("type");
            int score = (int) subjectMap.get("score");

            if ("core".equalsIgnoreCase(type)) {
                CoreSubject coreSubject = new CoreSubject(subjectName);
                coreSubject.setScore(score);
                subjects.add(coreSubject);
            } else if ("elective".equalsIgnoreCase(type)) {
                double weight = (double) subjectMap.get("weight");
                ElectiveSubject electiveSubject = new ElectiveSubject(subjectName, weight);
                electiveSubject.setScore(score);
                subjects.add(electiveSubject);
            }
        }
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
        report.append(String.format("Name: %s\n", name));
        report.append(String.format("ID: %s\n", id));
        report.append(String.format("Class: %s\n", className));
        report.append("Subject Grade:\n");

        // Loop through the subjects and output them
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

    public double calculateWeightedAverage() {
        if (subjects.isEmpty()) {
            return 0.0;
        }

        double totalScore = 0;
        double totalWeight = 0;

        for (Subject subject : subjects) {
            totalScore += subject.getScore() * subject.getWeight();
            totalWeight += subject.getWeight();
        }

        return totalWeight == 0 ? 0.0 : totalScore / totalWeight;
    }
}
