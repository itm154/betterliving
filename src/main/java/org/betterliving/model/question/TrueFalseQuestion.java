package org.betterliving.model.question;

public class TrueFalseQuestion extends Question {
	private final boolean correctAnswer;

	public TrueFalseQuestion(String text, boolean correctAnswer, int points) {
		super(text, points);
		this.correctAnswer = correctAnswer;
	}

	// For repository use
	public TrueFalseQuestion(int id, String text, boolean correctAnswer, int points) {
		this(text, correctAnswer, points);
		setId(id);
	}

	@Override
	public String getCorrectAnswer() {
		return String.valueOf(correctAnswer);
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.TF;
	}

	@Override
	public boolean validateAnswer(String userResponse) {
		return Boolean.parseBoolean(userResponse) == correctAnswer;
	}
}
