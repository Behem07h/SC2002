package org.Users;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public abstract class GenericManager<T extends user> {
    protected List<T> userDB = new ArrayList<>();
    //todo: store users to csv
    // Load users from CSV
    public boolean loadUsersFromCSV(String filename) {
        File file = new File(filename);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                try {
                    T user = parseUser(line);
                    if (user != null) {
                        if (isValidAge(user.getAge())) {
                            userDB.add(user);
                        } else {
                            System.out.println("Invalid age for user: " + user.getUserID());
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line);
                }
            }
            System.out.println("Users loaded successfully.");
            return true;
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
            return false;
        }
    }

    // Abstract: parse CSV line into user object
    protected abstract T parseUser(String line);

    // Login
    public T authenticate(String userID, String password) {
        System.out.println("generic authenticate");
        for (T user : userDB) {
            System.out.println(user.getUserID() + " " + user.getPassword());
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + user.getUsername());
                return user;
            }
        }
        System.out.println("Invalid UserID or Password.");
        return null;
    }

    // Change password
    public void changePassword(String userID, String oldPassword, String newPassword) {
        for (T user : userDB) {
            if (user.getUserID().equals(userID) && user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                System.out.println("Password changed successfully.");
                return;
            }
        }
        System.out.println("User not found or old password incorrect.");
    }

    // Default ID regex validation (e.g. NRIC)
    public boolean isValidUserID(String userID) {
        String regex = "^[A-Z][0-9]{7}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userID);
        return matcher.matches();
    }

    // Validate age (must be >= 18 and >= 0)
    public boolean isValidAge(int age) {
        if (age < 0) {
            System.out.println("Error: Age cannot be negative.");
            return false;
        }
        return age >= 18;
    }

    // Display user list
    public void displayUsers() {
        System.out.println("User List:");
        for (T user : userDB) {
            System.out.println(user);
        }
    }

    // Abstract: manually add user
    public abstract void addUser(String userID, String username, int age, String extra1, String extra2);
}
