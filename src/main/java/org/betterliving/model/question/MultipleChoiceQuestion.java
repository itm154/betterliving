package org.betterliving.model.question;

public class MultipleChoiceQuestion extends Question {
	private final String correctAnswer;

	public MultipleChoiceQuestion(String text, String correctAnswer, int points) {
		super(text, points);
		this.correctAnswer = correctAnswer;
	}

	// For repository use
	public MultipleChoiceQuestion(int id, String text, String correctAnswer, int points) {
		this(text, correctAnswer, points);
		setId(id);
	}

	public String getCorrectAnswer() {
		return correctAnswer;
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
