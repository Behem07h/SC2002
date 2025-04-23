/**
 * Represents an HDB Manager in the system.
 * This class implements the user interface and includes manager-specific functionality.
 *
 * <p>HDBManager contains personal information about the manager, their authentication
 * credentials, permission level, and information about their currently assigned project.</p>
 *
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.Users.HDBManager;

import org.Users.user;  // Adjust the import as necessary if your user interface is in a different package

import java.util.Scanner;

public class HDBManager implements user {
    /** The unique ID for the manager */
    private String userID;

    /** The username for login purposes */
    private String username;

    /** The plain-text password for authentication */
    private String password;  // Plain-text password

    /** The permission level assigned to this manager */
    private final PermissionLevel perms;

    /** The marital status of the manager */
    private String maritalStatus;

    /** The age of the manager */
    private int age;

    /**
     * ID of the project currently being handled by this manager
     * Note: Active projects cannot overlap with previous projects
     */
    private String activeProjectId;

    /**
     * Creates a new HDBManager with complete profile information.
     *
     * @param userID The unique ID for this manager
     * @param username The login username for this manager
     * @param password The login password for this manager (plain text)
     * @param maritalStatus The marital status of the manager
     * @param age The age of the manager
     * @param perms The permission level assigned to this manager
     * @param activeProjectId The ID of the project currently assigned to this manager
     */
    public HDBManager(String userID, String username, String password, String maritalStatus, int age, PermissionLevel perms, String activeProjectId) {
        this.userID = userID;
        this.username = username;
        this.password = password;  // Plain-text password
        this.maritalStatus = maritalStatus;
        this.age = age;
        this.perms = perms;
        this.activeProjectId = activeProjectId;
    }

    /**
     * Gets the ID of the project currently being handled by this manager.
     *
     * @return The active project ID
     */
    public String getActiveProjectId() {
        return activeProjectId;
    }

    /**
     * Sets the ID of the project currently being handled by this manager.
     *
     * @param activeProjectId The active project ID to set
     */
    public void setActiveProjectId(String activeProjectId) {
        this.activeProjectId = activeProjectId;
    }

    /**
     * Gets the marital status of the manager.
     *
     * @return The marital status
     */    @Override
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the marital status of the manager.
     *
     * @param maritalStatus The marital status to set
     */
    @Override
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * Gets the age of the manager.
     *
     * @return The age
     */
    @Override
    public int getAge() {
        return age;
    }

    /**
     * Sets the age of the manager.
     *
     * @param age The age to set
     */
    @Override
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Gets the unique ID for the manager.
     *
     * @return The user ID
     */    @Override
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the unique ID for the manager.
     *
     * @param userID The user ID to set
     */
    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Gets the username for login purposes.
     *
     * @return The username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for login purposes.
     *
     * @param username The username to set
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the password for authentication.
     *
     * @return The password
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Sets the plain-text password for authentication.
     *
     * @param password The password to set
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the permission level assigned to this manager.
     *
     * @return The permission level
     */
    @Override
    public PermissionLevel getPerms() {
        return perms;
    }

    /**
     * Changes the manager's password if the old password matches the current one.
     *
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if the password was successfully changed, false otherwise
     */
    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {  // Plain-text comparison
            this.password = newPassword;
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the HDBManager object.
     *
     * @return A string containing the manager's details
     */
    @Override
    public String toString() {
        return "HDBManager{" +
               "username='" + username + '\'' +
               ", userID='" + userID + '\'' +
               ", maritalStatus='" + maritalStatus + '\'' +
               ", age=" + age +
               ", permissionLevel=" + perms +
                ", activeProjectId='" + activeProjectId + '\'' +
               '}';
    }
}
