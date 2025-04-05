package org.HDBOfficer.mainUI;

import org.HDBOfficer.HDBOfficerManager;
import java.util.Scanner;

public class MainMenuUI {
    public static void main(String[] args) {
        HDBOfficerManager officerManager = new HDBOfficerManager();
        boolean loaded = officerManager.loadOfficersFromCSV("data/hdbofficer.csv");
        if (!loaded) {
            System.out.println("Warning: Failed to load officers from CSV file.");
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== HDB Officer Management System =====");
            System.out.println("1. Login\n2. Add Officer\n3. Change Password\n4. Display Officers\n5. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline
            } catch (Exception e) {
                System.out.println("Please enter a valid number.");
                scanner.nextLine(); // Clear the invalid input
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter UserID: ");
                    String userID = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    officerManager.authenticate(userID, password);
                    break;
                case 2:
                    System.out.print("Enter UserID: ");
                    String newUserID = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();  // Asking for the officer's name
                    int age;
                    try {
                        System.out.print("Enter Age: ");
                        age = scanner.nextInt();
                        scanner.nextLine(); // Consume newline
                    } catch (Exception e) {
                        System.out.println("Invalid age input. Please enter a number.");
                        scanner.nextLine(); // Clear the invalid input
                        continue;
                    }

                    System.out.print("Enter Marital Status (0 = Single, 1 = Married): ");
                    String maritalStatus = getMaritalStatus(scanner);

                    System.out.print("Enter Password: ");
                    String newPassword = scanner.nextLine();  // Asking for the officer's password

                    // Now add the officer with the name, marital status, and password included
                    officerManager.addOfficer(newUserID, name, age, maritalStatus, newPassword);
                    break;
                case 3:
                    System.out.print("Enter UserID: ");
                    String userIDToChange = scanner.nextLine();
                    System.out.print("Enter Old Password: ");
                    String oldPassword = scanner.nextLine();
                    System.out.print("Enter New Password: ");
                    String newPasswordChange = scanner.nextLine();
                    officerManager.changePassword(userIDToChange, oldPassword, newPasswordChange);
                    break;
                case 4:
                    officerManager.displayOfficers();
                    break;
                case 5:
                    System.out.println("Thank you for using HDB Officer Management System. Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Helper method to handle marital status input and validation
    private static String getMaritalStatus(Scanner scanner) {
        String maritalStatus = "";
        while (true) {
            String maritalStatusInput = scanner.nextLine();
            if ("0".equals(maritalStatusInput)) {
                maritalStatus = "Single";
                break;
            } else if ("1".equals(maritalStatusInput)) {
                maritalStatus = "Married";
                break;
            } else {
                System.out.println("Invalid input for marital status. Please enter 0 for Single or 1 for Married.");
            }
        }
        return maritalStatus;
    }
}
