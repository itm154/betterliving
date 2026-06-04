package org.betterliving.model.question;

import java.util.ArrayList;
import java.util.List;

public class MultipleChoiceQuestion extends Question {
	private final String correctAnswer;
	private final List<String> options;

	public MultipleChoiceQuestion(String text, String correctAnswer, int points, List<String> options) {
		super(text, points);
		this.correctAnswer = correctAnswer;
		this.options = new ArrayList<>(options);
	}

	// For repository use
	public MultipleChoiceQuestion(int id, String text, String correctAnswer, int points, List<String> options) {
		this(text, correctAnswer, points, options);
		setId(id);
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	public List<String> getOptions() {
		return new ArrayList<>(options);
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.MC;
	}

	@Override
	public boolean validateAnswer(String userResponse) {
		if (userResponse == null)
			return false;
		return userResponse.trim().equalsIgnoreCase(correctAnswer);
	}
}
