/**
 * Package for user-related functionality and interfaces.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 */
package org.Users;

import java.util.Scanner;
/**
 * Defines the common interface for all user types in the system.
 * This interface establishes the contract for basic user operations including
 * identity management, authentication, and personal information handling. All user
 * implementations must provide these core functionalities.
 */
public interface user {

    /**
     * Gets the unique ID for this user.
     *
     * @return The user ID as a String
     */
    String getUserID();
    /**
     * Sets the unique ID for this user.
     *
     * @param userID The new user ID to set
     */
    void setUserID(String userID);

    /**
     * Gets the username for this user.
     *
     * @return The username as a String
     */
    String getUsername();

    /**
     * Sets the username for this user.
     *
     * @param username The new username to set
     */
    void setUsername(String username);

    /**
     * Gets the password for this user.
     *
     * @return The password as a String
     */
    String getPassword();

    /**
     * Sets the password for this user.
     *
     * @param password The new password to set
     */
    void setPassword(String password);

    /**
     * Gets the marital status for this user.
     *
     * @return The marital status as a String
     */
    String getMaritalStatus();

    /**
     * Sets the marital status for this user.
     *
     * @param maritalStatus The new marital status to set
     */
    void setMaritalStatus(String maritalStatus);

    /**
     * Gets the age of this user.
     *
     * @return The age as an integer
     */
    int getAge();

    /**
     * Sets the age for this user.
     *
     * @param age The new age to set
     */
    void setAge(int age);

    /**
     * Gets the permission level assigned to this user.
     *
     * @return The permission level enum value
     */
    PermissionLevel getPerms();

    /**
     * Changes the password for this user.
     *
     * <p>This method verifies the old password before setting the new one.</p>
     *
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if the password was changed successfully, false otherwise
     */
    boolean changePassword(String oldPassword, String newPassword);

    /**
     * Defines the permission levels available in the system.
     * Each level grants different access rights to system functionality.
     */
    public enum PermissionLevel {
        /** Read-only access for applicants */
        APPLICANT,   // Read permission
        /** Access HDB officers */
        OFFICER,  // Write permission
        /** Access for HDB managers */
        MANAGER,  // Administrator privileges
        /** No special permissions (default value) */
        NONE    // No special permissions (default value)

    }
}


