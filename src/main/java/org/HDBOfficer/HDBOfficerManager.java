package org.HDBOfficer;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HDBOfficerManager {
    private List<HDBOfficer> officer_db = new ArrayList<>();

    // Constructor to load officers from a CSV file upon instantiation
    public boolean loadOfficersFromCSV(String filename) {
        File file = new File(filename);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length != 5) {  // Expecting 5 columns: username, userID, age, maritalStatus, password
                    System.out.println("Skipping malformed line: " + line);
                    continue;
                }
                try {
                    String username = details[0];
                    String userID = details[1];
                    int age = Integer.parseInt(details[2]);  // Ensure age is an integer
                    String maritalStatus = details[3];  // maritalStatus mapped to department in HDBOfficer
                    String password = details[4];
                    
                    // Default permission level for now
                    HDBOfficer.PermissionLevel perms = HDBOfficer.PermissionLevel.NONE;
                    
                    // Debugging output to check what's being loaded
                    officer_db.add(new HDBOfficer(userID, username, password, maritalStatus, age, perms));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid data format in line: " + line);
                }
            }
            System.out.println("Officers loaded successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Error loading officers: " + e.getMessage());
            return false;
        }
    }

    // Method to authenticate an officer
    public void authenticate(String userID, String password) {
        for (HDBOfficer officer : officer_db) {
            if (officer.getUserID().equals(userID) && officer.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + officer.getUsername());
                return;
            }
        }
        System.out.println("Invalid UserID or Password.");
    }

    // Method to add a new officer manually with a password
    public void addOfficer(String userID, String username, int age, String maritalStatus, String password) {
        if (age < 0) {
            System.out.println("Invalid age. Age cannot be negative.");
            return;
        }
        // Default permissions or assign based on context
        HDBOfficer.PermissionLevel perms = HDBOfficer.PermissionLevel.NONE;
        officer_db.add(new HDBOfficer(userID, username, password, maritalStatus, age, perms));
        System.out.println("Officer added: " + userID);
    }

    // Utility method to validate officer ID
    public boolean isValidUserID(String userID) {
        String regex = "^[A-Z][0-9]{7}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userID);
        return matcher.matches();
    }

    // Method to change officer's password
    public void changePassword(String userID, String oldPassword, String newPassword) {
        for (HDBOfficer officer : officer_db) {
            if (officer.getUserID().equals(userID) && officer.getPassword().equals(oldPassword)) {
                officer.setPassword(newPassword);
                System.out.println("Password changed successfully.");
                return;
            }
        }
        System.out.println("Officer not found or old password incorrect.");
    }

    // Method to display all officers
    public void displayOfficers() {
        System.out.println("Officers loaded successfully.");
        for (HDBOfficer officer : officer_db) {
            System.out.println("Loaded officer: " + officer.getUsername() + " | UserID: " + officer.getUserID());
        }
    }
}
