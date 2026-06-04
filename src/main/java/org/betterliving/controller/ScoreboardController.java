package org.betterliving.controller;

import org.betterliving.model.user.Student;
import org.betterliving.repository.ScoreboardRepository;
import java.util.List;

public class ScoreboardController {
	private final ScoreboardRepository repository;

	public ScoreboardController(ScoreboardRepository repository) {
		this.repository = repository;
	}

	public List<Student> getAllScores() {
		return repository.findAll();
	}

	public void saveScore(String username, int points) {
		if (username == null || username.trim().isEmpty()) {
			return;
		}
		String name = username.trim();
		Student student = repository.findByName(name);
		if (student == null) {
			student = new Student(name, 0);
		}
		student.addScore(points);
		repository.save(student);
	}

	public void deleteScore(int id) {
		repository.deleteById(id);
	}
}
