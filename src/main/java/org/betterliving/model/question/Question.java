package org.betterliving.model.question;

import java.util.List;

import org.betterliving.model.Identifiable;
import org.betterliving.model.Scorable;

public abstract class Question implements Identifiable, Scorable {
	private int id;
	private final String text;
	private final int points;

	protected Question(String text, int points) {
		this.text = text;
		this.points = points;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public String getQuestionText() {
		return text;
	}

	@Override
	public int getPoints() {
		return points;
	}

	public abstract String getQuestionType();

	public abstract String getCorrectAnswer();

	public List<String> getOptions() {
		return java.util.Collections.emptyList();
	}

	public abstract boolean validateAnswer(String userResponse);
}
