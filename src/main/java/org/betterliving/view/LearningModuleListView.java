package org.betterliving.view;

import org.betterliving.controller.LearningModuleController;
import org.betterliving.model.LearningModule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LearningModuleListView extends JFrame {
	private final LearningModuleController controller;
	private final boolean isTeacher;
	private JTable table;
	private DefaultTableModel tableModel;
	private List<LearningModule> currentModules;

	public LearningModuleListView(LearningModuleController controller, boolean isTeacher) {
		this.controller = controller;
		this.isTeacher = isTeacher;

		setTitle("SDG 13: Climate Action - Learning Modules");
		setSize(750, 400);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(10, 10));

		String[] columns = { "Module ID", "Topic Title", "Content Preview" };
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
		JButton openBtn = new JButton("Open Selected");
		JButton createBtn = new JButton("Create New");
		JButton deleteBtn = new JButton("Delete Selected");

		openBtn.addActionListener(e -> openSelectedModule());
		createBtn.addActionListener(e -> {
			controller.createNewModule();
			refreshTable();
			if (!currentModules.isEmpty()) {
				new LearningModuleView(currentModules.get(currentModules.size() - 1), isTeacher, this, controller);
			}
		});
		deleteBtn.addActionListener(e -> deleteSelectedModule());

		actionPanel.add(openBtn);
		if (isTeacher) {
			actionPanel.add(createBtn);
			actionPanel.add(deleteBtn);
		}
		add(actionPanel, BorderLayout.SOUTH);

		setVisible(true);
	}

	private void deleteSelectedModule() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Please select a module to delete.");
			return;
		}

		if (JOptionPane.showConfirmDialog(this, "Are you sure?", "Confirm",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			int id = currentModules.get(selectedRow).getId();
			controller.deleteModule(id);
			refreshTable();
		}
	}

	public void refreshTable() {
		tableModel.setRowCount(0);
		currentModules = controller.getAllModules();
		for (LearningModule m : currentModules) {
			String content = m.getContentText();
			if (content == null)
				content = "";
			String snippet = content.length() > 60 ? content.substring(0, 57) + "..." : content;
			tableModel.addRow(new Object[] { m.getId(), m.getTitle(), snippet });
		}
	}

	private void openSelectedModule() {
		int selectedRow = table.getSelectedRow();
		if (selectedRow != -1) {
			new LearningModuleView(currentModules.get(selectedRow), isTeacher, this, controller);
		} else {
			JOptionPane.showMessageDialog(this, "Select a module first.");
		}
	}
}
