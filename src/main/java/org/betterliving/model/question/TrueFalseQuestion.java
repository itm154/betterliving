package org.betterliving.model.question;

public class TrueFalseQuestion implements Question {
	private final int id;
	private final String text;
	private final boolean correctAnswer;

	public TrueFalseQuestion(int id, String text, boolean correctAnswer) {
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
		return "TF";
	}

	@Override
	public boolean validateAnswer(String userResponse) {
		return Boolean.parseBoolean(userResponse) == correctAnswer;
	}
}
