package org.betterliving.model.question;

public class ShortAnswerQuestion extends Question {
	private final String correctAnswer;

	public ShortAnswerQuestion(String text, String correctAnswer, int points) {
		super(text, points);
		this.correctAnswer = correctAnswer;
	}

	// For repository use
	public ShortAnswerQuestion(int id, String text, String correctAnswer, int points) {
		this(text, correctAnswer, points);
		setId(id);
	}

	public String getCorrectAnswer() {
		return correctAnswer;
	}

	@Override
	public QuestionType getQuestionType() {
		return QuestionType.SA;
	}

	@Override
	public boolean validateAnswer(String userResponse) {
		if (userResponse == null)
			return false;
		// Remove trailing space and ignore case sensitivity
		return userResponse.trim().equalsIgnoreCase(correctAnswer.trim());
	}
}
