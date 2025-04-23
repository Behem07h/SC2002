/**
 * Package for HDB Officer user management functionality.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 */
package org.Users.HDBOfficer;

import org.Users.user;

/**
 * Represents an HDB Officer in the system with user authentication and personal information.
 * This class implements the user interface and includes functionality
 * to manage officer details such as username, password, marital status, age, and permission level.
 * which is a significant security vulnerability. Consider using a secure password hashing
 * mechanism in production environments.
 */
 public class HDBOfficer implements user {
    /** Unique identifier for the HDB Officer */
    private String userID;

    /** Username for login authentication */
    private String username;

    /** Password stored in plain text (security concern) */
    private String password; // Plain-text password

    /** Officer's marital status information */
    private String maritalStatus;

    /** Officer's age in years */
    private int age;

    /** Permission level assigned to this officer, immutable after creation */
    private final PermissionLevel perms;  // Default to PermissionLevel.NONE

    /**
     * Constructs a new HDBOfficer with the specified details.
     *
     * @param userID        The unique identifier for the officer
     * @param username      The login username for the officer
     * @param password      The password for authentication (stored as plain text)
     * @param maritalStatus The officer's marital status
     * @param age           The officer's age in years
     * @param perms         The permission level assigned to this officer
     */    public HDBOfficer(String userID, String username, String password, String maritalStatus, int age, PermissionLevel perms) {
        this.userID = userID;
        this.username = username;
        this.password = password;  // Plain-text password
        this.maritalStatus = maritalStatus;
        this.age = age;
        this.perms = perms;
    }

    /**
     * Gets the user ID of this HDB Officer.
     *
     * @return The user ID as a String
     */    @Override
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the user ID for this HDB Officer.
     *
     * @param userID The new user ID to set
     */
    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Gets the username of this HDB Officer.
     *
     * @return The username as a String
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for this HDB Officer.
     *
     * @param username The new username to set
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the plain-text password of this HDB Officer.
     *
     * <p>Note: Returning plain-text passwords is a security concern.</p>
     *
     * @return The password as a String
     */
    @Override
    public String getPassword() {
        return password;  // Plain-text password
    }

    /**
     * Sets the password for this HDB Officer.
     *
     * <p>Note: Password is stored as plain text without encryption or hashing.</p>
     *
     * @param password The new password to set
     */
    @Override
    public void setPassword(String password) {
        this.password = password;  // Set plain-text password
    }

    /**
     * Gets the marital status of this HDB Officer.
     *
     * <p>Note: For HDBOfficer, this may represent department information instead.</p>
     *
     * @return The marital status as a String
     */
    @Override
    public String getMaritalStatus() {
        return maritalStatus; // Department instead of marital status for HDBOfficer
    }

    /**
     * Sets the marital status for this HDB Officer.
     *
     * @param maritalStatus The new marital status to set
     */
    @Override
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * Gets the age of this HDB Officer.
     *
     * @return The age as an integer
     */
    @Override
    public int getAge() {
        return age;
    }

    /**
     * Sets the age for this HDB Officer.
     *
     * @param age The new age to set
     */
    @Override
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the permission level of this HDB Officer.
     *
     * @return The permission level enum value
     */
    @Override
    public PermissionLevel getPerms() {
        return this.perms;
    }

    /**
     * Changes the password for this HDB Officer.
     *
     * <p>This method verifies the old password before setting the new one.</p>
     * <p>Note: Plain-text password comparison is used, which is not secure.</p>
     *
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if the password was changed successfully, false otherwise
     */
    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {  // Plain-text password comparison
            this.password = newPassword;  // Set new password
            return true;
        }
        return false;
    }
}
