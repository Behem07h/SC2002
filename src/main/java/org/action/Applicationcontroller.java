package org.action;

import java.util.ArrayList;
import java.util.List;

public class Applicationcontroller {
    private List<Application> applicationList;

    public Applicationcontroller() {
        applicationList = new ArrayList<>();
    }

    public void addApplication(Application app) {
        applicationList.add(app);
        System.out.println("Application added: " + app);
    }

    public void viewAllApplications() {
        System.out.println("=== All Applications ===");
        for (Application app : applicationList) {
            app.view();
            System.out.println("--------------------");
        }
    }

    public Application retrieveApplication(String applicationId) {
        for (Application app : applicationList) {
            if (app.getApplicationId().equalsIgnoreCase(applicationId)) {
                return app;
            }
        }
        System.out.println("No application found with ID: " + applicationId);
        return null;
    }

    public void processApplication(String applicationId, Application.ApplicationStatus targetStatus) {
        Application app = retrieveApplication(applicationId);
        if (app == null) {
            System.out.println("Application ID \"" + applicationId + "\" not found.");
            return;
        }

        switch (targetStatus) {
            case BOOKED:
                System.out.println("Processing submission for application ID: " + applicationId);
                app.submit();
                break;

            case SUCCESSFUL:
                System.out.println("Processing approval for application ID: " + applicationId);
                app.approveApplication();
                break;

            case UNSUCCESSFUL:
                System.out.println("Processing rejection for application ID: " + applicationId);
                app.rejectApplication();
                break;

            default:
                System.out.println("Unsupported target status: " + targetStatus);
        }
    }

    public void updateApplicationStatus(String applicationId, Application.ApplicationStatus newStatus) {
        Application app = retrieveApplication(applicationId);
        if (app != null) {
            app.setApplicationStatus(newStatus);
            if (newStatus == Application.ApplicationStatus.SUCCESSFUL || newStatus == Application.ApplicationStatus.UNSUCCESSFUL) {
                app.setClosingDate(java.time.LocalDateTime.now());
            }
            System.out.println("Status updated to: " + newStatus);
        }
    }

    public void withdrawApplication(String applicationId) {
        Application app = retrieveApplication(applicationId);
        if (app != null) {
            app.withdrawApplication();
        }
    }

    public void submitApplication(String applicationId) {
        Application app = retrieveApplication(applicationId);
        if (app != null) {
            app.submit();
        }
    }

    public void approveApplication(String applicationId) {
        Application app = retrieveApplication(applicationId);
        if (app != null) {
            app.approveApplication();
        }
    }

    public void rejectApplication(String applicationId) {
        Application app = retrieveApplication(applicationId);
        if (app != null) {
            app.rejectApplication();
        }
    }

    public List<Application> getApplicationList() {
        return applicationList;
    }
} 






