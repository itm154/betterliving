package org.betterliving.view;

import org.betterliving.controller.QuizSetController;
import org.betterliving.controller.QuestionController;
import org.betterliving.model.QuizSet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuizSetListView extends JFrame {
	private final QuizSetController quizSetController;
	private final QuestionController questionController;
	private final boolean isTeacher;
	private JTable table;
	private DefaultTableModel tableModel;
	private List<QuizSet> currentQuizSets;

	public QuizSetListView(QuizSetController quizSetController, QuestionController questionController,
			boolean isTeacher) {
		this.quizSetController = quizSetController;
		this.questionController = questionController;
		this.isTeacher = isTeacher;

		setTitle("SDG 13: Climate Action - Quiz Sets");
		setSize(750, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));

		String[] columns = { "Quiz Set ID", "Quiz Name", "Number of Questions" };
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		refreshTable();

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton startQuizBtn = new JButton("Start Quiz");
		JButton manageBtn = new JButton("Manage Quiz Set");
		JButton createBtn = new JButton("Create New");
		JButton deleteBtn = new JButton("Delete Selected");
		JButton closeBtn = new JButton("Close");

		startQuizBtn.addActionListener(e -> startSelectedQuiz());
		manageBtn.addActionListener(e -> openSelectedQuizSet());
		createBtn.addActionListener(e -> {
			QuizSet newSet = quizSetController.createNewQuizSet();
			refreshTable();
			new QuizSetView(newSet, isTeacher, this, quizSetController, questionController);
		});
		deleteBtn.addActionListener(e -> deleteSelectedQuizSet());
		closeBtn.addActionListener(e -> dispose());

		actionPanel.add(startQuizBtn);
		if (isTeacher) {
			actionPanel.add(manageBtn);
			actionPanel.add(createBtn);
			actionPanel.add(deleteBtn);
		}
		actionPanel.add(closeBtn);
		add(actionPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void startSelectedQuiz() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a quiz set to start.");
			return;
		}
		QuizSet selectedSet = currentQuizSets.get(selectedRow);
		int questionsCount = questionController.getQuestionsForQuizSet(selectedSet.getId()).size();
		if (questionsCount == 0) {
			JOptionPane.showMessageDialog(this, "This quiz set has no questions yet!");
			return;
		}
		new QuestionView(questionController, selectedSet.getId());
	}

	private void openSelectedQuizSet() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			new QuizSetView(currentQuizSets.get(selectedRow), isTeacher, this, quizSetController, questionController);
		} else {
			JOptionPane.showMessageDialog(this, "Select a quiz set first.");
		}
	}

	private void deleteSelectedQuizSet() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a quiz set to delete.");
			return;
		}

		QuizSet selectedSet = currentQuizSets.get(selectedRow);
		if (selectedSet.getId() == 1) {
			JOptionPane.showMessageDialog(this, "The preset quiz set cannot be deleted!");
			return;
		}

		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete this quiz set and all its questions?",
				"Confirm Delete",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			quizSetController.deleteQuizSet(selectedSet.getId());
			refreshTable();
		}
	}

	public void refreshTable() {
		tableModel.setRowCount(0);
		currentQuizSets = quizSetController.getAllQuizSets();
		for (QuizSet set : currentQuizSets) {
			int count = questionController.getQuestionsForQuizSet(set.getId()).size();
			tableModel.addRow(new Object[] { set.getId(), set.getName(), count });
		}
	}
}
