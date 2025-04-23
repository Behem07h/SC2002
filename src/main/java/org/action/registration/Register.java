/**
 * Represents a registration entry in the project management system.
 * This class handles registration status, submission details, and processing operations.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.action.registration;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * A registration connects officer to a project with a specific status
 * (NIL, PENDING, APPROVED, REJECTED) and tracks important dates.
 */
public class Register {

    /** Unique ID for the registration */
    private String registrationID;

    /** ID of the user submitting the registration */
    private String userID;

    /** Username of the user submitting the registration */
    private String username;

    /** Identifier of the project being registered for */
    private String projectID;

    /** Current status of the registration */
    private RegistrationStatus status;

    /** Date when the registration was submitted */
    private final LocalDate submissionDate;

    /** Date when the registration was closed */
    private LocalDate closingDate;

    /**
     * Enumeration of possible registration statuses.
     */
    public enum RegistrationStatus {
        NIL,
        PENDING,
        APPROVED,
        REJECTED
    }
    /**
     * Constructs a new Registration with the specified details.
     *
     * @param registrationID Unique ID for this registration
     * @param userID ID of the user submitting the registration
     * @param username Username of the user submitting the registration
     * @param projectID Project ID being registered for
     * @param status Current status of the registration
     * @param openingDate Date when the registration was opened
     * @param closingDate Date when the registration was closed
     */
    public Register(
        String registrationID, 
        String userID,
        String username,
        String projectID,
        RegistrationStatus status,
        String openingDate,
        String closingDate
    ) {
        this.registrationID     = registrationID;
        this.userID             = userID;
        this.username           = username;
        this.projectID          = projectID;
        this.status             = status;
        this.submissionDate     = LocalDate.parse(openingDate);
        if (!closingDate.isEmpty()) {
            this.closingDate    = LocalDate.parse(closingDate);
        } else {
            this.closingDate    = null;
        }
    }

    /**
     * Provides a string representation of the registration.
     *
     * @return String containing registration ID, project ID, and submission date
     */
    public String view() {
        return String.format("RegID: %s | Project %s\nSubmitted %s\n",registrationID, projectID, submissionDate);
    }

    /**
     * Provides a string representation of the registration.
     *
     * @return String containing registration ID, username, and submission date
     */
    public String view_full() {
        return String.format("RegID: %s | Officer %s\nSubmitted %s\n",registrationID, username, submissionDate);
    }

    /**
     * Approves pending registration.
     * Changes status to APPROVED if current status is PENDING.
     */
    public void approveRegistration() {
        if (status == RegistrationStatus.PENDING) {
            status      = RegistrationStatus.APPROVED;
            System.out.println("Registration " + registrationID + " for project " + projectID + "approved");
        } else {
            System.out.println("Cannot accept: status is " + status);
        }
    }

    /**
     * Rejects a pending registration.
     * Changes status to REJECTED and sets closing date to current date if status is PENDING.
     */
    public void rejectRegistration() {
        if (status == RegistrationStatus.PENDING) {
            status      = RegistrationStatus.REJECTED;
            closingDate = LocalDate.now();
            System.out.println("Registration " + registrationID + " for project " + projectID + " rejected on " + closingDate);
        } else {
            System.out.println("Cannot reject: status is " + status);
        }
    }

    /**
     * Filters registrations based on specified criteria.
     *
     * @param userID User ID to filter by (empty string to ignore)
     * @param username Username to filter by (empty string to ignore)
     * @param projectID Project ID to filter by (empty string to ignore)
     * @param registrationID Registration ID to filter by (empty string to ignore)
     * @param statusWhitelist List of statuses to include in the filter
     * @return true if the registration matches all specified criteria, false otherwise
     */
    public boolean filter(String userID, String username, String projectID, String registrationID, List<RegistrationStatus> statusWhitelist) {
        boolean out = true;
        if (!userID.isEmpty()) {
            out = Objects.equals(this.userID, userID);
        }
        if (!username.isEmpty()) {
            out = Objects.equals(this.username, username);
        }
        if (!projectID.isEmpty()) {
            out = Objects.equals(this.projectID, projectID);
        }
        if (!registrationID.isEmpty()) {
            out = out && Objects.equals(this.registrationID, registrationID);
        }
        for (RegistrationStatus status : statusWhitelist) {
            out = out && (this.status == status);
        }
        return out;
    }

    /**
     * Gets the registration ID.
     *
     * @return The registration ID
     */
    public String getRegistrationID() {
        return registrationID;
    }

    /**
     * Gets the user ID.
     *
     * @return The user ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * Gets the username.
     *
     * @return The username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the project ID.
     *
     * @return The project ID
     */
    public String getProjectID() {
        return projectID;
    }

    /**
     * Gets the registration status.
     *
     * @return The current status
     */
    public RegistrationStatus getStatus() {
        return status;
    }

    /**
     * Gets the submission date.
     *
     * @return The submission date
     */
    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Gets the closing date as a string.
     *
     * @return The closing date as a string, or "NONE" if not set
     */
    public String getClosingDate() {
        if (closingDate == null) {
            return "NONE";
        } else {
            return String.valueOf(closingDate);
        }
    }

    /**
     * Sets the closing date.
     *
     * @param now The new closing date
     */
    public void setClosingDate(LocalDate now) {
        this.closingDate = now;
    }

    /**
     * Sets the registration status.
     *
     * @param newStatus The new status to set
     */
    public void setRegistrationStatus(RegistrationStatus newStatus) {
        this.status = newStatus;
    }

}
