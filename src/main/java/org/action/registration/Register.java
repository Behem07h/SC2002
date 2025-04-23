package org.action.registration;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;


public class Register {
    private String registrationID;
    private String userID;
    private String username;
    private String projectID;
    private RegistrationStatus status;
    private final LocalDate submissionDate;
    private LocalDate closingDate;

    public enum RegistrationStatus {
        NIL,
        PENDING,
        APPROVED,
        REJECTED
    }

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

    public String view() {
        return String.format("RegID: %s | Project %s\nSubmitted %s\n",registrationID, projectID, submissionDate);
    }

    public String view_full() {
        return String.format("RegID: %s | Officer %s\nSubmitted %s\n",registrationID, username, submissionDate);
    }
    public void approveRegistration() {
        if (status == RegistrationStatus.PENDING) {
            status      = RegistrationStatus.APPROVED;
            System.out.println("Registration " + registrationID + " for project " + projectID + "approved");
        } else {
            System.out.println("Cannot accept: status is " + status);
        }
    }

    public void rejectRegistration() {
        if (status == RegistrationStatus.PENDING) {
            status      = RegistrationStatus.REJECTED;
            closingDate = LocalDate.now();
            System.out.println("Registration " + registrationID + " for project " + projectID + " rejected on " + closingDate);
        } else {
            System.out.println("Cannot reject: status is " + status);
        }
    }

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

    public String getRegistrationID() {
        return registrationID;
    }

    public String getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getProjectID() {
        return projectID;
    }

    public RegistrationStatus getStatus() {
        return status;
    }

    public LocalDate getSubmissionDate() {
        return submissionDate;
    }

    public String getClosingDate() {
        if (closingDate == null) {
            return "NONE";
        } else {
            return String.valueOf(closingDate);
        }
    }

    public void setClosingDate(LocalDate now) {
        this.closingDate = now;
    }

    public void setRegistrationStatus(RegistrationStatus newStatus) {
        this.status = newStatus;
    }

}
