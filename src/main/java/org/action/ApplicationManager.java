package org.action;

import org.UI.ConfigLDR;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationManager {
    private List<Application> applicationList;
    private final String path = "data/db";
    private final String filename =  "/applications.csv";

    public ApplicationManager() {
        //load applications from csv
        this.applicationList = new ArrayList<Application>();

        ConfigLDR ldr = new ConfigLDR();
        Map<String,String[]> appl_map = ldr.ReadToArrMap(path + filename);
        for (String key : appl_map.keySet()) {
            String[] items = appl_map.get(key);
            if (items.length < 5) {
                System.out.println("Application ID " + key + " missing params");
                continue;
            } //if param length too short, skip

            String projectID = items[0];
            String applicantId = items[1];
            Application.ApplicationStatus status = Application.ApplicationStatus.valueOf(items[2]);
            LocalDate openingDate = LocalDate.parse(items[3]);
            LocalDate closingDate = LocalDate.parse(items[4]);
            this.applicationList.add(new Application(key,applicantId,projectID,status, openingDate,closingDate));
        }
    }

    public void store() {
        // run this when quitting program to store to csv
        Map<String,String[]> appl_map = new HashMap<>();
        for (Application a : applicationList) {
            String[] items = {a.getProjectId(), a.getApplicantId(), String.valueOf(a.getApplicantId()), String.valueOf(a.getSubmissionDate()), String.valueOf(a.getClosingDate())};
            appl_map.put(String.valueOf(a.getApplicantId()),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(path + filename,appl_map);
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
            if (app.getApplicantId().equalsIgnoreCase(applicationId)) {
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
                app.setClosingDate(java.time.LocalDate.now());
            }
            System.out.println("Status updated to: " + newStatus);
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






