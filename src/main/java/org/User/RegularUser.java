package org.User;

public class RegularUser implements User {
    private String userID;
    private String password;
    private int age;
    private int maritalStatus;
    private UserPermissions perms;

    public RegularUser(String userID, String password, int age, int maritalStatus) {
        this.userID = userID;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
        this.perms = new UserPermissions(0); // Default user type: applicant
    }

    public String getUserID() {
        return userID;
    }

    public String getPassword() {
        return password;
    }

    public int getAge() {
        return age;
    }

    public int getMaritalStatus() {
        return maritalStatus;
    }

    public UserPermissions getPermissions() {
        return perms;
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {
            this.password = newPassword;
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Incorrect old password.");
        }
    }

    @Override
    public String toString() {
        return "UserID: " + userID + ", Age: " + age + ", Marital Status: " + (maritalStatus == 0 ? "Single" : "Married");
    }
}