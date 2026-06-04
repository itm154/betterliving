package org.betterliving.model.question;

import org.betterliving.model.Identifiable;
import org.betterliving.model.Scorable;

import java.util.List;

public abstract class Question implements Identifiable, Scorable {
	private final String text;
	private final int points;
	private int id;
	private int quizSetId = 1;

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

	public int getQuizSetId() {
		return quizSetId;
	}

	public void setQuizSetId(int quizSetId) {
		this.quizSetId = quizSetId;
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

