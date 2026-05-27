package org.betterliving.model.question;

public abstract class Question {
	private int id;
	private final String text;
	private final int points;

	protected Question(String text, int points) {
		this.text = text;
		this.points = points;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestionText() {
		return text;
	}

	public int getQuestionPoints() {
		return points;
	}

	public abstract String getQuestionType();

	public abstract String getCorrectAnswer();

	public abstract boolean validateAnswer(String userResponse);
}
