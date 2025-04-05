package org.Applicant;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.Applicant.Applicant;
import org.Applicant.Enquiry;

public class ApplicantManager {
    private List<Applicant> usr_db = new ArrayList<>();

    // Constructor to load users from a CSV file upon instantiation
    public boolean loadUsersFromCSV(String filename) {
        File file = new File(filename);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length != 5) {  // Now expect only 5 columns (userID, username, password, maritalStatus, age)
                    System.out.println("Skipping malformed line: " + line);
                    continue; // Skip lines that do not have exactly 5 columns
                }
                try {
                    int age = Integer.parseInt(details[4]); // Ensure age is an integer
                    // Default permission level since it's no longer in the CSV
                    Applicant.PermissionLevel perms = Applicant.PermissionLevel.NONE;
                    // Debugging output to check what's being loaded
                    System.out.println("Loaded user: " + details[0] + " | Password: " + details[2]); // Print out loaded user info
                    usr_db.add(new Applicant(details[0], details[1], details[2], details[3], age, perms));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid data format in line: " + line);
                }
            }
            System.out.println("Users loaded successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            return false;
        }
    }
    
    

    // Method to authenticate a user
    public void authenticate(String userID, String password) {
        for (Applicant user : usr_db) {
            // Debugging output to check what's being compared
            System.out.println("Comparing UserID: " + user.getUserID() + " | Input UserID: " + userID);
            System.out.println("Comparing Password: " + user.getPassword() + " | Input Password: " + password);
    
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + user.getUsername());
                return;
            }
        }
        System.out.println("Invalid UserID or Password.");
    }
    

    // Method to add a user manually
    public void add_user(String userID, String username, int age, String maritalStatus) {
        if (age < 0) {
            System.out.println("Invalid age. Age cannot be negative.");
            return;
        }

        // Default permissions or assign based on context
        Applicant.PermissionLevel perms = Applicant.PermissionLevel.NONE;
        usr_db.add(new Applicant(userID, username, "defaultPassword", maritalStatus, age, perms));  // Create new applicant
        System.out.println("User added: " + userID);
    }

    // Utility method to validate user ID
    public boolean isValidUserID(String userID) {
        String regex = "^[ST][0-9]{7}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userID);
        return matcher.matches();
    }

    // Method to change user's password
    public void changePassword(String userID, String oldPassword, String newPassword) {
        for (Applicant user : usr_db) {
            if (user.getUserID().equals(userID) && user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                System.out.println("Password changed successfully.");
                return;
            }
        }
        System.out.println("User not found or old password incorrect.");
    }

    // Method to display all users
    public void displayUsers() {
        System.out.println("User List:");
        for (Applicant user : usr_db) {
            System.out.println(user);
        }
    }

    public List<Enquiry> getEnquiriesByUserID(String userID) {
        return usr_db.stream()
                .filter(user -> user.getUserID().equals(userID))
                .findFirst()
                .map(Applicant::getEnquiries)
                .orElse(new ArrayList<>());
    }

    public void resolveEnquiry(String userID, String enquiryID, String response) {
        Applicant applicant = usr_db.stream()
                .filter(u -> u.getUserID().equals(userID))
                .findFirst()
                .orElse(null);

        if (applicant != null) {
            applicant.resolveEnquiry(enquiryID, response);
        }
    }

    // Method to get all unresolved enquiries (for managers/officers)
    public List<Enquiry> getAllUnresolvedEnquiries() {
        return usr_db.stream()
                .flatMap(user -> user.getEnquiries().stream())
                .filter(enquiry -> !enquiry.isResolved())
                .collect(Collectors.toList());
    }

}









