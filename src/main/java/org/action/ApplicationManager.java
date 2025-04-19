package org.action;

import org.UI.ConfigLDR;
import org.Users.Applicant.Applicant;
import org.Users.HDBOfficer.HDBOfficer;
import org.Users.user;
import org.action.enquiry.EnquiriesManager;
import org.action.project.Project;
import org.action.project.ProjectManager;

import java.time.LocalDate;
import java.util.*;

public class ApplicationManager {
    private final List<Application> applicationList;
    private final String path = "data/db";
    private final String filename =  "/applications.csv";

    public ApplicationManager() {
        //load applications from csv
        this.applicationList = new ArrayList<>();

        ConfigLDR ldr = new ConfigLDR();
        Map<String,String[]> appl_map = ldr.ReadToArrMap(path + filename);
        for (String key : appl_map.keySet()) {
            String[] items = appl_map.get(key);
            if (items.length < 6) {
                System.out.println("Application ID " + key + " missing params");
                continue;
            } //if param length too short, skip

            String projectID = items[0];
            String applicantId = items[1];
            Application.ApplicationStatus status = Application.ApplicationStatus.valueOf(items[2]);
            LocalDate openingDate = (Objects.equals(items[3], "null") ? null : LocalDate.parse(items[3]));
            System.out.println(items[3]);
            System.out.println(items[4]);
            LocalDate closingDate = (Objects.equals(items[4], "null") ? null : LocalDate.parse(items[4]));
            String flatType = items[5]; //todo: load and save withdrawal status as well
            this.applicationList.add(new Application(key, applicantId, projectID, status, openingDate, closingDate, flatType));
        }
    }

    public void store() {
        // run this when quitting program to store to csv
        Map<String,String[]> appl_map = new HashMap<>();
        for (Application a : applicationList) {
            String[] items = {a.getProjectId(), a.getApplicantId(), String.valueOf(a.getStatus()), String.valueOf(a.getSubmissionDate()), String.valueOf(a.getClosingDate()), a.getFlatType()};
            appl_map.put(String.valueOf(a.getApplicationId()),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(path + filename,appl_map);
    }



    private List<Application> searchFilter(String userId, String projectId, String applicationId, Application.ApplicationStatus statusBlacklist) {
        List<Application> out = new ArrayList<>();
        for (Application a : applicationList) {
            if (a.filter(userId, projectId, applicationId, statusBlacklist)) {
                out.add(a);
            }
        }
        return out;
    }

    private int countByUser(user usr) {
        List<Application> filteredApps = searchFilter(usr.getUserID(),"","", Application.ApplicationStatus.WITHDRAWN);
        return filteredApps.size();
    }
    public List<String> listByUser(user usr, EnquiriesManager enqMan, ProjectManager proMan) {
        List<Application> filteredApps = searchFilter(usr.getUserID(),"","", null);
        List<String> output = new ArrayList<>(List.of(""));
        for (Application a : filteredApps) {
            output.set(0, output.get(0) + proMan.getProjectByName(usr, a.getProjectId(), enqMan, false).get(0) + "\n\n" + a.view());
        }
        return output;
    }

    public List<String> listByProject(user usr, String projectId) { //todo: perms checking. combine into 1 fn?
        List<Application> filteredApps = searchFilter("",projectId,"", null);
        List<String> output = new ArrayList<>(List.of(""));
        for (Application a : filteredApps) {
            output.set(0, output.get(0) + a.view());
        }
        return output;
    }
    private int generateNewApplicationId() {
        int maxId = 0;
        for (Application a : applicationList) {
            if (Integer.parseInt(a.getApplicationId()) > maxId) {
                maxId = Integer.parseInt(a.getApplicationId());
            }
        }
        return maxId + 1;
    }
    public void newApplication(user usr, String projectId, String flatType, ProjectManager proMan) {
        if (usr instanceof HDBOfficer) {
            Project p = proMan.getProjectObjByName(usr, projectId, false);
            if ((p != null) && (p.getOfficersIDList().contains(usr.getUserID()))) {
                System.out.println("You cannot apply for a project you are officer of");
                return;
            } //else continue to check for existing applications
        } else if (!(usr instanceof Applicant)) {
            System.out.println("Your user type cannot submit applications");
            return;
        }
        if (countByUser(usr) == 0) {
            if (proMan.projectExists(usr, projectId, true) > 0) {
                Application newApplication =  new Application(
                        String.valueOf(generateNewApplicationId()),
                        usr.getUserID(),
                        projectId,
                        Application.ApplicationStatus.PENDING,
                        LocalDate.now(),
                        null,
                        flatType
                );
                applicationList.add(newApplication);
            } else {
                System.out.println("No such project");
            }
        } else {
            System.out.println("You already have an application");
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






