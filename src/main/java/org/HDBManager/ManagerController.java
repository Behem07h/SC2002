package org.HDBManager;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HDBManagerManager {
    private List<HDBManager> manager_db = new ArrayList<>();

    // Constructor to load managers from a CSV file upon instantiation
    public boolean loadManagersFromCSV(String filename) {
        File file = new File(filename);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length != 5) {  // Expecting 5 columns: username, userID, age, handleOneProject, password
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }
                try {
                    String username = details[0];
                    String userID = details[1];
                    int age = Integer.parseInt(details[2]);  // Ensure age is an integer
                    String handleOneProject = details[3];
                    String password = details[4];
                    
                    // Default permission level for now
                    HDBManager.PermissionLevel perms = HDBManager.PermissionLevel.NONE;
                    
                    // Add the manager to the database
                    manager_db.add(new HDBManager(userID, username, password, handleOneProject, perms));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid data format in line: " + line);
                }
            }
            System.out.println("Managers loaded successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Error loading managers: " + e.getMessage());
            return false;
        }
    }

    // Method to authenticate a manager
    public void authenticate(String userID, String password) {
        for (HDBManager manager : manager_db) {
            if (manager.getUserID().equals(userID) && manager.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + manager.getUsername());
                return;
            }
        }
        System.out.println("Invalid UserID or Password.");
    }

    // Method to add a new manager manually with a password
    public void addManager(String userID, String username, int age, String handleOneProject, String password) {
        if (age < 0) {
            System.out.println("Invalid age. Age cannot be negative.");
            return;
        }
        // Default permissions or assign based on context
        HDBManager.PermissionLevel perms = HDBManager.PermissionLevel.NONE;
        manager_db.add(new HDBManager(userID, username, password, handleOneProject, perms));
        System.out.println("Manager added: " + userID);
    }

    // Utility method to validate manager ID
    public boolean isValidUserID(String userID) {
        String regex = "^[A-Z][0-9]{7}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userID);
        return matcher.matches();
    }

    // Method to change a manager's password
    public void changePassword(String userID, String oldPassword, String newPassword) {
        for (HDBManager manager : manager_db) {
            if (manager.getUserID().equals(userID) && manager.getPassword().equals(oldPassword)) {
                manager.setPassword(newPassword);
                System.out.println("Password changed successfully.");
                return;
            }
        }
        System.out.println("Manager not found or old password incorrect.");
    }

    // Method to display all loaded managers
    public void displayManagers() {
        System.out.println("Managers loaded successfully.");
        for (HDBManager manager : manager_db) {
            System.out.println("Loaded manager: " + manager.getUsername() + " | UserID: " + manager.getUserID());
        }
    }
}


