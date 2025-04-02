package org.Applicant;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplicantManager {
    private List<RegularApplicant> usr_db = new ArrayList<>();

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
                    usr_db.add(new RegularApplicant(details[0], details[4], Integer.parseInt(details[2]), details[3]));
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
        for (RegularApplicant user : usr_db) {
            if (user.getUserID().equals(userID) && user.getPassword().equals(password)) {
                System.out.println("Login successful! Welcome, " + user.getUserID());
                return;
            }
        }
        System.out.println("Invalid UserID or Password.");
    }

    public void add_user(String userID, int age, String maritalStatus, Scanner scanner) {
    if (age < 0) {
        System.out.println("Invalid age. Age cannot be negative.");
        return;
    }

    // Validate the UserID using regular expression
    while (!isValidUserID(userID)) {
        System.out.println("Invalid UserID. The UserID must start with 'S' or 'T', followed by 7 digits and ending with 1 alphabet.");
        System.out.print("Enter UserID (NRIC): ");
        userID = scanner.nextLine();
        System.out.println();
    }

    // Consume any leftover newline character after nextInt() (if it's in use)
    scanner.nextLine(); // this clears the buffer from nextInt()

    // Map "0" to "Single" and "1" to "Married"
    if (maritalStatus.equals("0")) {
        maritalStatus = "Single";
    } else if (maritalStatus.equals("1")) {
        maritalStatus = "Married";
    } else {
        // Keep prompting until valid marital status is entered
        while (!maritalStatus.equals("Single") && !maritalStatus.equals("Married")) {
            System.out.println("Invalid marital status. Enter '0' for Single or '1' for Married.");
            maritalStatus = scanner.nextLine(); // Wait for user input and immediately validate
            if (maritalStatus.equals("0")) {
                maritalStatus = "Single";
                break;
            } else if (maritalStatus.equals("1")) {
                maritalStatus = "Married";
                break;
            }
        }
    }

    // Add the new user to the database
    usr_db.add(new RegularApplicant(userID, "password", age, maritalStatus));
    System.out.println("User added: " + userID);
}

// Method to validate UserID
public boolean isValidUserID(String userID) {
    // Regular expression to check if the UserID matches the required format
    String regex = "^[ST][0-9]{7}[A-Za-z]$";
    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(userID);
    return matcher.matches();  // Returns true if the UserID matches the pattern, false otherwise
}
    
    
    
    

    public void changePassword(String userID, String oldPassword, String newPassword) {
        for (RegularApplicant user : usr_db) {
            if (user.getUserID().equals(userID)) {
                user.changePassword(oldPassword, newPassword);
                return;
            }
        }
        System.out.println("User not found.");
    }

    public void displayUsers() {
        System.out.println("User List:");
        for (RegularApplicant user : usr_db) {
            System.out.println(user);
        }
    }
}