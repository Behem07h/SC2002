package org.action;

import org.action.act;
import java.time.LocalDateTime;

public class Application implements act {
    private String applicationId;
    private String applicantName;
    private ApplicationStatus status;
    private String flattype;
    private LocalDateTime openingDate;
    private LocalDateTime closingDate;
    private boolean withdrawn = false;

    
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

   
    public Application(String applicationId, String applicantName, ApplicationStatus initialStatus, String flattype) {
        this.applicationId = applicationId;
        this.applicantName = applicantName;
        this.status = initialStatus;
        this.flattype = flattype;
        this.openingDate = null;
        this.closingDate = null;
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
        System.out.println("Flat Type: " + flattype);
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




    