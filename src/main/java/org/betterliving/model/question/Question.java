package org.betterliving.model.question;

public interface Question {
	int getId();

	String getQuestionText();

	boolean validateAnswer(String userResponse);

	String getQuestionType();
}
