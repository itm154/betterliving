package org.betterliving.controller;

import org.betterliving.model.reward.Rewardable;
import org.betterliving.model.user.Student;
import org.betterliving.repository.ScoreboardRepository;

import java.util.List;

public class ScoreboardController {
	private final ScoreboardRepository repository;

	public ScoreboardController(ScoreboardRepository repository) {
		this.repository = repository;
	}

	public List<Student> getAllScores() {
		List<Student> scores = repository.findAll();
		scores.sort(null);
		return scores;
	}

	public void saveScore(String name, int points) {
		saveScore(name, points, null);
	}

	public void saveScore(String name, int points, List<Rewardable> badges) {
		if (name == null || name.trim().isEmpty()) {
			return;
		}
		name = name.trim();
		Student student = repository.findByName(name);
		if (student == null) {
			student = new Student(name, 0);
		}
		student.addScore(points);
		if (badges != null) {
			for (Rewardable b : badges) {
				boolean exists = false;
				for (Rewardable existing : student.getAllBadges()) {
					if (existing.getName().equals(b.getName())) {
						exists = true;
						break;
					}
				}
				if (!exists) {
					student.addBadge(b);
				}
			}
		}
		repository.save(student);
	}

	public void deleteScore(int id) {
		repository.deleteById(id);
	}
}
