package org.betterliving.view;

import org.betterliving.controller.LearningModuleController;
import org.betterliving.model.LearningModule;

import javax.swing.*;
import java.awt.*;

public class LearningModuleView extends JFrame {
	private final LearningModule module;
	private final LearningModuleListView parentView;
	private final LearningModuleController lmController;
	private final boolean isTeacher;

	private final JTextField titleField;
	private final JTextArea contentArea;
	private final JLabel imageLabel;

	public LearningModuleView(LearningModule module, boolean isTeacher, LearningModuleListView parentView,
	                          LearningModuleController lmController) {
		this.module = module;
		this.parentView = parentView;
		this.lmController = lmController;
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
					try {
						byte[] bytes = java.nio.file.Files.readAllBytes(chooser.getSelectedFile().toPath());
						module.setImageBytes(bytes);
						renderImage();
					} catch (Exception ex) {
						ex.printStackTrace();
						JOptionPane.showMessageDialog(this, "Failed to read image file.");
					}
				}
			});
			removeBtn.addActionListener(e -> {
				module.setImageBytes(null);
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

				lmController.updateModule(module);

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
		byte[] bytes = module.getImageBytes();
		if (bytes != null && bytes.length > 0) {
			try {
				Image img = new ImageIcon(bytes).getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
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
