package org.betterliving.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class LearningModuleView extends JFrame {
    private final String[] moduleData; // Format: [0]=ID, [1]=Title, [2]=Content, [3]=ImagePath
    private final LearningModuleListView parentView;
    private final boolean isTeacher;
    
    private JTextField titleField;
    private JTextArea contentArea;
    private JLabel imageLabel;

    public LearningModuleView(String[] moduleData, boolean isTeacher, LearningModuleListView parentView) {
        this.moduleData = moduleData;
        this.parentView = parentView;
        this.isTeacher = isTeacher;

        setTitle("Viewing: " + moduleData[1]);
        setSize(850, 500); // Expanded width to comfortably anchor side-by-side text and visuals
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(15, 15));

        // Title Setup
        titleField = new JTextField(moduleData[1]);
        titleField.setFont(new Font("Arial", Font.BOLD, 22));
        titleField.setHorizontalAlignment(JTextField.CENTER);
        titleField.setEditable(isTeacher);
        if (!isTeacher) {
            titleField.setBorder(BorderFactory.createEmptyBorder());
            titleField.setOpaque(false);
        }
        add(titleField, BorderLayout.NORTH);

        // Content Area Setup (Right Pane)
        contentArea = new JTextArea(moduleData[2]);
        contentArea.setFont(new Font("Arial", Font.PLAIN, 16));
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(isTeacher); 
        JScrollPane textPane = new JScrollPane(contentArea);
        add(textPane, BorderLayout.CENTER);

        // Visuals/Media Setup Panel (Left Pane)
        JPanel mediaSidePanel = new JPanel(new BorderLayout(5, 5));
        mediaSidePanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        
        imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(250, 250));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        mediaSidePanel.add(imageLabel, BorderLayout.CENTER);

        if (isTeacher) {
            JPanel imgActionPanel = new JPanel(new GridLayout(2, 1, 5, 5));
            JButton uploadBtn = new JButton("Upload / Change Picture");
            JButton removeBtn = new JButton("Remove Picture");

            uploadBtn.addActionListener(e -> triggerImageChoice());
            removeBtn.addActionListener(e -> {
                moduleData[3] = ""; // Reset file path to blank string
                renderModuleImage(); // Re-render visual layer
            });

            imgActionPanel.add(uploadBtn);
            imgActionPanel.add(removeBtn);
            mediaSidePanel.add(imgActionPanel, BorderLayout.SOUTH);
        }
        add(mediaSidePanel, BorderLayout.WEST);

        // Core Window Interaction Tray (Bottom Panel)
        JPanel actionTray = new JPanel(new FlowLayout());
        JButton backBtn = new JButton("Back to Catalogue");
        backBtn.addActionListener(e -> dispose());
        actionTray.add(backBtn);

        if (isTeacher) {
            JButton saveBtn = new JButton("Save / Update Changes");
            saveBtn.addActionListener(e -> {
                moduleData[1] = titleField.getText();
                moduleData[2] = contentArea.getText();
                
                setTitle("Viewing: " + moduleData[1]);
                parentView.refreshTable(); 
                JOptionPane.showMessageDialog(this, "Module records updated successfully!");
            });
            actionTray.add(saveBtn);
        }

        add(actionTray, BorderLayout.SOUTH);
        
        // Initial Image Display Evaluation
        renderModuleImage();
        setVisible(true);
    }

    private void triggerImageChoice() {
        JFileChooser fileChooser = new JFileChooser();
        // Set standard image file extension constraints filters
        fileChooser.setDialogTitle("Select Module Topic Graphic Picture");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        
        int processOutput = fileChooser.showOpenDialog(this);
        if (processOutput == JFileChooser.APPROVE_OPTION) {
            File targetedFile = fileChooser.getSelectedFile();
            // Cache absolute path directly into our reference array element
            moduleData[3] = targetedFile.getAbsolutePath();
            renderModuleImage();
        }
    }

    private void renderModuleImage() {
        String path = moduleData[3];
        if (path != null && !path.trim().isEmpty()) {
            File checkFile = new File(path);
            if (checkFile.exists()) {
                try {
                    ImageIcon targetIcon = new ImageIcon(path);
                    // Smoothly scale chosen graphics to match our presentation frame constraint boundaries
                    Image sourceImg = targetIcon.getImage();
                    Image optimizedImg = sourceImg.getScaledInstance(250, 250, Image.SCALE_SMOOTH);
                    
                    imageLabel.setIcon(new ImageIcon(optimizedImg));
                    imageLabel.setText(""); // Wipe text out since image exists
                } catch (Exception err) {
                    imageLabel.setIcon(null);
                    imageLabel.setText("⚠️ Failed to decode graphic.");
                }
            } else {
                imageLabel.setIcon(null);
                imageLabel.setText("⚠️ Graphic path missing.");
            }
        } else {
            imageLabel.setIcon(null);
            // Display clean contextual instructions depending on user permission scopes
            if (isTeacher) {
                imageLabel.setText("[ No Graphic Attached: Upload Below ]");
            } else {
                imageLabel.setText("[ No Graphic Provided for This Topic ]");
            }
        }
    }
}