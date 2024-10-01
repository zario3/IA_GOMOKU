import java.io.*;
import java.util.*;

public class FileSorter {

    // File path for the text file
    private static String FILE_PATH;

    // Method to add a line of data and sort the file
    public static void addLineToFile(String path, String newLine) throws IOException {
        FILE_PATH = path;
        Map<String, String> linesMap = new TreeMap<>();  // Use a TreeMap to maintain sorted order

        // Read existing lines from the file if it exists
        File file = new File(FILE_PATH);
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String key = line.split(":")[0];
                linesMap.put(key, line); 
            }
            reader.close();
        }


        String newKey = newLine.split(":")[0];


        if (!linesMap.containsKey(newKey)) {

            linesMap.put(newKey, newLine);


            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            for (String sortedLine : linesMap.values()) {
                writer.write(sortedLine);
                writer.newLine();
            }
            writer.close();
            System.out.println("Added new line: " + newLine);
        } else {
            System.out.println("Line with key '" + newKey + "' already exists. Skipping addition.");
        }
    }
}
