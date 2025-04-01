package org.User;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserManager userManager = new UserManager();
        userManager.loadUsersFromCSV("data/users.csv");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Login\n2. Add User\n3. Change Password\n4. Display Users\n5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter UserID (NRIC): ");
                    String userID = scanner.nextLine();
                    System.out.print("Enter Password: ");
                    String password = scanner.nextLine();
                    userManager.authenticate(userID, password);
                    break;
                case 2:
                    System.out.print("Enter UserID (NRIC): ");
                    String newUserID = scanner.nextLine();
                    System.out.print("Enter Age: ");
                    int age = scanner.nextInt();
                    System.out.print("Enter Marital Status (0 = Single, 1 = Married): ");
                    userManager.add_user(newUserID, age, maritalStatus);
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
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}