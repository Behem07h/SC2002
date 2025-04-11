package org.HDBManager.mainUI;

import org.HDBManager.ManagerController;
import java.util.Scanner;

public class MainMenuUI {
    public static void main(String[] args) {
        // Create the manager controller (which loads managers from CSV)
        ManagerController managerController = new ManagerController();
        boolean loaded = managerController.loadManagersFromCSV("data/hdbmanager.csv");
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
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input, please enter a number.");
                continue;
            }

            switch (choice) {
                case 1:
                    // Login procedure using the controller's authenticate method.
                    System.out.print("Enter UserID: ");
                    String userID = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    managerController.authenticate(userID, password);
                    break;
                case 2:
                    // Add a new manager manually.
                    System.out.print("Enter UserID: ");
                    String newUserID = scanner.nextLine();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    int age;
                    try {
                        System.out.print("Enter Age: ");
                        age = Integer.parseInt(scanner.nextLine());
                    } catch (Exception e) {
                        System.out.println("Invalid age input. Please enter a number.");
                        continue;
                    }
                    System.out.print("Enter Handle One Project: ");
                    String handleOneProject = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String newPassword = scanner.nextLine();
                    managerController.addManager(newUserID, name, age, handleOneProject, newPassword);
                    break;
                case 3:
                    // Change a manager's password.
                    System.out.print("Enter UserID: ");
                    String userIDToChange = scanner.nextLine();
                    System.out.print("Enter Old Password: ");
                    String oldPassword = scanner.nextLine();
                    System.out.print("Enter New Password: ");
                    String newPasswordChange = scanner.nextLine();
                    managerController.changePassword(userIDToChange, oldPassword, newPasswordChange);
                    break;
                case 4:
                    // Display all loaded managers.
                    managerController.displayManagers();
                    break;
                case 5:
                    System.out.println("Exiting system. Goodbye!");
                    scanner.close();
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }

    // Helper method to handle the input for the unique manager attribute: handleOneProject.
    // (This method is kept for future use if you decide to use it instead of reading directly from scanner.)
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
