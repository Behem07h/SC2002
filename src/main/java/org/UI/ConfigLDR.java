package org.UI;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ConfigLDR{
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
    public HashMap<String, String[]> ReadToArrMap(String filename) {
        File file = new File(filename);
        HashMap<String, String[]> cfg = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length < 2) { // key and item
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }
                String[] tmp = Arrays.copyOfRange(details, 1, details.length);
                tmp[0] = tmp[0].trim();
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

    public void saveCSV(String filename, Map<String, String[]> map) {
        StringBuilder line = new StringBuilder();
        for (String key : map.keySet()) {
            line.append(key).append("   ,").append(arr2str(map.get(key))).append(",\n");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename, false))) {
            writer.append(line.toString());
        } catch (
                IOException e) {
            System.out.println("Error writing " + filename + ": " + e.getMessage());
        }
    }

    public String arr2str(String[] data) {
        return String.join(",", data);
    }
}