package model;
import java.util.List;


public abstract class Subject {
    private String name;
    private double score;
    private double weight;

    public Subject(String name, double weight) {
        this.name = name;
        this.weight = weight;
    }

    public void setScore(double score) {
        if (score < 0 || score > 100) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public double getScore() {
        return score;
    }

    public double getWeight() {
        return weight;
    }

    public abstract double calculateWeightedScore();


}