package org.betterliving.model.user;

import org.betterliving.model.reward.Rewardable;

import java.util.List;

public class Student extends User implements Comparable<Student> {

	private final List<Rewardable> badges;
	private int score;

	public Student(String name, int id) {
		super(name, id);
		this.score = 0;
		this.badges = new java.util.ArrayList<>();
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

	public void addBadge(Rewardable badge) {
		this.badges.add(badge);
	}

	public List<Rewardable> getAllBadges() {
		return badges;
	}

	@Override
	public int compareTo(Student other) {
		return Integer.compare(other.getScore(), this.getScore());
	}
}
