package org.betterliving.view;

import org.betterliving.controller.ScoreboardController;
import org.betterliving.model.user.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ScoreboardListView extends JFrame {
	private final ScoreboardController sbController;
	private final boolean isTeacher;
	private final DefaultTableModel tableModel;
	private final JTable table;
	private List<Student> currentStudents;

	public ScoreboardListView() {
		this(new ScoreboardController(new org.betterliving.repository.ScoreboardRepository()), false);
	}

	public ScoreboardListView(ScoreboardController sbController) {
		this(sbController, false);
	}

	public ScoreboardListView(ScoreboardController sbController, boolean isTeacher) {
		this.sbController = sbController;
		this.isTeacher = isTeacher;

		setTitle("Scoreboard");
		setSize(850, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(15, 15));

		String[] columns = {"ID", "Username", "Total Score"};
		tableModel = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(tableModel);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);

		refreshTable();

		JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton deleteBtn = new JButton("Delete Selected");

		deleteBtn.addActionListener(e -> deleteSelectedScore());

		if (isTeacher) {
			actionPanel.add(deleteBtn);
		}
		add(actionPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void deleteSelectedScore() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a score record to delete.");
			return;
		}

		Student selectedStudent = currentStudents.get(selectedRow);
		if (JOptionPane.showConfirmDialog(this,
				"Are you sure you want to delete the score for " + selectedStudent.getName() + "?",
				"Confirm Delete",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			sbController.deleteScore(selectedStudent.getId());
			refreshTable();
		}
	}

	public void refreshTable() {
		tableModel.setRowCount(0);
		currentStudents = sbController.getAllScores();
		for (Student s : currentStudents) {
			tableModel.addRow(new Object[]{s.getId(), s.getName(), s.getScore()});
		}
	}
}
