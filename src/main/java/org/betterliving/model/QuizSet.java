package org.betterliving.model;

public class QuizSet implements Identifiable {
	private int id;
	private String title;

	public QuizSet(int id, String title) {
		this.id = id;
		this.title = title;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
