package org.Users.HDBOfficer;

import org.Users.user;

import java.util.Scanner;

public class HDBOfficer implements user {
    private String userID;
    private String username;
    private String password; // Plain-text password
    private String department;  // Officer's department
    private int age;
    private PermissionLevel perms = PermissionLevel.OFFICER;  // Default to PermissionLevel.NONE

    // Constructor
    public HDBOfficer(String userID, String username, String password, String department, int age, PermissionLevel perms) {
        this.userID = userID;
        this.username = username;
        this.password = password;  // Plain-text password
        this.department = department;
        this.age = age;
        this.perms = perms;
    }

    // Getters and Setters
    @Override
    public String getUserID() {
        return userID;
    }

    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getPassword() {
        return password;  // Plain-text password
    }

    @Override
    public void setPassword(String password) {
        this.password = password;  // Set plain-text password
    }

    @Override
    public String getMaritalStatus() {
        return department; // Department instead of marital status for HDBOfficer
    }

    @Override
    public void setMaritalStatus(String department) {
        this.department = department;
    }

    @Override
    public int getAge() {
        return age;
    }

    @Override
    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public PermissionLevel getPerms() {
        return null;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {  // Plain-text password comparison
            this.password = newPassword;  // Set new password
            return true;
        }
        return false;
    }

    @Override
    public String[] act(String action, Scanner sc) {
        return new String[0];
    }

    // Enum for Permission Levels
}
