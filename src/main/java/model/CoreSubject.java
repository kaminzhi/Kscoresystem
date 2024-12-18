package model;

public class CoreSubject extends Subject {
    public CoreSubject(String name) {
        super(name, 1.0);
    }

    @Override
    public double calculateWeightedScore() {
        return getScore() * getWeight();
    }
}

