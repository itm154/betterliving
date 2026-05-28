package org.betterliving.model.user;

public class Student extends User {

    private int score;

    public Student(String name, int id) {
        super(name, id);
        this.score = 0;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        score += points;
    }

    public void resetScore() {
        score = 0;
    }

    @Override
    public String toString() {
        return "Student: " + name +
               ", ID: " + id +
               ", Score: " + score;
    }
}
