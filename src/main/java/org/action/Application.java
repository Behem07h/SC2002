package org.action;

import org.action.act;
import java.time.LocalDateTime;

public class Application implements act {
    private String applicationId;
    private String applicantName;
    private String projectId;
    private ApplicationStatus status;
    private String flatType;
    private LocalDateTime openingDate;
    private LocalDateTime closingDate;
    private boolean withdrawn = false;
    private WithdrawalRequest withdrawalRequest;

    public enum ApplicationStatus {
        PENDING,
        BOOKED,
        SUCCESSFUL,
        UNSUCCESSFUL,
        WITHDRAWN
    }

    public Application(String applicationId, String applicantName, String projectId,
                       ApplicationStatus status, String flatType,
                       LocalDateTime openingDate, LocalDateTime closingDate) {
        this.applicationId = applicationId;
        this.applicantName = applicantName;
        this.projectId = projectId;
        this.status = status;
        this.flatType = flatType;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.withdrawalRequest = new WithdrawalRequest(applicationId);
    }

    @Override
    public void view() {
        if (withdrawn) {
            System.out.println("This application has been withdrawn and its details are no longer visible.");
            return;
        }
        System.out.println("Application ID: " + applicationId);
        System.out.println("Applicant Name: " + applicantName);
        System.out.println("Status: " + status);
        System.out.println("Flat Type: " + flatType);
        System.out.println("Opening Date: " + (openingDate != null ? openingDate : "Not set"));
        System.out.println("Closing Date: " + (closingDate != null ? closingDate : "Not set"));
    }

    @Override
    public void submit() {
        if (status == ApplicationStatus.BOOKED) {
            System.out.println("The application has already been submitted (booked).");
        } else if (status == ApplicationStatus.PENDING) {
            status = ApplicationStatus.BOOKED;
            openingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " submitted (booked) successfully on " + openingDate);
        } else {
            System.out.println("Application " + applicationId + " cannot be submitted. Current status: " + status);
        }
    }

    public void approveApplication() {
        if (status == ApplicationStatus.BOOKED) {
            status = ApplicationStatus.SUCCESSFUL;
            closingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " approved and marked as 'Successful' on " + closingDate + ".");
        } else {
            System.out.println("Application " + applicationId + " cannot be approved. Current status: " + status);
        }
    }

    public void rejectApplication() {
        if (status == ApplicationStatus.BOOKED) {
            status = ApplicationStatus.UNSUCCESSFUL;
            closingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " has been rejected on " + closingDate + ".");
        } else {
            System.out.println("Application " + applicationId + " cannot be rejected. Current status: " + status);
        }
    }

    public void withdrawApplication() {
        if (!withdrawn && status == ApplicationStatus.BOOKED) {
            withdrawalRequest.request();
        } else if (withdrawn) {
            System.out.println("Application " + applicationId + " is already withdrawn.");
        } else {
            System.out.println("Cannot request withdrawal in current status: " + status);
        }
    }

    public void approveWithdrawal() {
        if (withdrawalRequest.getApprovalStatus() == WithdrawalRequest.OfficerApprovalStatus.PENDING) {
            withdrawalRequest.approve();
            withdrawn = true;
            status = ApplicationStatus.WITHDRAWN;
            closingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " has been withdrawn successfully on " + closingDate + ".");
        } else {
            System.out.println("Withdrawal request for application " + applicationId + " is not pending or already processed.");
        }
    }

    public void rejectWithdrawal() {
        if (withdrawalRequest.getApprovalStatus() == WithdrawalRequest.OfficerApprovalStatus.PENDING) {
            withdrawalRequest.reject();
            System.out.println("Withdrawal request for application " + applicationId + " has been rejected.");
        } else {
            System.out.println("No pending withdrawal request for application " + applicationId + ".");
        }
    }

    public void setClosingDate(LocalDateTime now) {
        this.closingDate = now;
    }

    public void setApplicationStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
    }

    public String getApplicationId() {
        return applicationId;
    }
    // other getters/setters omitted
}








    