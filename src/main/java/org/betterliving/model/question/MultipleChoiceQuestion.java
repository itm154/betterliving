package org.betterliving.model.question;

public class MultipleChoiceQuestion implements Question {
	private final int id;
	private final String text;
	private final String correctAnswer;

	public MultipleChoiceQuestion(int id, String text, String correctAnswer) {
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
		return "MC";
	}

	@Override
	public boolean validateAnswer(String userResponse) {
		if (userResponse == null)
			return false;
		return userResponse.trim().equalsIgnoreCase(correctAnswer);
	}
}
