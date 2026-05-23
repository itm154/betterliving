import javax.swing.SwingUtilities;

public class BetterLiving {
    // This is the ONLY main method in your entire project.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AppGUI mainWindow = new AppGUI();
            mainWindow.setVisible(true);
        });
    }
}