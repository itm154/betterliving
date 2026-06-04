package org.betterliving.view;

import org.betterliving.controller.ScoreboardController;
import org.betterliving.model.reward.Rewardable;
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

	public ScoreboardListView(ScoreboardController sbController, boolean isTeacher) {
		this.sbController = sbController;
		this.isTeacher = isTeacher;

		setTitle("Scoreboard");
		setSize(850, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(15, 15));

		String[] columns = {"ID", "Name", "Total Score"};
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
		JButton infoBtn = new JButton("See Achievements");

		deleteBtn.addActionListener(e -> deleteSelectedScore());
		infoBtn.addActionListener(e -> showStudentInfo());

		actionPanel.add(infoBtn);
		if (isTeacher) {
			actionPanel.add(deleteBtn);
		}
		add(actionPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void showStudentInfo() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a student record to see achievements.");
			return;
		}

		Student selectedStudent = currentStudents.get(selectedRow);
		List<Rewardable> badges = selectedStudent.getAllBadges();

		StringBuilder message = new StringBuilder();
		message.append("Student Name: ").append(selectedStudent.getName()).append("\n");
		message.append("Score: ").append(selectedStudent.getScore()).append("\n\n");
		message.append("Achievements/Badges Earned:\n");
		if (badges == null || badges.isEmpty()) {
			message.append("- No badges earned yet.\n");
		} else {
			for (Rewardable badge : badges) {
				message.append("🏆 ").append(badge.getName()).append(" - ").append(badge.getDesc()).append("\n");
			}
		}

		JOptionPane.showMessageDialog(this, message.toString(), "Student Achievements", JOptionPane.INFORMATION_MESSAGE);
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
