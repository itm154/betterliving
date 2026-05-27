package org.betterliving.model.question;

public class TrueFalseQuestion extends Question {
	private final boolean correctAnswer;

	public TrueFalseQuestion(int id, String text, boolean correctAnswer, int points) {
		super(id, text, points);
		this.correctAnswer = correctAnswer;
	}

	@Override
	public String getQuestionType() {
		return "TF";
	}

	@Override
	public boolean validateAnswer(String userResponse) {
		return Boolean.parseBoolean(userResponse) == correctAnswer;
	}
}
