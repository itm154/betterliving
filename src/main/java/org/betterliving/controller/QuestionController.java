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

	public void addMultipleChoice(String text, String answer, int points, List<String> options, int quizSetId) {
		MultipleChoiceQuestion mcq = new MultipleChoiceQuestion(text, answer, points, options);
		mcq.setQuizSetId(quizSetId);
		repository.save(mcq);
	}

	public void addTrueFalse(String text, boolean answer, int points, int quizSetId) {
		TrueFalseQuestion tfq = new TrueFalseQuestion(text, answer, points);
		tfq.setQuizSetId(quizSetId);
		repository.save(tfq);
	}

	public void addShortAnswer(String text, String answer, int points, int quizSetId) {
		ShortAnswerQuestion saq = new ShortAnswerQuestion(text, answer, points);
		saq.setQuizSetId(quizSetId);
		repository.save(saq);
	}

	public List<Question> getQuestionsForQuizSet(int quizSetId) {
		return repository.findByQuizSetId(quizSetId);
	}

	public void deleteQuestion(int id) {
		repository.deleteById(id);
	}
}
