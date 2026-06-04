package org.betterliving.controller;

import org.betterliving.model.QuizSet;
import org.betterliving.repository.QuizSetRepository;

import java.util.List;

public class QuizSetController {
	private final QuizSetRepository repository;

	public QuizSetController(QuizSetRepository repository) {
		this.repository = repository;
	}

	public List<QuizSet> getAllQuizSets() {
		return repository.findAll();
	}

	public QuizSet createNewQuizSet() {
		QuizSet newSet = new QuizSet(0, "New Quiz Set Title");
		repository.save(newSet);
		return newSet;
	}

	public void updateQuizSet(QuizSet set) {
		repository.save(set);
	}

	public void deleteQuizSet(int id) {
		repository.deleteById(id);
	}
}
