package org.HDBManager.mainUI;

import org.HDBManager.HDBManagerManager;
import java.util.Scanner;

public class MainMenuUI {
    public static void main(String[] args) {
        HDBManagerManager managerManager = new HDBManagerManager();
        boolean loaded = managerManager.loadManagersFromCSV("data/hdbmanager.csv");
        if (!loaded) {
            System.out.println("Warning: Failed to load managers from CSV file.");
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n===== HDB Manager Management System =====");
            System.out.println("1. Login");
            System.out.println("2. Add Manager");
            System.out.println("3. Change Password");
            System.out.println("4. Display Managers");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");

            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
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
                    managerManager.authenticate(userID, password);
                    break;
                case 2:
                    System.out.print("Enter UserID: ");
                    String newUserID = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();  // Manager's name
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
                    System.out.print("Enter Handle One Project: ");
                    String handleOneProject = getHandleOneProject(scanner);
                    System.out.print("Enter Password: ");
                    String newPassword = scanner.nextLine();  // Manager's password

                    // Add the manager with the provided details
                    managerManager.addManager(newUserID, name, age, handleOneProject, newPassword);
                    break;
                case 3:
                    System.out.print("Enter UserID: ");
                    String userIDToChange = scanner.nextLine();
                    System.out.print("Enter Old Password: ");
                    String oldPassword = scanner.nextLine();
                    System.out.print("Enter New Password: ");
                    String newPasswordChange = scanner.nextLine();
                    managerManager.changePassword(userIDToChange, oldPassword, newPasswordChange);
                    break;
                case 4:
                    managerManager.displayManagers();
                    break;
                case 5:
                    System.out.println("Thank you for using HDB Manager Management System. Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    // Helper method to handle the input for the unique manager attribute: handleOneProject
    private static String getHandleOneProject(Scanner scanner) {
        String handleOneProject = "";
        while (true) {
            String input = scanner.nextLine();
            if (input != null && !input.trim().isEmpty()) {
                handleOneProject = input;
                break;
            } else {
                System.out.println("Invalid input for project. Please enter a valid project name.");
            }
        }
        return handleOneProject;
    }
}


