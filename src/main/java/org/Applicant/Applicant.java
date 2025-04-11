package org.Applicant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Applicant implements user {
    private String userID;
    private String username;
    private String password;  // Plain-text password
    private String maritalStatus;
    private int age;
    private PermissionLevel perms;  // Default permission level

    // Constructor
    public Applicant(String userID, String username, String password, String maritalStatus, int age, PermissionLevel perms) {
        this.userID = userID;
        this.username = username;
        this.password = password;  // Plain-text password
        this.maritalStatus = maritalStatus;
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
        return password;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getMaritalStatus() {
        return maritalStatus;
    }

    @Override
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
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
    public void act(String something) {
        // Simple behavior — print something or log an action
        System.out.println("Applicant [" + username + "] is acting on: " + something);
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
    public String toString() {
        return "Applicant{username='" + username + "', userID='" + userID + "'}";
    }

    // Enum for Permission Levels
    public enum PermissionLevel {
        READ,   // Read permission
        WRITE,  // Write permission
        ADMIN,  // Administrator privileges
        NONE    // No special permissions (default value)
    }
}








