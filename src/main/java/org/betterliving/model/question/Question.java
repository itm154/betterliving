package org.betterliving.model.question;

public abstract class Question {
	private final int id;
	private final String text;
	private final int points;

	protected Question(int id, String text, int points) {
		this.id = id;
		this.text = text;
		this.points = points;
	}

	public int getId() {
		return id;
	}

	public String getQuestionText() {
		return text;
	}

	public int getQuestionPoints() {
		return points;
	}

	public abstract String getQuestionType();

	public abstract boolean validateAnswer(String userResponse);
}
