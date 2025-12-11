import javax.swing.*;

public class TagExtractorRunner {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TagExtractorGUI gui = new TagExtractorGUI();
            gui.setVisible(true);
        });
    }
}
