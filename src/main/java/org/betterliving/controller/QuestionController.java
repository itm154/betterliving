package org.betterliving.controller;

import org.betterliving.model.question.MultipleChoiceQuestion;
import org.betterliving.model.question.Question;
import org.betterliving.model.question.ShortAnswerQuestion;
import org.betterliving.model.question.TrueFalseQuestion;
import org.betterliving.repository.QuestionRepository;

import java.util.List;

public class QuestionController {
	private final QuestionRepository repository;

	public QuestionController(QuestionRepository repository) {
		this.repository = repository;
	}

	public void addMultipleChoice(String text, String answer, int points, List<String> options) {
		repository.save(new MultipleChoiceQuestion(text, answer, points, options));
	}

	public void addTrueFalse(String text, boolean answer, int points) {
		repository.save(new TrueFalseQuestion(text, answer, points));
	}

	public void addShortAnswer(String text, String answer, int points) {
		repository.save(new ShortAnswerQuestion(text, answer, points));
	}

	public List<Question> getAllQuestions() {
		return repository.findAll();
	}

	public void deleteQuestion(int id) {
		repository.deleteById(id);
	}

	public boolean checkAnswer(int questionId, String userResponse) {
		return repository.findAll().stream()
				.filter(q -> q.getId() == questionId)
				.findFirst()
				.map(q -> q.validateAnswer(userResponse))
				.orElse(false);
	}
}
