package org.Users.HDBManager;

import org.Users.user;  // Adjust the import as necessary if your user interface is in a different package

import java.util.Scanner;

public class HDBManager implements user {
    private String userID;
    private String username;
    private String password;  // Plain-text password (note: consider hashing in production)
    
    // Unique attribute for HDBManager: handles one project at a time
    private String handleOneProject;
    
    // Permission level for the manager
    private final PermissionLevel perms;
    
    // Additional fields from Excel
    private String maritalStatus;
    private int age;

    private String activeProjectId; //todo: active projects can never overlap with the previous
    
    // Constructor for HDBManager with age and maritalStatus included
    public HDBManager(String userID, String username, String password, String maritalStatus, int age, PermissionLevel perms, String activeProjectId) {
        this.userID = userID;
        this.username = username;
        this.password = password;  // Plain-text password
        this.maritalStatus = maritalStatus;
        this.age = age;
        this.perms = perms;
        this.activeProjectId = activeProjectId;
    }
    
    // Getter and Setter for handleOneProject
    public String getActiveProjectId() {
        return activeProjectId;
    }
    
    public void setActiveProjectId(String activeProjectId) {
        this.activeProjectId = activeProjectId;
    }
    
    // Getters and Setters for age and maritalStatus
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
    
    // Implementing methods required by the 'user' interface
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
    public PermissionLevel getPerms() {
        return perms;
    }
    
    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {  // Plain-text comparison
            this.password = newPassword;
            return true;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return "HDBManager{" +
               "username='" + username + '\'' +
               ", userID='" + userID + '\'' +
               ", handleOneProject='" + handleOneProject + '\'' +
               ", maritalStatus='" + maritalStatus + '\'' +
               ", age=" + age +
               ", permissionLevel=" + perms +
               '}';
    }
}
