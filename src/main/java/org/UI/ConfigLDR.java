/**
 * ConfigLDR is a configuration loader class for handling CSV configuration files.
 *
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.UI;

import java.io.*;
import java.util.*;

/**
 * It provides methods to read configuration data from CSV files into maps and save map data back to CSV files.
 */
public class ConfigLDR{
    /**
     * Reads a CSV file and loads its contents into a HashMap with string keys and values.
     * The method expects a CSV format with two columns (key, value).
     * The first line of the file is assumed to be a header and is skipped.
     *
     * @param filename the path to the CSV file to read
     * @return a HashMap containing the key-value pairs from the CSV file
     */
    public HashMap<String, String> ReadToMap(String filename) {
        File file = new File(filename);
        HashMap<String, String> cfg = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length != 2) {  // key and item
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }
                cfg.put(details[0].trim(), details[1].trim());
            }
            System.out.println(filename + " loaded successfully.");
            return cfg;
        } catch (
                IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
            return cfg;
        }
    }
    /**
     * Reads a CSV file and loads its contents into a HashMap with string keys and string array values.
     * The method expects a CSV format with multiple columns, where the first column is the key
     * and all subsequent columns form an array of values.
     * The first line of the file is assumed to be a header and is skipped.
     * Values equal to "NONE" in CSV are converted to empty strings.
     *
     * @param filename the path to the CSV file to read
     * @return a HashMap containing keys and their associated array of values from the CSV file
     */
    public HashMap<String, String[]> ReadToArrMap(String filename) {
        //Scanner sc = new Scanner(System.in); //for debugging purposes
        //sc.nextLine();
        File file = new File(filename);
        HashMap<String, String[]> cfg = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            System.out.println(br.readLine()); // Skip header line


            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length < 2) { // key and item
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }
                String[] tmp = Arrays.copyOfRange(details, 1, details.length);
                for (int i = 0; i < tmp.length; i++) {
                    tmp[i] = tmp[i].trim();
                    if (Objects.equals(tmp[i], "NONE")) {
                        tmp[i] = "";
                    }
                }
                System.out.println(Arrays.toString(tmp));
                cfg.put(details[0].trim(), tmp);
            }
            System.out.println(filename + " loaded successfully.");
            return cfg;
        } catch (
                IOException e) {
            System.out.println("Error loading " + filename + ": " + e.getMessage());
            return cfg;
        }
    }

    /**
     * Saves a Map with string keys and string array values to a CSV file.
     * The method preserves the header line from the original file.
     * Empty strings in arrays are converted to "NONE" in the CSV output.
     *
     * @param filename the path to the CSV file to write to
     * @param map the Map containing key-value pairs to save
     */
    public void saveCSV(String filename, Map<String, String[]> map) {
        String headerLine = "";
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            headerLine = br.readLine();
            headerLine = headerLine + "\n";
        } catch (IOException e) {
            System.out.println("Error loading file header: " + filename + ": " + e.getMessage());
        }

        StringBuilder line = new StringBuilder();
        for (String key : map.keySet()) {
            line.append(key).append("   ,").append(arr2str(map.get(key))).append(",\n");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            writer.append(headerLine);
            writer.append(line.toString());
        } catch (
                IOException e) {
            System.out.println("Error writing " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Converts a string array to a comma-separated string.
     * Empty strings in the array are converted to "NONE" before joining.
     *
     * @param data the string array to convert
     * @return a comma-separated string representation of the array
     */
        public String arr2str(String[] data) {
        for (int i = 0; i < data.length; i++) {
            if (data[i].isEmpty()) {
                data[i] = "NONE";
            }
        }
        return String.join(", ", data);
    }
}