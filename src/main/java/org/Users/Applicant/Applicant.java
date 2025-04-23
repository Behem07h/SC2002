/**
 * The Applicant class represents a user with the role of an applicant in the system.
 * It implements the user interface and provides functionality specific to applicants.
 *
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */

package org.Users.Applicant;

import org.Users.user;

public class Applicant implements user {
    private String userID;
    private String username;
    private String password;  // Plain-text password
    private String maritalStatus;
    private int age;
    private final PermissionLevel perms;  // Default permission level

    /**
     * Constructs a new Applicant with the specified attributes.
     *
     * @param userID        The unique ID for the applicant
     * @param username      The username for the applicant
     * @param password      The password for the applicant (stored as plain-text)
     * @param maritalStatus The marital status of the applicant
     * @param age           The age of the applicant
     * @param perms         The permission level of the applicant
     */
    public Applicant(String userID, String username, String password, String maritalStatus, int age, PermissionLevel perms) {
        this.userID = userID;
        this.username = username;
        this.password = password;  // Plain-text password
        this.maritalStatus = maritalStatus;
        this.age = age;
        this.perms = perms;
    }

    /**
     * Returns the unique ID of the applicant.
     *
     * @return The applicant's user ID
     */
    @Override
    public String getUserID() {
        return userID;
    }

    /**
     * Sets the unique ID for the applicant.
     *
     * @param userID The new user ID to set
     */
    @Override
    public void setUserID(String userID) {
        this.userID = userID;
    }

    /**
     * Returns the username of the applicant.
     *
     * @return The applicant's username
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username for the applicant.
     *
     * @param username The new username to set
     */
    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the applicant.
     *
     * @return The applicant's password (plain-text)
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password for the applicant.
     *
     * @param password The new password to set
     */
    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns the marital status of the applicant.
     *
     * @return The applicant's marital status
     */
    @Override
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * Sets the marital status for the applicant.
     *
     * @param maritalStatus The new marital status to set
     */
    @Override
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * Returns the age of the applicant.
     *
     * @return The applicant's age
     */
    @Override
    public int getAge() {
        return age;
    }

    /**
     * Sets the age for the applicant.
     *
     * @param age The new age to set
     */
    @Override
    public void setAge(int age) {
        this.age = age;
    }

    /**
     * Returns the permission level of the applicant.
     *
     * @return The applicant's permission level
     */
    @Override
    public PermissionLevel getPerms() {
        return this.perms;
    }

    /**
     * Changes the password for the applicant if the old password is correct.
     *
     * @param oldPassword The current password for verification
     * @param newPassword The new password to set
     * @return true if password was changed successfully, false otherwise
     */
    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        if (this.password.equals(oldPassword)) {  // Plain-text password comparison
            this.password = newPassword;  // Set new password
            return true;
        }
        return false;
    }

    /**
     * Returns a string representation of the Applicant object.
     *
     * @return A string containing the username and userID of the applicant
     */
    @Override
    public String toString() {
        return "Applicant{username='" + username + "', userID='" + userID + "'}";
    }


}








