package org.betterliving.view;

import org.betterliving.controller.QuestionController;
import org.betterliving.model.question.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class QuestionView extends JFrame {
	private final QuestionController controller;
	private List<Question> questions;
	private int currentQuestionIndex = 0;
	private int totalScore = 0;
	private int maxPossibleScore = 0;

	private JTextArea questionArea;
	private JPanel inputPanel;
	private JLabel feedbackLabel;
	private JButton nextBtn;

	public QuestionView(QuestionController controller) {
		this.controller = controller;
		this.questions = controller.getAllQuestions();

		for (Question q : questions) {
			maxPossibleScore += q.getQuestionPoints();
		}

		setTitle("BetterLiving Quiz");
		setSize(600, 400);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);

		// Main container using GridBagLayout to center everything
		JPanel mainPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.insets = new Insets(10, 20, 10, 20);
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.weightx = 1.0;

		questionArea = new JTextArea("Welcome to the Quiz!");
		questionArea.setLineWrap(true);
		questionArea.setWrapStyleWord(true);
		questionArea.setEditable(false);
		questionArea.setFocusable(false);
		questionArea.setFont(new Font("Arial", Font.BOLD, 18));
		mainPanel.add(questionArea, gbc);

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
			JTextField textField = new JTextField(30);
			JButton submitBtn = new JButton("Submit");
			submitBtn.addActionListener(e -> submitAnswer(q, textField.getText()));
			inputPanel.add(textField);
			inputPanel.add(submitBtn);
		}

		inputPanel.revalidate();
		inputPanel.repaint();
	}

	private void submitAnswer(Question q, String answer) {
		// Disable input buttons/fields
		inputPanel.setVisible(false);

		boolean correct = q.validateAnswer(answer);
		if (correct) {
			totalScore += q.getQuestionPoints();
			feedbackLabel.setText("Correct! (+ " + q.getQuestionPoints() + " points)");
			feedbackLabel.setForeground(new Color(0, 120, 0));
		} else {
			feedbackLabel.setText("Incorrect. The correct answer was: " + q.getCorrectAnswer());
			feedbackLabel.setForeground(Color.RED);
		}

		nextBtn.setVisible(true);
		if (currentQuestionIndex == questions.size() - 1) {
			nextBtn.setText("Finish Quiz");
		}
	}

	private void showResults() {
		inputPanel.removeAll();
		nextBtn.setVisible(false);
		questionArea.setText("Quiz Finished!");

		String scoreText = String.format("Final Score: %d / %d", totalScore, maxPossibleScore);
		feedbackLabel.setText(scoreText);
		feedbackLabel.setFont(new Font("Arial", Font.BOLD, 16));
		feedbackLabel.setForeground(new Color(0, 0, 150));

		JButton closeBtn = new JButton("Back to Main Menu");
		closeBtn.addActionListener(e -> dispose());
		inputPanel.add(closeBtn);

		inputPanel.revalidate();
		inputPanel.repaint();
	}
}
