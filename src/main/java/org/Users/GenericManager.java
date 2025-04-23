/**
 * Package for user management functionality.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.Users;

import org.UI.ConfigLDR;

import java.io.*;
import java.util.*;
import java.util.regex.*;
/**
 * Abstract base class for user management operations.
 * This class provides common functionality for managing different types of users
 * in the system, including loading from CSV, authentication, password management,
 * and validation. Specific user manager implementations must extend this class
 * and implement the abstract methods.
 *
 * @param <T> The specific user type that extends the user interface
 */
public abstract class GenericManager<T extends user> {

    /** Collection of users managed by this manager */
    protected List<T> userDB = new ArrayList<>();

    /** File path that the users were loaded from */
    private String filepath;

    /**
     * Loads user data from a CSV file.
     *
     * <p>The first line of the file is assumed to be a header and is skipped.
     * Each subsequent line is parsed into a user object using the parseUser method.
     * Age validation is performed for each user.</p>
     *
     * @param filename Path to the CSV file
     * @return true if users were loaded successfully, false otherwise
     */
    public boolean loadUsersFromCSV(String filename) {
        filepath = filename;
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

    /**
     * Parses a CSV line into a user object.
     *
     * <p>This abstract method must be implemented by subclasses to define
     * how a line from the CSV file is converted into the specific user type.</p>
     *
     * @param line The CSV line to parse
     * @return A user object of type T, or null if parsing fails
     */
    protected abstract T parseUser(String line);

    /**
     * Authenticates a user based on user ID and password.
     *
     * <p>This method searches for a user with the provided ID and password
     * in the user database.</p>
     *
     * @param userID The user ID to authenticate
     * @param password The password to verify
     * @return The authenticated user object, or null if authentication fails
     */
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

    /**
     * Changes the password for a user.
     *
     * <p>Finds the user with the specified ID and old password,
     * then updates the password if found.</p>
     *
     * @param userID The ID of the user
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     */
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

    /**
     * Validates a user ID against a predefined pattern.
     *
     * <p>The default implementation checks if the user ID matches the format
     * of a Singapore NRIC (e.g., S1234567A).</p>
     *
     * @param userID The user ID to validate
     * @return true if the user ID is valid, false otherwise
     */
    public boolean isValidUserID(String userID) {
        String regex = "^[A-Z][0-9]{7}[A-Za-z]$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(userID);
        return matcher.matches();
    }

    /**
     * Validates the age of a user.
     *
     * <p>Checks that the age is non-negative and at least 18 years.</p>
     *
     * @param age The age to validate
     * @return true if the age is valid, false otherwise
     */
    public boolean isValidAge(int age) {
        if (age < 0) {
            System.out.println("Error: Age cannot be negative.");
            return false;
        }
        return age >= 18;
    }

    /**
     * Displays all users in the database.
     *
     * <p>Prints a list of all users managed by this manager.</p>
     */
    public void displayUsers() {
        System.out.println("User List:");
        for (T user : userDB) {
            System.out.println(user);
        }
    }

    /**
     * Finds a user by their ID.
     *
     * <p>Searches the user database for a user with the specified ID.</p>
     *
     * @param userID The ID of the user to find
     * @return The user object if found, null otherwise
     */
    public T findById(String userID) {
        for (T u : userDB) {
            if (u.getUserID().equals(userID)) {
                return u;
            }
        }
        return null;
    }

    /**
     * Adds a new user to the system.
     *
     * <p>This abstract method must be implemented by subclasses to define
     * how a new user is created and added to the database.</p>
     *
     * @param userID The unique ID for the user
     * @param username The login username for the user
     * @param age The user's age in years
     * @param extra1 Additional parameter specific to the user type
     * @param extra2 Additional parameter specific to the user type
     */
    public abstract void addUser(String userID, String username, int age, String extra1, String extra2);

    /**
     * Stores all users in the database to a file
     *
     * <p>Stores all users in userDB to the file they were loaded from.</p>
     */
    public void store() {
        Map<String,String[]> reg_map = new HashMap<>();
        for (user usr : userDB) {
            String[] items = {usr.getUsername(),usr.getPassword(),usr.getMaritalStatus(), String.valueOf(usr.getAge()), String.valueOf(usr.getPerms())};
            reg_map.put(usr.getUserID(),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(filepath,reg_map);
    }
}
