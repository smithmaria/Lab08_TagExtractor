import java.io.*;
import java.util.*;

public class TagExtractorModel {
    private Set<String> stopWords;
    private Map<String, Integer> tagFrequency;

    public TagExtractorModel() {
        stopWords = new HashSet<>();
        tagFrequency = new HashMap<>();
    }

    public void loadStopWords(File file) throws IOException {
        stopWords.clear();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            stopWords.add(line.trim().toLowerCase());
        }
        reader.close();
    }

    public void processTextFile(File file) throws IOException {
        tagFrequency.clear();
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            String words[] = line.toLowerCase().replaceAll("[^a-zA-Z ]", " ").split("\\s+");

            for (String word : words) {
                if (!word.isEmpty() && !stopWords.contains(word)) {
                    // put (word, new number), gets current number and adds 1 or adds 1 to 0
                    tagFrequency.put(word, tagFrequency.getOrDefault(word, 0) + 1);
                }
            }
        }
        reader.close();
    }

    public String getFormattedResults() {
        StringBuilder sb = new StringBuilder();

        // Sort by frequency (highest first)
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(tagFrequency.entrySet());
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        return sb.toString();
    }

    public void saveResults(File file) throws IOException {
        PrintWriter writer = new PrintWriter(new FileWriter(file));
        writer.print(getFormattedResults());
        writer.close();
    }
}
