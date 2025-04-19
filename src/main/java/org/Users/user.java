package org.Users;

import java.util.Scanner;

public interface user {

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

    PermissionLevel getPerms();


    boolean changePassword(String oldPassword, String newPassword);

    // Enum for Permission Levels
    public enum PermissionLevel {
        APPLICANT,   // Read permission
        OFFICER,  // Write permission
        MANAGER,  // Administrator privileges
        NONE    // No special permissions (default value)
    }
}


