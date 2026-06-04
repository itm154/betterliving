package org.betterliving.view;

import org.betterliving.controller.QuestionController;
import org.betterliving.controller.ScoreboardController;
import org.betterliving.model.question.MultipleChoiceQuestion;
import org.betterliving.model.question.Question;
import org.betterliving.model.question.ShortAnswerQuestion;
import org.betterliving.model.question.TrueFalseQuestion;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuestionView extends JFrame {
	private final QuestionController qsController;
	private final ScoreboardController sbController;
	private final String name;
	private final List<Question> questions;
	private int currentQuestionIndex = 0;
	private int totalScore = 0;
	private int maxPossibleScore = 0;
	private int totalCorrect = 0;

	private final JTextArea questionArea;
	private final JPanel inputPanel;
	private final JLabel feedbackLabel;
	private final JButton nextBtn;

	public QuestionView(QuestionController qsController) {
		this(qsController, 1, null, null);
	}

	public QuestionView(QuestionController qsController, int quizSetId) {
		this(qsController, quizSetId, null, null);
	}

	public QuestionView(QuestionController qsController, int quizSetId, ScoreboardController sbController, String name) {
		this.qsController = qsController;
		this.sbController = sbController;
		this.name = name;
		this.questions = qsController.getQuestionsForQuizSet(quizSetId);

		for (Question q : questions) {
			maxPossibleScore += q.getPoints();
		}

		setTitle("BetterLiving Quiz");
		setSize(600, 450);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		// Main container using GridBagLayout to center everything
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(15, 20, 15, 20);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		questionArea = new JTextArea("Welcome to the Quiz!");
		questionArea.setFont(new Font("Arial", Font.BOLD, 18));
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		questionArea.setEditable(false);
		questionArea.setFocusable(false);
		questionArea.setOpaque(false);
		mainPanel.add(questionArea, gbc);

		gbc.fill = GridBagConstraints.NONE;
		gbc.weightx = 0;

		inputPanel = new JPanel();
		mainPanel.add(inputPanel, gbc);

		feedbackLabel = new JLabel("", SwingConstants.CENTER);
		feedbackLabel.setFont(new Font("Arial", Font.ITALIC, 14));
		mainPanel.add(feedbackLabel, gbc);

		nextBtn = new JButton("Next Question");
		nextBtn.setVisible(false);
		nextBtn.addActionListener(e -> {
			currentQuestionIndex++;
			if (currentQuestionIndex < questions.size()) {
				displayQuestion(questions.get(currentQuestionIndex));
			} else {
				showResults();
			}
		});
		mainPanel.add(nextBtn, gbc);

		add(mainPanel);

		if (!questions.isEmpty()) {
			displayQuestion(questions.get(currentQuestionIndex));
		} else {
			questionArea.setText("No questions available.");
		}

		setVisible(true);
	}

	private void displayQuestion(Question q) {
		inputPanel.removeAll();
		inputPanel.setVisible(true);
		feedbackLabel.setText("");
		nextBtn.setVisible(false);
		questionArea.setText("Question " + (currentQuestionIndex + 1) + ": " + q.getQuestionText());

		if (q instanceof TrueFalseQuestion) {
			JButton trueBtn = new JButton("True");
			JButton falseBtn = new JButton("False");
			trueBtn.addActionListener(e -> submitAnswer(q, "true"));
			falseBtn.addActionListener(e -> submitAnswer(q, "false"));
			inputPanel.add(trueBtn);
			inputPanel.add(falseBtn);
		} else if (q instanceof MultipleChoiceQuestion mcq) {
			for (String option : mcq.getOptions()) {
				JButton btn = new JButton(option);
				btn.addActionListener(e -> submitAnswer(q, option));
				inputPanel.add(btn);
			}
		} else if (q instanceof ShortAnswerQuestion) {
			JTextField textField = new JTextField(25);
			JButton submitBtn = new JButton("Submit");
			submitBtn.addActionListener(e -> submitAnswer(q, textField.getText()));
			inputPanel.add(textField);
			inputPanel.add(submitBtn);
		}

		inputPanel.revalidate();
		inputPanel.repaint();
	}

	private void submitAnswer(Question q, String answer) {
		inputPanel.setVisible(false);

		boolean correct = q.validateAnswer(answer);
		if (correct) {
			totalScore += q.getPoints();
			totalCorrect++;
			feedbackLabel.setText("Correct! (+ " + q.getPoints() + " points)");
			feedbackLabel.setForeground(new Color(0, 120, 0));
		} else {
			feedbackLabel.setText("Incorrect!");
			feedbackLabel.setForeground(Color.RED);
		}

		nextBtn.setVisible(true);
		if (currentQuestionIndex == questions.size() - 1) {
			nextBtn.setText("Finish Quiz");
		}
	}

	private void showResults() {
		inputPanel.removeAll();
		inputPanel.setVisible(true);
		nextBtn.setVisible(false);
		questionArea.setText("Quiz Finished!");

		if (sbController != null && name != null && !name.trim().isEmpty()) {
			sbController.saveScore(name, totalScore);
		}

		float percentage = questions.isEmpty() ? 0 : ((float) totalCorrect / questions.size()) * 100;
		String finalMessage = getMotivationalMessage(percentage);

		String resultText = String.format(
				"<html><center>Final Score: %d / %d<br>Questions Correct: %d / %d<br>Percentage: %.2f%%<br><br><font color='blue' size='5'>%s</font></center></html>",
				totalScore, maxPossibleScore, totalCorrect, questions.size(), percentage, finalMessage);

		feedbackLabel.setText(resultText);
		feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16));

		JButton closeBtn = new JButton("Back to Main Menu");
		closeBtn.addActionListener(e -> dispose());
		inputPanel.add(closeBtn);

		inputPanel.revalidate();
		inputPanel.repaint();
	}

	private String getMotivationalMessage(float percentage) {
		if (percentage >= 80)
			return "Outstanding!";
		if (percentage >= 60)
			return "That's good!";
		if (percentage >= 40)
			return "Good try!";
		if (percentage >= 20)
			return "You can do better!";
		return "Don't give up!";
	}
}
