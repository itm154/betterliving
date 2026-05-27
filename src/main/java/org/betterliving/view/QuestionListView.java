package org.betterliving.view;

import org.betterliving.controller.QuestionController;
import org.betterliving.model.question.Question;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class QuestionListView extends JFrame {
	public QuestionListView(QuestionController controller) {
		setTitle("Question List");
		setSize(600, 400);
		setLocationRelativeTo(null);
		setLayout(new BorderLayout());

		List<Question> questions = controller.getAllQuestions();
		String[] columnNames = { "Type", "Points", "Question Text", "Options", "Answer" };
		DefaultTableModel model = new DefaultTableModel(columnNames, 0);

		for (Question q : questions) {
			boolean isMCQ = q.getQuestionType().equals("MC");
			Object[] row = { q.getQuestionType(), q.getPoints(), q.getQuestionText(),
					isMCQ ? q.getOptions() : "-",
					q.getCorrectAnswer() };
			model.addRow(row);
		}

		JTable table = new JTable(model);
		table.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		JButton closeBtn = new JButton("Close");
		closeBtn.addActionListener(e -> dispose());
		add(closeBtn, BorderLayout.SOUTH);

		setVisible(true);
	}
}
