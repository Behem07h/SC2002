// src/org/action/Application.java
package org.action;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.action.Application.WithdrawalStatus.*;

/**
 * A single application with full submit/approve/reject/withdrawal workflow.
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

    public enum ApplicationStatus {
        PENDING,
        BOOKED,
        SUCCESSFUL,
        UNSUCCESSFUL,
        WITHDRAWN
    }

    public enum WithdrawalStatus {
        NIL,
        PENDING,
        WITHDRAWN,
        REJECTED
    }

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

    @Override
    public String view() {
        if (status == ApplicationStatus.WITHDRAWN) {
            return "Application is withdrawn, cannot view";
        }
        return String.format("Application %s | Project %s\nApplicant: %s\nStatus: %s%s\nSubmitted on: %s%s", applicationId, projectId, applicantId, status, ((withdrawStatus != NIL) ? String.format(" | Withdrawal: %s", withdrawStatus) : ""),submissionDate, (closingDate  != null ? String.format("| Closed on: %s",closingDate)  : ""));
    }

    public void acceptApplication() {
        if (status == ApplicationStatus.PENDING) {
            status      = ApplicationStatus.SUCCESSFUL;
            System.out.println("Application " + applicationId + " invited for flat booking");
        } else {
            System.out.println("Cannot accept: status is " + status);
        }
    }

    public void book_flat() {
        if (status == ApplicationStatus.SUCCESSFUL) {
            status      = ApplicationStatus.BOOKED;
            closingDate = LocalDate.now();
            System.out.println("Application " + applicationId + " approved on " + closingDate);
        } else {
            System.out.println("Cannot approve: status is " + status);
        }
    }

    public void rejectApplication() {
        if (status == ApplicationStatus.PENDING) {
            status      = ApplicationStatus.UNSUCCESSFUL;
            closingDate = LocalDate.now();
            System.out.println("Application " + applicationId + " rejected on " + closingDate);
        } else {
            System.out.println("Cannot reject: status is " + status);
        }
    }

    /** Applicant requests withdrawal (only if BOOKED). */
    public void requestWithdrawal() {
        if (status != ApplicationStatus.WITHDRAWN) {
            withdrawStatus = PENDING;
        } else {
            System.out.println("Cannot request withdrawal: status=" + status);
        }
    }

    /** Officer finalizes an approved withdrawal. */
    public void approveWithdrawal() {
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

    /** Officer processes a rejected withdrawal. */
    public void rejectWithdrawal() {
        if (withdrawStatus == PENDING) {
            withdrawStatus = REJECTED;
            System.out.println("Withdrawal request for " + applicationId + " was rejected.");
        } else {
            System.out.println("No rejected withdrawal to process for " + applicationId);
        }
    }

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

    public String getProjectId() {
        return projectId;
    }

    /** Returns the applicant’s ID. */
    public String getApplicantId() {
    return applicantId;
    }

    public String getClosingDate() {
        if (closingDate == null) {
            return "NONE";
        } else {
            return String.valueOf(closingDate);
        }
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public String getApplicationId() {return applicationId;}

    public String getFlatType() {
        return flatType;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public WithdrawalStatus getWithdrawStatus() {
        return withdrawStatus;
    }

    /** Updates the application’s status. */
    public void setApplicationStatus(ApplicationStatus newStatus) {
    this.status = newStatus;
    }

/** Updates the closing date. */
    public void setClosingDate(LocalDate now) {
    this.closingDate = now;
    }

    
}









    