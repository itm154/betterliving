package org.betterliving.model.question;

public class ShortAnswerQuestion implements Question {
	private final int id;
	private final String text;
	private final String correctAnswer;

	public ShortAnswerQuestion(int id, String text, String correctAnswer) {
		this.id = id;
		this.text = text;
		this.correctAnswer = correctAnswer;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getQuestionText() {
		return text;
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
