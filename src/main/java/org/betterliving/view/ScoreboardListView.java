package org.betterliving.view;

import org.betterliving.controller.ScoreboardController;
import org.betterliving.model.user.Student;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ScoreboardListView extends JFrame {
	private DefaultTableModel tableModel;
	private JTable table;
	private final ScoreboardController controller;
	private final boolean isTeacher;
	private List<Student> currentStudents;

	public ScoreboardListView() {
		this(new ScoreboardController(new org.betterliving.repository.ScoreboardRepository()), false);
	}

	public ScoreboardListView(ScoreboardController controller) {
		this(controller, false);
	}

	public ScoreboardListView(ScoreboardController controller, boolean isTeacher) {
		this.controller = controller;
		this.isTeacher = isTeacher;

		setTitle("Scoreboard");
		setSize(850, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(15, 15));

		String[] columns = { "ID", "Username", "Total Score" };
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

		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JButton deleteBtn = new JButton("Delete Selected");

		deleteBtn.addActionListener(e -> deleteSelectedScore());

		if (isTeacher) {
			buttonPanel.add(deleteBtn);
		}
		add(buttonPanel, BorderLayout.SOUTH);

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
			controller.deleteScore(selectedStudent.getId());
			refreshTable();
		}
	}

	public void refreshTable() {
		tableModel.setRowCount(0);
		currentStudents = controller.getAllScores();
		for (Student s : currentStudents) {
			tableModel.addRow(new Object[] { s.getId(), s.getName(), s.getScore() });
		}
	}
}
