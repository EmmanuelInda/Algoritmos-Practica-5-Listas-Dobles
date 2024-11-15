package csv;

import gods.GreekGod;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 
import java.util.List;

/**
 * Reader class responsible for reading Greek gods data from a CSV file
 * and parsing it into a list of GreekGod objects.
 */
public class Reader {
    private String filePath;
    private BufferedReader br;

    /**
     * Default constructor.
     */
    public Reader() {}

    /**
     * Constructor with file path initialization.
     * 
     * @param filePath The path to the CSV file.
     */
    public Reader(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Reads the CSV file and parses each line into a GreekGod object.
     * 
     * @return A list of GreekGod objects parsed from the CSV.
     * @throws IOException If an I/O error occurs.
     */
    public List<GreekGod> readAll() throws IOException {
        List<GreekGod> godsList = new ArrayList<>();
        br = new BufferedReader(new FileReader(filePath));
        String line;

        while ((line = br.readLine()) != null) {
            // Skip empty lines
            if (line.trim().isEmpty()) {
                continue;
            }

            // Parse the line into fields
            String[] fields = parseCSVLine(line);
            
            // Validate the number of fields
            if (fields.length != 6) {
                System.err.println("Invalid line format: " + line);
                continue; // Skip malformed lines
            }

            // Extract fields
            String nameEnglish = fields[0];
            String nameGreek = fields[1];
            String mainType = fields[2];
            String subType = fields[3];
            String description = fields[4];
            String imagePath = fields[5];

            // Create a GreekGod object and add it to the list
            GreekGod god = new GreekGod(nameEnglish, nameGreek, mainType, subType, description, imagePath);
            godsList.add(god);
        }

        br.close();
        return godsList;
    }

    /**
     * Parses a single line of CSV into an array of fields.
     * This method handles fields enclosed in quotes and commas within quoted fields.
     * 
     * @param line The CSV line to parse.
     * @return An array of parsed fields.
     */
    private String[] parseCSVLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;

        // Iterate over each character in the line
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '\"') {
                inQuotes = !inQuotes; // Toggle the inQuotes flag
            } else if (c == ',' && !inQuotes) {
                // If comma outside quotes, it's a field separator
                tokens.add(sb.toString().trim());
                sb.setLength(0); // Reset the StringBuilder
            } else {
                sb.append(c);
            }
        }

        // Add the last field
        tokens.add(sb.toString().trim());

        return tokens.toArray(new String[0]);
    }
}

