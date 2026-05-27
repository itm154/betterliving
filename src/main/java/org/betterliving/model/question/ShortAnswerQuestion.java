package org.betterliving.model.question;

public class ShortAnswerQuestion extends Question {
	private final String correctAnswer;

	public ShortAnswerQuestion(int id, String text, String correctAnswer, int points) {
		super(id, text, points);
		this.correctAnswer = correctAnswer;
	}

	@Override
	public String getQuestionType() {
		return "SA";
	}

	@Override
	public boolean validateAnswer(String userResponse) {
		if (userResponse == null)
			return false;
		// Remove trailing space and ignore case sensitivity
		return userResponse.trim().equalsIgnoreCase(correctAnswer.trim());
	}
}
