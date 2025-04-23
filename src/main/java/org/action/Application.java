// src/org/action/Application.java
package org.action;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.action.Application.WithdrawalStatus.*;

/**
 * Represents a single housing application with a complete workflow for submission,
 * approval, rejection, and withdrawal processes.
 * <p>
 * This class encapsulates all the information and business logic for managing
 * a housing application through its entire lifecycle, including state transitions,
 * withdrawal requests, and status tracking.
 *
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 */
public class Application implements Act {
    private final String applicationId;
    private final String applicantId;
    private final String projectId;
    private ApplicationStatus status;
    private final LocalDate submissionDate;
    private LocalDate closingDate;
    private final String flatType;
    private WithdrawalStatus withdrawStatus;

    /**
     * Represents the possible states of an application status in the system.
     */
    public enum ApplicationStatus {

        /** Application has been submitted but not yet processed */
        PENDING,

        /** Application has been finalized and a flat has been booked */
        BOOKED,

        /** Application is successful */
        SUCCESSFUL,

        /** Application is unsuccessful */
        UNSUCCESSFUL,

        /** Application is withdrawn */
        WITHDRAWN
    }

    /**
     * Represents the possible states of a withdrawal request for an application.
     */
    public enum WithdrawalStatus {

        /** No withdrawal has been requested */
        NIL,

        /** Withdrawal is still pending */
        PENDING,

        /** Withdrawal has been approved */
        WITHDRAWN,

        /** Withdrawal request has been rejected */
        REJECTED
    }

    /**
     * Constructs a new Application with the specified parameters.
     *
     * @param applicationId Unique ID for the application
     * @param applicantId ID of the applicant who submitted the application
     * @param projectId ID of the housing project being applied for
     * @param status Current status of the application
     * @param withdrawStatus Withdrawal status of the application
     * @param openingDate Date when the application was submitted (ISO format)
     * @param closingDate Date when the application was closed (ISO format), or empty string if not closed
     * @param flatType Type of flat being applied for
     */
    public Application(
        String applicationId,
        String applicantId,
        String projectId,
        ApplicationStatus status,
        WithdrawalStatus withdrawStatus,
        String openingDate,
        String closingDate,
        String flatType
    ) {
        this.applicationId     = applicationId;
        this.applicantId       = applicantId;
        this.projectId         = projectId;
        this.status            = status;
        this.withdrawStatus    = withdrawStatus;
        this.submissionDate    = LocalDate.parse(openingDate);
        if (!closingDate.isEmpty()) {
            this.closingDate       = LocalDate.parse(closingDate);
        } else {
            this.closingDate = null;
        }
        this.flatType          = flatType;
    }

    /**
     * Returns a string representation of the application details.
     * If the application has been withdrawn, returns a message indicating that
     * the application cannot be viewed.
     *
     * @return A string containing the application details
     */
    @Override
    public String view() {
        if (status == ApplicationStatus.WITHDRAWN) {
            return "Application is withdrawn, cannot view";
        }
        return String.format("Application %s | Project %s\nApplicant: %s\nStatus: %s%s\nSubmitted on: %s%s", applicationId, projectId, applicantId, status, ((withdrawStatus != NIL) ? String.format(" | Withdrawal: %s", withdrawStatus) : ""),submissionDate, (closingDate  != null ? String.format("| Closed on: %s",closingDate)  : ""));
    }

    /**
     * Changes the application status to SUCCESSFUL if it is currently PENDING.
     * This indicates the applicant is invited to book a flat.
     * Prints a confirmation message or an error if the status transition is not allowed.
     */
    public void acceptApplication() {
        if (status == ApplicationStatus.PENDING) {
            status      = ApplicationStatus.SUCCESSFUL;
            System.out.println("Application " + applicationId + " invited for flat booking");
        } else {
            System.out.println("Cannot accept: status is " + status);
        }
    }

    /**
     * Changes the application status to BOOKED if it is currently SUCCESSFUL.
     * Sets the closing date to the current date.
     * Prints a confirmation message or an error if the status transition is not allowed.
     */
    public void book_flat() {
        if (status == ApplicationStatus.SUCCESSFUL) {
            status      = ApplicationStatus.BOOKED;
            closingDate = LocalDate.now();
            System.out.println("Application " + applicationId + " approved on " + closingDate);
        } else {
            System.out.println("Cannot approve: status is " + status);
        }
    }

    /**
     * Changes the application status to UNSUCCESSFUL if it is currently PENDING.
     * Sets the closing date to the current date.
     * Prints a confirmation message or an error if the status transition is not allowed.
     */
    public void rejectApplication() {
        if (status == ApplicationStatus.PENDING) {
            status      = ApplicationStatus.UNSUCCESSFUL;
            closingDate = LocalDate.now();
            System.out.println("Application " + applicationId + " rejected on " + closingDate);
        } else {
            System.out.println("Cannot reject: status is " + status);
        }
    }

    /**
     * Initiates a withdrawal request for the application.
     * Sets the withdrawal status to PENDING if the application is not already withdrawn.
     * Prints an error message if the application is already withdrawn.
     */    public void requestWithdrawal() {
        if (status != ApplicationStatus.WITHDRAWN) {
            withdrawStatus = PENDING;
        } else {
            System.out.println("Cannot request withdrawal: status=" + status);
        }
    }

    /**
     * Approves a pending withdrawal request.
     * Changes the withdrawal status to WITHDRAWN and the application status to WITHDRAWN.
     * Sets the closing date to the current date.
     * Prints a confirmation message or an error if there is no pending withdrawal.
     */    public void approveWithdrawal() {
        if (withdrawStatus == PENDING) {
            withdrawStatus = WITHDRAWN;
            status      = ApplicationStatus.WITHDRAWN;
            closingDate = LocalDate.now();
            System.out.println("Application " + applicationId +
                               " withdrawn successfully on " + closingDate);
        } else {
            System.out.println("Cannot finalize withdrawal: approval status is " +
                    withdrawStatus);
        }
    }

    /**
     * Rejects a pending withdrawal request.
     * Changes the withdrawal status to REJECTED.
     * Prints a confirmation message or an error if there is no pending withdrawal.
     */    public void rejectWithdrawal() {
        if (withdrawStatus == PENDING) {
            withdrawStatus = REJECTED;
            System.out.println("Withdrawal request for " + applicationId + " was rejected.");
        } else {
            System.out.println("No rejected withdrawal to process for " + applicationId);
        }
    }

    /**
     * Filters applications based on specified criteria.
     *
     * @param userId Filter by applicant ID
     * @param projectId Filter by project ID
     * @param applicationId Filter by application ID
     * @param statusBlacklist List of application statuses to exclude
     * @return true if the application matches all provided criteria
     */

    public boolean filter(String userId, String projectId, String applicationId, List<ApplicationStatus> statusBlacklist) {
        boolean out = true;
        if (!userId.isEmpty()) {
            out = Objects.equals(this.applicantId, userId);
        }
        if (!projectId.isEmpty()) {
            out = Objects.equals(this.projectId, projectId);
        }
        if (!applicationId.isEmpty()) {
            out = out && Objects.equals(this.applicationId, applicationId);
        }
        if (statusBlacklist != null) {
            for (ApplicationStatus status : statusBlacklist) {
                out = out && (this.status != status);
            }
        }
        return out;
    }

    /**
     * Gets the project ID associated with this application.
     *
     * @return The project ID
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * Gets the applicant ID associated with this application.
     *
     * @return The applicant ID
     */
    public String getApplicantId() {
    return applicantId;
    }

    /**
     * Gets the closing date of the application as a string.
     *
     * @return The closing date as a string
     */
    public String getClosingDate() {
        if (closingDate == null) {
            return "NONE";
        } else {
            return String.valueOf(closingDate);
        }
    }

    /**
     * Gets the submission date of the application.
     *
     * @return The submission date
     */
    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    /**
     * Gets the application ID.
     *
     * @return The application ID
     */
    public String getApplicationId() {return applicationId;}

    /**
     * Gets the type of flag being requested in this application.
     *
     * @return The flat type
     */
    public String getFlatType() {
        return flatType;
    }

    /**
     * Gets the current status of the application.
     *
     * @return The application status
     */
    public ApplicationStatus getStatus() {
        return status;
    }

    /**
     * Gets the current withdrawal status of the application.
     *
     * @return The withdrawal status
     */
    public WithdrawalStatus getWithdrawStatus() {
        return withdrawStatus;
    }

    /**
     * Updates the application's status.
     *
     * @param newStatus The new application status
     */    public void setApplicationStatus(ApplicationStatus newStatus) {
    this.status = newStatus;
    }

    /**
     * Updates the closing date of the application.
     *
     * @param now The new closing date
     */    public void setClosingDate(LocalDate now) {
    this.closingDate = now;
    }

    
}









    