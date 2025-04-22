package org.action;

import org.UI.ConfigLDR;
import org.Users.Applicant.Applicant;
import org.Users.HDBOfficer.HDBOfficer;
import org.Users.user;
import org.action.enquiry.EnquiriesManager;
import org.action.project.Project;
import org.action.project.ProjectManager;
import org.receipt.BookingReceipt;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

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
            if (items.length < 7) {
                System.out.println("Application ID " + key + " missing params");
                continue;
            } //if param length too short, skip
            String projectID = items[0];
            String applicantId = items[1];
            Application.ApplicationStatus status = Application.ApplicationStatus.valueOf(items[2]);
            Application.WithdrawalStatus withdrawStatus = Application.WithdrawalStatus.valueOf(items[3]);
            String openingDate = items[4];
            String closingDate = items[5];
            String flatType = items[6];
            this.applicationList.add(new Application(key, applicantId, projectID, status, withdrawStatus, openingDate, closingDate, flatType));
        }
    }

    public void store() {
        // run this when quitting program to store to csv
        Map<String,String[]> appl_map = new HashMap<>();
        for (Application a : applicationList) {
            String[] items = {a.getProjectId(), a.getApplicantId(), String.valueOf(a.getStatus()), String.valueOf(a.getWithdrawStatus()), String.valueOf(a.getSubmissionDate()), String.valueOf(a.getClosingDate()), a.getFlatType()};
            appl_map.put(String.valueOf(a.getApplicationId()),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(path + filename,appl_map);
    }



    private List<Application> searchFilter(String userId, String projectId, String applicationId, List<Application.ApplicationStatus> statusBlacklist) {
        List<Application> out = new ArrayList<>();
        for (Application a : applicationList) {
            if (a.filter(userId, projectId, applicationId, statusBlacklist)) {
                out.add(a);
            }
        }
        return out;
    }

    private int countByUser(user usr) {
        List<Application> filteredApps = searchFilter(usr.getUserID(),"","", List.of(Application.ApplicationStatus.WITHDRAWN,Application.ApplicationStatus.UNSUCCESSFUL));
        return filteredApps.size();
    }
    public List<String> listByUser(user usr, EnquiriesManager enqMan, ProjectManager proMan) {
        List<Application> filteredApps = searchFilter(usr.getUserID(),"","", List.of());
        List<String> output = new ArrayList<>(List.of(""));
        for (Application a : filteredApps) {
            output.set(0, output.get(0) + proMan.getProjectByName(usr, a.getProjectId(), enqMan, false).get(0) + "\n\n" + a.view());
        }
        return output;
    }

    public List<String> listByProject(user usr, String projectId) {//todo: perms checking.
        List<Application> filteredApps;
        List<String> output = new ArrayList<>(List.of(""));

        if (usr instanceof Applicant) {
            filteredApps = searchFilter(usr.getUserID(), projectId, "", List.of());
            if (filteredApps.isEmpty()) {
                output.set(0, "You have no applications for this project.");
                return output;
            }
        }
        else {
            filteredApps = searchFilter("", projectId, "", List.of());
        }
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
                        Application.WithdrawalStatus.NIL,
                        String.valueOf(LocalDate.now()),
                        "",
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

    private Application retrieveApplication(String applicationId) {
        for (Application app : applicationList) {
            if (app.getApplicationId().equalsIgnoreCase(applicationId)) {
                return app;
            }
        }
        System.out.println("No application found with ID: " + applicationId);
        return null;
    }

    public void processApplication(user usr, String applicationId, String action, ProjectManager proMan) {
        if (!(usr instanceof HDBOfficer)) {
            System.out.println("You do not have the perms to process project applications");
            return;
        }
        Application app = retrieveApplication(applicationId);
        if (app == null) {
            System.out.println("Application ID \"" + applicationId + "\" not found.");
            return;
        }
        Project pro = proMan.getProjectObjByName(usr, app.getProjectId(), false);
        if (!(pro != null && pro.getOfficersIDList().contains(usr.getUserID()))) {
            System.out.println("You are not an officer for this project");
            return;
        }
        switch (action) {
            case "BOOKED":
                if (pro.flatAvailability(app.getFlatType()) > 0) {
                    System.out.println("Processing submission for application ID: " + applicationId);
                    app.book_flat();
                    pro.addBooking(applicationId, app.getFlatType());
                } else {
                    System.out.println("Project has no more available flats");
                }

                break;

            case "SUCCESSFUL":
                System.out.println("Processing approval for application ID: " + applicationId);
                app.acceptApplication();
                break;

            case "UNSUCCESSFUL":
                System.out.println("Processing rejection for application ID: " + applicationId);
                app.rejectApplication();
                break;

            default:
                System.out.println("Unsupported target status: " + action);
        }
    }

    public void processWithdrawal(user usr, String applicationId, String action, ProjectManager proMan) {
        if (!(usr instanceof HDBOfficer)) {
            System.out.println("You do not have the perms to process project withdrawal");
            return;
        }
        Application app = retrieveApplication(applicationId);
        if (app == null) {
            System.out.println("Application ID \"" + applicationId + "\" not found.");
            return;
        }
        Project pro = proMan.getProjectObjByName(usr, app.getProjectId(), false);
        if (!(pro != null && pro.getOfficersIDList().contains(usr.getUserID()))) {
            System.out.println("You are not an officer for this project");
            return;
        }
        switch (action) {
            case "WITHDRAW":
                System.out.println("Processing submission for application ID: " + applicationId);
                app.approveWithdrawal();
                pro.removeBooking(applicationId, app.getFlatType());
                break;

            case "REJECT":
                System.out.println("Processing approval for application ID: " + applicationId);
                app.rejectWithdrawal();
                break;

            default:
                System.out.println("Unsupported target status: " + action);
        }
    }

    public void requestWithdrawal(user usr, String applicationId) {
        if (!(usr instanceof HDBOfficer || usr instanceof Applicant)) {
            System.out.println("You do not have the perms to request withdrawal from projects");
            return;
        }
        Application app = retrieveApplication(applicationId);
        if (app == null) {
            System.out.println("Application ID \"" + applicationId + "\" not found.");
            return;
        } else if (!Objects.equals(app.getApplicantId(), usr.getUserID())) {
            System.out.println("You cannot request withdrawal for somebody else's application.");
            return;
        }

        app.requestWithdrawal();
    }

    
    public List<BookingReceipt> getBookingReceipts(
            user actor,
            String projectFilter,
            String flatTypeFilter,
            String maritalFilter,
            ProjectManager proMan,
            Function<String, user> lookupUser
    ) {
        List<BookingReceipt> receipts = new ArrayList<>();
    
        for (Application a : applicationList) {
            if (a.getStatus() != Application.ApplicationStatus.BOOKED) continue;
            if (!projectFilter.isEmpty() && !a.getProjectId().equals(projectFilter)) continue;
            if (!flatTypeFilter.isEmpty() && !a.getFlatType().equals(flatTypeFilter)) continue;
    
            user applicant = lookupUser.apply(a.getApplicantId());
            if (applicant == null) continue;
            if (!maritalFilter.isEmpty()
             && !applicant.getMaritalStatus().equalsIgnoreCase(maritalFilter))
                continue;
    
            // build the flat-type details from the Project
            Project p = proMan.getProjectObjByName(actor, a.getProjectId(), true);
            if (p == null) continue;
            int free1 = p.getFlatCount1() - p.getBookingCount1();
            String details = String.format(
                "%s – $%d; %d/%d free",
                p.getFlatType1(), p.getFlatPrice1(), free1, p.getFlatCount1()
            );
            if (p.getFlatType2() != null && !p.getFlatType2().isEmpty()) {
                int free2 = p.getFlatCount2() - p.getBookingCount2();
                details += String.format(
                  ", %s – $%d; %d/%d free",
                  p.getFlatType2(), p.getFlatPrice2(), free2, p.getFlatCount2()
                );
            }
            String projDetails = String.format("%s: %s", p.getProjectName(), details);
    
            receipts.add(new BookingReceipt(
                a.getFlatType(),
                applicant.getUsername(),
                applicant.getAge(),
                applicant.getUserID(),
                projDetails,
                applicant.getMaritalStatus()
            ));
        }
    
        return receipts;
    }


    public List<Application> getApplicationList() {
        return applicationList;
    }
} 






