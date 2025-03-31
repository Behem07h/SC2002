package org.User;

import java.io.*;
import java.util.*;

public class UserManager {
    private List<RegularUser> usr_db = new ArrayList<>();

    public boolean loadUsersFromCSV(String filename) {
        try {
            File file = new File(filename);
            System.out.println("Looking for file at: " + file.getAbsolutePath());
            
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] details = line.split(",");
                if (details.length == 5) {
                    usr_db.add(new RegularUser(details[0], details[4], Integer.parseInt(details[2]), details[3].equals("Single") ? 0 : 1));
                }
            }
            System.out.println("Users loaded successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            return false;
        }
    }
    
    public void authenticate(String userID, String password) {
        for (RegularUser user : usr_db) {
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + user.getUserID());
                return;
            }
        }
        System.out.println("Invalid UserID or Password.");
    }

    public void add_user(String userID, int age, int maritalStatus) {
        usr_db.add(new RegularUser(userID, "password", age, maritalStatus));
        System.out.println("User added: " + userID);
    }

    public void changePassword(String userID, String oldPassword, String newPassword) {
        for (RegularUser user : usr_db) {
            if (user.getUserID().equals(userID)) {
                user.changePassword(oldPassword, newPassword);
                return;
            }
        }
        System.out.println("User not found.");
    }

    public void displayUsers() {
        System.out.println("User List:");
        for (RegularUser user : usr_db) {
            System.out.println(user);
        }
    }
}