package org.betterliving.model.question;

public class MultipleChoiceQuestion extends Question {
	private final String correctAnswer;

	public MultipleChoiceQuestion(int id, String text, String correctAnswer, int points) {
		super(id, text, points);
		this.correctAnswer = correctAnswer;
	}

	@Override
	public String getQuestionType() {
		return "MC";
	}

	@Override
	public boolean validateAnswer(String userResponse) {
		if (userResponse == null)
			return false;
		return userResponse.trim().equalsIgnoreCase(correctAnswer);
	}
}
