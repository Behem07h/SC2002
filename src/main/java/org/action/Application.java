package org.action;

import java.time.LocalDateTime;

public class Application implements act {
    private final String applicationId;
    private final String applicantId;
    private final String projectId;
    private ApplicationStatus status;
    private LocalDateTime submissionDate;
    private LocalDateTime closingDate;
    private boolean withdrawn = false;

    public String getProjectId() {
        return projectId;
    }
    public String getApplicantId() {
        return applicantId;
    }
    public ApplicationStatus getApplicationStatus() {
        return status;
    }
    public LocalDateTime getSubmissionDate() {
        return submissionDate;
    }

    public LocalDateTime getClosingDate() {
        return closingDate;
    }

    public void setApplicationStatus(ApplicationStatus newStatus) {
        this.status = newStatus;
    }

    public void setClosingDate(LocalDateTime now) {
        this.closingDate = now;
    }

    
    public enum ApplicationStatus {
        PENDING,       
        BOOKED,       
        SUCCESSFUL,   
        UNSUCCESSFUL  
    }

   
    public Application(String applicationId, String applicantId, String projectId, ApplicationStatus status, LocalDateTime openingDate, LocalDateTime closingDate) {
        this.applicationId = applicationId;
        this.applicantId = applicantId;
        this.projectId = projectId;
        this.status = status;
        this.submissionDate = openingDate;
        this.closingDate = closingDate;
    }

    @Override
    public void view() {
        if (withdrawn) {
            System.out.println("This application has been withdrawn and its details are no longer visible.");
            return;
        }
        System.out.println("Application ID: " + applicationId);
        System.out.println("Applicant Name: " + applicantId);
        System.out.println("Status: " + status);
        System.out.println("Submission Date: " + (submissionDate != null ? submissionDate : "Not set"));
        System.out.println("Closing Date: " + (closingDate != null ? closingDate : "Not set"));
    }

    @Override
    public void submit() {
        if (status == ApplicationStatus.BOOKED) {
            System.out.println("The application has already been submitted (booked).");
        } else if (status == ApplicationStatus.PENDING) {
            status = ApplicationStatus.BOOKED;
            submissionDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " submitted (booked) successfully on " + submissionDate);
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
        if (status == ApplicationStatus.BOOKED) {
            // Allow withdrawal only if the flat was booked.
            withdrawn = true;
            closingDate = LocalDateTime.now();
            System.out.println("Application " + applicationId + " withdrawn successfully on " + closingDate + ".");
        } else {
            System.out.println("Application " + applicationId + " cannot be withdrawn because it is not booked.");
        }
    }

    public String getApplicationId() {
        return this.applicationId;
    }
    
}




    