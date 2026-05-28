package org.betterliving.view;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LearningModuleListView extends JFrame {
    // Stores each topic page directly as an array: [0]=ID, [1]=Title, [2]=Content, [3]=ImagePath
    private final List<String[]> modulesData;
    private final boolean isTeacher;
    private JTable table;
    private DefaultTableModel tableModel;

    public LearningModuleListView(boolean isTeacher) {
        this.isTeacher = isTeacher;
        this.modulesData = new ArrayList<>();
        loadMockData(); // Instantiates internal UI mock data collection

        setTitle("SDG 13: Climate Action - Learning Modules Catalogue");
        setSize(750, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        String[] columns = {"Module ID", "Topic Title", "Content Preview"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } 
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        refreshTable();

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Control Panel Tray
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton openBtn = new JButton("Open Selected Module");
        JButton createModuleBtn = new JButton("Create New Module");
        JButton deleteModuleBtn = new JButton("Delete Selected Module");
        JButton closeBtn = new JButton("Close");

        openBtn.addActionListener(e -> openSelectedModule());
        createModuleBtn.addActionListener(e -> createNewModule());
        deleteModuleBtn.addActionListener(e -> deleteSelectedModule());
        closeBtn.addActionListener(e -> dispose());

        actionPanel.add(openBtn);
        
        // Expose managerial features to Teachers only
        if (isTeacher) {
            actionPanel.add(createModuleBtn);
            actionPanel.add(deleteModuleBtn);
        }
        
        actionPanel.add(closeBtn);
        add(actionPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void deleteSelectedModule() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a module to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Are you sure you want to delete this module?", 
            "Confirm Deletion", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            modulesData.remove(selectedRow);
            refreshTable();
            JOptionPane.showMessageDialog(this, "Module deleted successfully.");
        }
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        for (String[] row : modulesData) {
            String snippet = row[2].length() > 60 ? row[2].substring(0, 57) + "..." : row[2];
            tableModel.addRow(new Object[]{row[0], row[1], snippet});
        }
    }

    private void createNewModule() {
        String newId = String.valueOf(modulesData.size() + 1);
        // Added 4th index position initialized to an empty string (No image attached yet)
        String[] newModuleData = {newId, "New Module Title", "Enter module content here...", ""};
        
        modulesData.add(newModuleData);
        refreshTable();
        
        // Open edit viewer dashboard immediately
        new LearningModuleView(newModuleData, true, this);
    }

    private void openSelectedModule() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a module row first.");
            return;
        }
        
        String[] selectedData = modulesData.get(selectedRow);
        new LearningModuleView(selectedData, isTeacher, this);
    }

    private void loadMockData() {
        // Mock data initialized with 4-element structures (leaving assets empty or pointing to default layout strings)
        modulesData.add(new String[]{"1", "SDG 13: Climate Action Overview", 
            "Climate change is one of the greatest challenges of our time. It disrupts national economies and affects lives, costing people, communities, and countries dearly today and even more tomorrow.", ""});
        
        modulesData.add(new String[]{"2", "The Greenhouse Effect", 
            "Human activities—predominantly the burning of fossil fuels—have spiked atmospheric carbon dioxide concentrations, accelerating warming trends via trapping thermal energy within Earth's biosphere.", ""});
        
        modulesData.add(new String[]{"3", "Rising Sea Levels", 
            "Global warming is causing glaciers and ice sheets to melt, leading to accelerated sea-level increases which directly threaten coastal infrastructure globally.", ""});
    }
}