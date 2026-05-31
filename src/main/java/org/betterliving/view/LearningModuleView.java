package org.betterliving.view;

import org.betterliving.controller.LearningModuleController;
import org.betterliving.model.LearningModule;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LearningModuleView extends JFrame {
	private final LearningModule module;
	private final LearningModuleListView parentView;
	private final LearningModuleController controller;
	private final boolean isTeacher;

	private JTextField titleField;
	private JTextArea contentArea;
	private JLabel imageLabel;

	public LearningModuleView(LearningModule module, boolean isTeacher, LearningModuleListView parentView,
			LearningModuleController controller) {
		this.module = module;
		this.parentView = parentView;
		this.controller = controller;
		this.isTeacher = isTeacher;

		setTitle("Viewing: " + module.getTitle());
		setSize(850, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setLayout(new BorderLayout(15, 15));

		titleField = new JTextField(module.getTitle());
		titleField.setFont(new Font("Arial", Font.BOLD, 22));
		titleField.setHorizontalAlignment(JTextField.CENTER);
		titleField.setEditable(isTeacher);
		if (!isTeacher) {
			titleField.setBorder(BorderFactory.createEmptyBorder());
			titleField.setOpaque(false);
		}
		add(titleField, BorderLayout.NORTH);

		contentArea = new JTextArea(module.getContentText());
		contentArea.setFont(new Font("Arial", Font.PLAIN, 16));
		contentArea.setLineWrap(true);
		contentArea.setWrapStyleWord(true);
		contentArea.setEditable(isTeacher);
		add(new JScrollPane(contentArea), BorderLayout.CENTER);

		JPanel mediaSidePanel = new JPanel(new BorderLayout(5, 5));
		imageLabel = new JLabel("", SwingConstants.CENTER);
		imageLabel.setPreferredSize(new Dimension(250, 250));
		imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
		mediaSidePanel.add(imageLabel, BorderLayout.CENTER);

		if (isTeacher) {
			JPanel imgActionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
			JButton uploadBtn = new JButton("Upload Picture");
			JButton removeBtn = new JButton("Remove Picture");

			uploadBtn.addActionListener(e -> {
				JFileChooser chooser = new JFileChooser();
				if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
					module.setImagePath(chooser.getSelectedFile().getAbsolutePath());
					renderImage();
				}
			});
			removeBtn.addActionListener(e -> {
				module.setImagePath("");
				renderImage();
			});

			imgActionPanel.add(uploadBtn);
			imgActionPanel.add(removeBtn);
			mediaSidePanel.add(imgActionPanel, BorderLayout.SOUTH);
		}
		add(mediaSidePanel, BorderLayout.WEST);

		JPanel actionTray = new JPanel(new FlowLayout());
		JButton backBtn = new JButton("Back");
		backBtn.addActionListener(e -> dispose());
		actionTray.add(backBtn);

		if (isTeacher) {
			JButton saveBtn = new JButton("Save Changes");
			saveBtn.addActionListener(e -> {
				module.setTitle(titleField.getText());
				module.setContentText(contentArea.getText());

				controller.updateModule(module);

				setTitle("Viewing: " + module.getTitle());
				parentView.refreshTable();
				JOptionPane.showMessageDialog(this, "Saved to Database!");
			});
			actionTray.add(saveBtn);
		}

		add(actionTray, BorderLayout.SOUTH);
		renderImage();
		setVisible(true);
	}

	private void renderImage() {
		String path = module.getImagePath();
		if (path != null && !path.trim().isEmpty() && new File(path).exists()) {
			try {
				Image img = new ImageIcon(path).getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
				imageLabel.setIcon(new ImageIcon(img));
				imageLabel.setText("");
			} catch (Exception err) {
				imageLabel.setIcon(null);
				imageLabel.setText("⚠️ Failed to load image.");
			}
		} else {
			imageLabel.setIcon(null);
			imageLabel.setText(isTeacher ? "[ Upload Picture Below ]" : "[ No Picture ]");
		}
	}
}
