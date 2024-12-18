// src/main/java/model/ElectiveSubject.java
package model;

public class ElectiveSubject extends Subject {
    public ElectiveSubject(String name, double weight) {
        super(name, weight);
        if (weight <= 0 || weight > 1) {
            throw new IllegalArgumentException("Weight must be between 0 and 1");
        }
    }

    @Override
    public double calculateWeightedScore() {
        return getScore() * getWeight();
    }
}