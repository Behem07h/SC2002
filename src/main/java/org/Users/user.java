package org.Users;

import java.util.Scanner;

public interface user {
    PermissionLevel perms = PermissionLevel.NONE;


    String getUserID();

    void setUserID(String userID);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getMaritalStatus();

    void setMaritalStatus(String maritalStatus);

    int getAge();

    void setAge(int age);


    boolean changePassword(String oldPassword, String newPassword);

    // Enum for Permission Levels
    public enum PermissionLevel {
        READ,   // Read permission
        WRITE,  // Write permission
        ADMIN,  // Administrator privileges
        NONE    // No special permissions (default value)
    }

    String[] act(String action, Scanner sc); //wrapper fn that handles user inputs for all actions
}


