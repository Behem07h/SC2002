package org.action;

import org.action.Application;
import org.action.Application.ApplicationStatus;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Applicationcontroller {
    private Map<String, Application> applications = new HashMap<>();
    
    /**
     * Adds a new application.
     *
     * @param applicationId the unique ID for the application.
     * @param applicantName the applicant's name.
     * @param flatType the type of flat applied for.
     */
    public void addApplication(String applicationId, String applicantName, String flatType) {
        Application app = new Application(applicationId, applicantName, ApplicationStatus.PENDING, flatType);
        applications.put(applicationId, app);
        System.out.println("Application " + applicationId + " added successfully.");
    }
    
    /**
     * Displays the details of the application with the given ID.
     *
     * @param applicationId the unique ID of the application to view.
     */
    public void viewApplication(String applicationId) {
        Application app = applications.get(applicationId);
        if (app != null) {
            app.view();
        } else {
            System.out.println("Application " + applicationId + " not found.");
        }
    }
    
    /**
     * Submits the application with the given ID.
     *
     * @param applicationId the unique ID of the application to submit.
     */
    public void submitApplication(String applicationId) {
        Application app = applications.get(applicationId);
        if (app != null) {
            app.submit();
        } else {
            System.out.println("Application " + applicationId + " not found.");
        }
    }
    
    /**
     * Approves the application with the given ID.
     *
     * @param applicationId the unique ID of the application to approve.
     */
    public void approveApplication(String applicationId) {
        Application app = applications.get(applicationId);
        if (app != null) {
            app.approveApplication();
        } else {
            System.out.println("Application " + applicationId + " not found.");
        }
    }
    
    /**
     * Rejects the application with the given ID.
     *
     * @param applicationId the unique ID of the application to reject.
     */
    public void rejectApplication(String applicationId) {
        Application app = applications.get(applicationId);
        if (app != null) {
            app.rejectApplication();
        } else {
            System.out.println("Application " + applicationId + " not found.");
        }
    }
    
    /**
     * Withdraws the application with the given ID.
     *
     * @param applicationId the unique ID of the application to withdraw.
     */
    public void withdrawApplication(String applicationId) {
        Application app = applications.get(applicationId);
        if (app != null) {
            app.withdrawApplication();
        } else {
            System.out.println("Application " + applicationId + " not found.");
        }
    }
    
    /**
     * Updates the status of the application with the given ID directly.
     * If the new status is final (e.g., SUCCESSFUL or UNSUCCESSFUL), 
     * the closing date is updated accordingly.
     *
     * @param applicationId the unique ID of the application.
     * @param newStatus     the new status to update the application to.
     */
    public void updateStatus(String applicationId, ApplicationStatus newStatus) {
        Application app = applications.get(applicationId);
        if (app != null) {
            // Update the application's status using its setter.
            app.setApplicationStatus(newStatus);
            // If the status is final, update the closing date.
            if (newStatus == ApplicationStatus.SUCCESSFUL || newStatus == ApplicationStatus.UNSUCCESSFUL) {
                app.setClosingDate(LocalDateTime.now());
            }
            System.out.println("Application " + applicationId + " status updated to " + newStatus);
        } else {
            System.out.println("Application " + applicationId + " not found.");
        }
    }
    
    /**
     * Processes the application by updating its state based on the target status.
     * <ul>
     *   <li>If the target status is BOOKED, the application is submitted (marking the flat as booked).</li>
     *   <li>If the target status is SUCCESSFUL, the application is approved.</li>
     *   <li>If the target status is UNSUCCESSFUL, the application is rejected.</li>
     * </ul>
     *
     * @param applicationId the unique ID of the application to process.
     * @param targetStatus  the target status for processing.
     */
    public void processApplication(String applicationId, ApplicationStatus targetStatus) {
        Application app = applications.get(applicationId);
        if (app == null) {
            System.out.println("Application " + applicationId + " not found.");
            return;
        }
        
        switch (targetStatus) {
            case BOOKED:
                // Process as submission.
                app.submit();
                break;
            case SUCCESSFUL:
                // Process as approval.
                app.approveApplication();
                break;
            case UNSUCCESSFUL:
                // Process as rejection.
                app.rejectApplication();
                break;
            default:
                System.out.println("Processing for target status " + targetStatus + " is not supported.");
        }
    }
}




