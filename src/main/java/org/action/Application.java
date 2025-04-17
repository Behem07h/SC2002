// src/org/action/Application.java
package org.action;

import java.time.LocalDateTime;

/**
 * A single application with full submit/approve/reject/withdrawal workflow.
 */
public class Application implements Act {
    private final String applicationId;
    private final String applicantId;
    private final String projectId;
    private ApplicationStatus status;
    private String flatType;
    private LocalDateTime openingDate;
    private LocalDateTime closingDate;
    private boolean withdrawn = false;
    private final WithdrawalRequest withdrawalRequest;

    public enum ApplicationStatus {
        PENDING,
        BOOKED,
        SUCCESSFUL,
        UNSUCCESSFUL,
        WITHDRAWN
    }

    public Application(
        String applicationId,
        String applicantId,
        String projectId,
        ApplicationStatus status,
        String flatType,
        LocalDateTime openingDate,
        LocalDateTime closingDate
    ) {
        this.applicationId     = applicationId;
        this.applicantId       = applicantId;
        this.projectId         = projectId;
        this.status            = status;
        this.flatType          = flatType;
        this.openingDate       = openingDate;
        this.closingDate       = closingDate;
        this.withdrawalRequest = new WithdrawalRequest(applicationId);
    }

    @Override
    public void view() {
        if (withdrawn) {
            System.out.println("This application has been withdrawn; details hidden.");
            return;
        }
        System.out.println("Application ID: " + applicationId);
        System.out.println("Applicant ID:   " + applicantId);
        System.out.println("Project ID:     " + projectId);
        System.out.println("Status:         " + status);
        System.out.println("Flat Type:      " + flatType);
        System.out.println("Opening Date:   " + (openingDate != null ? openingDate : "Not set"));
        System.out.println("Closing Date:   " + (closingDate  != null ? closingDate  : "Not set"));
    }

    @Override
    public void submit() {
        if (status == ApplicationStatus.PENDING) {
            status      = ApplicationStatus.BOOKED;
            openingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " booked on " + openingDate);
        } else {
            System.out.println("Cannot submit: status is " + status);
        }
    }

    public void approveApplication() {
        if (status == ApplicationStatus.BOOKED) {
            status      = ApplicationStatus.SUCCESSFUL;
            closingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " approved on " + closingDate);
        } else {
            System.out.println("Cannot approve: status is " + status);
        }
    }

    public void rejectApplication() {
        if (status == ApplicationStatus.BOOKED) {
            status      = ApplicationStatus.UNSUCCESSFUL;
            closingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " rejected on " + closingDate);
        } else {
            System.out.println("Cannot reject: status is " + status);
        }
    }

    /** Applicant requests withdrawal (only if BOOKED). */
    public void withdrawApplication() {
        if (!withdrawn && status == ApplicationStatus.BOOKED) {
            withdrawalRequest.request();
        } else {
            System.out.println("Cannot request withdrawal: status=" + status + ", withdrawn=" + withdrawn);
        }
    }

    /** Officer finalizes an approved withdrawal. */
    public void approveWithdrawal() {
        if (withdrawalRequest.getApprovalStatus()
            == WithdrawalRequest.OfficerApprovalStatus.APPROVED) {

            withdrawn   = true;
            status      = ApplicationStatus.WITHDRAWN;
            closingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId +
                               " withdrawn successfully on " + closingDate);
        } else {
            System.out.println("Cannot finalize withdrawal: approval status is " +
                               withdrawalRequest.getApprovalStatus());
        }
    }

    /** Officer processes a rejected withdrawal. */
    public void rejectWithdrawal() {
        if (withdrawalRequest.getApprovalStatus()
            == WithdrawalRequest.OfficerApprovalStatus.REJECTED) {
            System.out.println("Withdrawal request for " + applicationId + " was rejected.");
        } else {
            System.out.println("No rejected withdrawal to process for " + applicationId);
        }
    }

    public String getProjectId() {
        return projectId;
    }

    /** Returns the applicant’s ID. */
    public String getApplicantId() {
    return applicantId;
    }

/** Updates the application’s status. */
    public void setApplicationStatus(ApplicationStatus newStatus) {
    this.status = newStatus;
    }

/** Updates the closing date. */
    public void setClosingDate(LocalDateTime now) {
    this.closingDate = now;
    }

    
}









    