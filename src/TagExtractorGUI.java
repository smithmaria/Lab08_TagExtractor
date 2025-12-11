import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class TagExtractorGUI extends JFrame {
    private JButton loadTextButton;
    private JButton loadStopWordsButton;
    private JButton extractTagsButton;
    private JButton saveTagsButton;
    private JLabel textFileLabel;
    private JLabel stopWordsLabel;
    private JTextArea resultsArea;
    private TagExtractorModel model;
    private File textFile;
    private File stopWordsFile;

    public TagExtractorGUI() {
        model = new TagExtractorModel();
        setTitle("Tag/Keyword Extractor");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top panel for file loading
        JPanel topPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        loadTextButton = new JButton("Load Text File");
        loadStopWordsButton = new JButton("Load Stop Words");
        extractTagsButton = new JButton("Extract Tags");
        textFileLabel = new JLabel("No text file loaded");
        stopWordsLabel = new JLabel("No stop words loaded");

        topPanel.add(loadTextButton);
        topPanel.add(textFileLabel);
        topPanel.add(loadStopWordsButton);
        topPanel.add(stopWordsLabel);
        topPanel.add(extractTagsButton);

        // Center panel for results
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        // Bottom panel for save
        saveTagsButton = new JButton("Save Tags to File");
        JPanel bottomPanel = new JPanel();
        bottomPanel.add(saveTagsButton);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setupListeners();
    }

    private void setupListeners() {
        loadTextButton.addActionListener(e -> loadTextFile());
        loadStopWordsButton.addActionListener(e -> loadStopWords());
        extractTagsButton.addActionListener(e -> extractTags());
        saveTagsButton.addActionListener(e -> saveTags());
    }

    private void loadTextFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textFile = chooser.getSelectedFile();
            textFileLabel.setText("Text: " + textFile.getName());
        }
    }

    private void loadStopWords() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            stopWordsFile = chooser.getSelectedFile();
            try {
                model.loadStopWords(stopWordsFile);
                stopWordsLabel.setText("Stop Words: " + stopWordsFile.getName());
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error loading stop words: " + ex.getMessage());
            }
        }
    }

    private void extractTags() {
        if (textFile == null) {
            JOptionPane.showMessageDialog(this, "Please load a text file first");
            return;
        }
        if (stopWordsFile == null) {
            JOptionPane.showMessageDialog(this, "Please load stop words first");
            return;
        }

        try {
            model.processTextFile(textFile);
            displayResults();
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error processing file: " + ex.getMessage());
        }
    }

    private void displayResults() {
        resultsArea.setText(model.getFormattedResults());
    }

    private void saveTags() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                model.saveResults(chooser.getSelectedFile());
                JOptionPane.showMessageDialog(this, "Tags saved successfully!");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving: " + ex.getMessage());
            }
        }
    }
}
