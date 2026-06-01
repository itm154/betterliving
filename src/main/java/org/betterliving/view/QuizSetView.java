package org.betterliving.view;

import org.betterliving.controller.QuizSetController;
import org.betterliving.controller.QuestionController;
import org.betterliving.model.QuizSet;
import org.betterliving.model.question.Question;
import org.betterliving.model.question.MultipleChoiceQuestion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class QuizSetView extends JFrame {
	private final QuizSet quizSet;
	private final boolean isTeacher;
	private final QuizSetListView parentView;
	private final QuizSetController quizSetController;
	private final QuestionController questionController;

	private JTextField nameField;
	private JTable questionTable;
	private DefaultTableModel tableModel;
	private List<Question> currentQuestions;

	public QuizSetView(QuizSet quizSet, boolean isTeacher, QuizSetListView parentView,
			QuizSetController quizSetController, QuestionController questionController) {
		this.quizSet = quizSet;
		this.isTeacher = isTeacher;
		this.parentView = parentView;
		this.quizSetController = quizSetController;
		this.questionController = questionController;

		setTitle("Managing Quiz Set: " + quizSet.getName());
		setSize(850, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(15, 15));

		// Top panel: Name of the Quiz Set
		JPanel topPanel = new JPanel(new BorderLayout(5, 5));
		topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10));
		JLabel nameLabel = new JLabel("Quiz Set Name: ");
		nameLabel.setFont(new Font("Arial", Font.BOLD, 14));
		nameField = new JTextField(quizSet.getName());
		nameField.setFont(new Font("Arial", Font.PLAIN, 16));
		nameField.setEditable(isTeacher);
		topPanel.add(nameLabel, BorderLayout.WEST);
		topPanel.add(nameField, BorderLayout.CENTER);
		add(topPanel, BorderLayout.NORTH);

		// Center panel: Questions list inside this Quiz Set
		JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
		centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

		JLabel tableTitleLabel = new JLabel("Questions in this Quiz Set:");
		tableTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
		centerPanel.add(tableTitleLabel, BorderLayout.NORTH);

		String[] columns = { "ID", "Type", "Points", "Question Text", "Correct Answer", "MCQ Options" };
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		questionTable = new JTable(tableModel);
		questionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		refreshQuestionsTable();

		JScrollPane scrollPane = new JScrollPane(questionTable);
		centerPanel.add(scrollPane, BorderLayout.CENTER);

		// Right side panel: Manage questions buttons
		if (isTeacher) {
			JPanel btnPanel = new JPanel(new GridLayout(4, 1, 10, 10));
			btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
			JButton addQuestionBtn = new JButton("Add Question");
			JButton deleteQuestionBtn = new JButton("Delete Question");

			addQuestionBtn.addActionListener(e -> openAddQuestionDialog());
			deleteQuestionBtn.addActionListener(e -> deleteSelectedQuestion());

			btnPanel.add(addQuestionBtn);
			btnPanel.add(deleteQuestionBtn);
			// Fill empty space
			btnPanel.add(new JLabel(""));
			btnPanel.add(new JLabel(""));
			centerPanel.add(btnPanel, BorderLayout.EAST);
		}

		add(centerPanel, BorderLayout.CENTER);

		// Bottom panel: Action Tray
		JPanel actionTray = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(e -> dispose());
		actionTray.add(backBtn);

		if (isTeacher) {
			JButton saveBtn = new JButton("Save Changes");
			saveBtn.addActionListener(e -> {
				quizSet.setName(nameField.getText());
				quizSetController.updateQuizSet(quizSet);
				setTitle("Managing Quiz Set: " + quizSet.getName());
				parentView.refreshTable();
				JOptionPane.showMessageDialog(this, "Quiz Set updated successfully!");
			});
			actionTray.add(saveBtn);
		}

		add(actionTray, BorderLayout.SOUTH);
		setVisible(true);
	}

	private void refreshQuestionsTable() {
		tableModel.setRowCount(0);
		currentQuestions = questionController.getQuestionsForQuizSet(quizSet.getId());
		for (Question q : currentQuestions) {
			String optionsStr = "-";
			if (q instanceof MultipleChoiceQuestion mcq) {
				optionsStr = String.join(", ", mcq.getOptions());
			}
			tableModel.addRow(new Object[] {
					q.getId(),
					q.getQuestionType(),
					q.getPoints(),
					q.getQuestionText(),
					q.getCorrectAnswer(),
					optionsStr
			});
		}
	}

	private void deleteSelectedQuestion() {
		int selectedRow = questionTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a question to delete.");
			return;
		}

		if (JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this question?", "Confirm",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			int id = currentQuestions.get(selectedRow).getId();
			questionController.deleteQuestion(id);
			refreshQuestionsTable();
			parentView.refreshTable(); // Update question count in parent
		}
	}

	private void openAddQuestionDialog() {
		JDialog dialog = new JDialog(this, "Add Question to " + quizSet.getName(), true);
		dialog.setSize(500, 400);
		dialog.setLocationRelativeTo(this);
		dialog.setLayout(new GridBagLayout());

		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10, 10, 10, 10);
		gbc.fill = GridBagConstraints.HORIZONTAL;

		// 1. Question Type
		gbc.gridx = 0;
		gbc.gridy = 0;
		dialog.add(new JLabel("Question Type:"), gbc);

		gbc.gridx = 1;
		String[] types = { "Multiple Choice", "True / False", "Short Answer" };
		JComboBox<String> typeCombo = new JComboBox<>(types);
		dialog.add(typeCombo, gbc);

		// 2. Question Text
		gbc.gridx = 0;
		gbc.gridy = 1;
		dialog.add(new JLabel("Question Text:"), gbc);

		gbc.gridx = 1;
		JTextField textFd = new JTextField(20);
		dialog.add(textFd, gbc);

		// 3. Points
		gbc.gridx = 0;
		gbc.gridy = 2;
		dialog.add(new JLabel("Points:"), gbc);

		gbc.gridx = 1;
		JSpinner pointsSpinner = new JSpinner(new SpinnerNumberModel(5, 1, 100, 5));
		dialog.add(pointsSpinner, gbc);

		// 4. Correct Answer
		gbc.gridx = 0;
		gbc.gridy = 3;
		JLabel ansLabel = new JLabel("Correct Answer:");
		dialog.add(ansLabel, gbc);

		gbc.gridx = 1;
		// Swap answer fields depending on question type
		JPanel answerPanel = new JPanel(new CardLayout());
		JTextField mcqAndShortAnsFd = new JTextField(20);
		JComboBox<String> tfCombo = new JComboBox<>(new String[] { "true", "false" });

		answerPanel.add(mcqAndShortAnsFd, "TEXT");
		answerPanel.add(tfCombo, "TF");
		dialog.add(answerPanel, gbc);

		// 5. MCQ Options
		gbc.gridx = 0;
		gbc.gridy = 4;
		JLabel optionsLabel = new JLabel("MCQ Options (comma separated):");
		dialog.add(optionsLabel, gbc);

		gbc.gridx = 1;
		JTextField optionsFd = new JTextField(20);
		dialog.add(optionsFd, gbc);

		// Type changing action to enable/disable fields
		typeCombo.addActionListener(e -> {
			String selected = (String) typeCombo.getSelectedItem();
			CardLayout cl = (CardLayout) answerPanel.getLayout();
			if ("True / False".equals(selected)) {
				cl.show(answerPanel, "TF");
				optionsFd.setEnabled(false);
				optionsLabel.setEnabled(false);
			} else {
				cl.show(answerPanel, "TEXT");
				if ("Multiple Choice".equals(selected)) {
					optionsFd.setEnabled(true);
					optionsLabel.setEnabled(true);
				} else {
					optionsFd.setEnabled(false);
					optionsLabel.setEnabled(false);
				}
			}
		});

		// Initialize
		optionsFd.setEnabled(true);
		optionsLabel.setEnabled(true);

		// Save & Cancel buttons
		gbc.gridx = 0;
		gbc.gridy = 5;
		gbc.gridwidth = 2;
		JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton saveBtn = new JButton("Save Question");
		JButton cancelBtn = new JButton("Cancel");

		cancelBtn.addActionListener(ev -> dialog.dispose());
		saveBtn.addActionListener(ev -> {
			String qText = textFd.getText().trim();
			int points = (Integer) pointsSpinner.getValue();
			String selectedType = (String) typeCombo.getSelectedItem();

			if (qText.isEmpty()) {
				JOptionPane.showMessageDialog(dialog, "Please enter question text.");
				return;
			}

			if ("Multiple Choice".equals(selectedType)) {
				String answer = mcqAndShortAnsFd.getText().trim();
				String optsRaw = optionsFd.getText().trim();
				if (answer.isEmpty() || optsRaw.isEmpty()) {
					JOptionPane.showMessageDialog(dialog, "Please fill in correct answer and MCQ options.");
					return;
				}
				List<String> optionsList = Arrays.asList(optsRaw.split("\\s*,\\s*"));
				if (!optionsList.contains(answer)) {
					JOptionPane.showMessageDialog(dialog, "MCQ Options must contain the correct answer.");
					return;
				}
				questionController.addMultipleChoice(qText, answer, points, optionsList, quizSet.getId());
			} else if ("True / False".equals(selectedType)) {
				boolean answer = Boolean.parseBoolean((String) tfCombo.getSelectedItem());
				questionController.addTrueFalse(qText, answer, points, quizSet.getId());
			} else { // Short Answer
				String answer = mcqAndShortAnsFd.getText().trim();
				if (answer.isEmpty()) {
					JOptionPane.showMessageDialog(dialog, "Please enter a correct answer.");
					return;
				}
				questionController.addShortAnswer(qText, answer, points, quizSet.getId());
			}

			dialog.dispose();
			refreshQuestionsTable();
			parentView.refreshTable(); // Update question count in parent
		});

		btnPanel.add(saveBtn);
		btnPanel.add(cancelBtn);
		dialog.add(btnPanel, gbc);

		dialog.pack();
		dialog.setVisible(true);
	}
}
