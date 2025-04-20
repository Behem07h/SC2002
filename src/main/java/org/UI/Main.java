package org.UI;

import org.Users.Applicant.ApplicantManager;
import org.Users.GenericManager;
import org.Users.HDBManager.ManagerController;
import org.Users.HDBOfficer.HDBOfficerManager;
import org.Users.user;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int choice;
        GenericManager<user> manager;
        do {
            System.out.println("1. Applicant\n2. HDB Officer\n3. HDB Manager\n4. Exit");
            choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    manager = new ApplicantManager();
                    new loginScreen(scanner, manager, "data/applicant.csv");
                    return;
                    //initialise generic manager as applicant manager
                case 2:
                    manager = new HDBOfficerManager();
                    new loginScreen(scanner, manager, "data/hdbofficer.csv");
                    return;
                    //initialise generic manager as officer manager
                case 3:
                    manager = new ManagerController();
                    new loginScreen(scanner, manager, "data/hdbmanager.csv");
                    return;
                    //initialise generic manager as manager of manager
                default:
                    System.out.println("Invalid option.");
            }
        } while (choice < 4);
    }
}

class loginScreen {
    public loginScreen(Scanner scanner, GenericManager<user> userManager, String filename) { //todo: pass manager as generic superclass
        boolean loaded = userManager.loadUsersFromCSV(filename);
        if (!loaded) {
            System.out.println("Warning: Failed to load users from CSV file.");
        }



        while (true) {
            System.out.println("\n===== BTO Management System =====");
            System.out.println("1. Login\n2. Add User\n3. Change Password\n4. Display Users\n5. Exit");
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
                    System.out.print("Enter UserID (NRIC): ");
                    String userID = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    user myuser = userManager.authenticate(userID, password);
                    if (myuser != null) {
                        UI mainUI = new UI("data/ui_cfg", myuser, scanner);
                        mainUI.load_ui();
                    }
                    break;
                case 2:
                    System.out.print("Enter UserID (NRIC): ");
                    String newUserID = scanner.nextLine();
                    System.out.print("Enter Username: ");
                    String newUsername = scanner.nextLine();

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

                    // Corrected the method call with proper parameters
                    userManager.addUser(newUserID, newUsername, age, maritalStatus, "");
                    break;
                case 3:
                    System.out.print("Enter UserID (NRIC): ");
                    String userIDToChange = scanner.nextLine();
                    System.out.print("Enter Old Password: ");
                    String oldPassword = scanner.nextLine();
                    System.out.print("Enter New Password: ");
                    String newPassword = scanner.nextLine();
                    userManager.changePassword(userIDToChange, oldPassword, newPassword);
                    break;
                case 4:
                    userManager.displayUsers();
                    break;
                case 5:
                    System.out.println("Thank you for using BTO Management System. Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
    private static String getMaritalStatus(Scanner scanner) {
        String maritalStatus;
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
    } // Helper method to handle marital status input and validation
}
