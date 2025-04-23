/**
 * Manages housing applications in the HDB system.
 *
 * <p> This class handles CRUD operations for applications, including storing applications,
 * filtering and retrieving applications, processing approvals, rejections, and withdrawals.
 * It also provides reporting capabilities for booking receipts.</p>
 *
 * Applications are loaded from and stored to CSV files for persistence.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.action;

import org.UI.ConfigLDR;
import org.Users.Applicant.Applicant;
import org.Users.Applicant.ApplicantManager;
import org.Users.GenericManager;
import org.Users.HDBManager.HDBManager;
import org.Users.HDBOfficer.HDBOfficer;
import org.Users.user;
import org.action.enquiry.EnquiriesManager;
import org.action.project.Project;
import org.action.project.ProjectManager;
import org.receipt.BookingReceipt;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static org.action.Application.ApplicationStatus.PENDING;
import static org.action.Application.ApplicationStatus.SUCCESSFUL;

public class ApplicationManager {

    /** List containing all applications in the system */
    private final List<Application> applicationList;

    /** Base directory path for data files */
    private final String path = "data/db";

    /** Filename for applications data */
    private final String filename =  "/applications.csv";

    /**
     * Constructs a new ApplicationManager which loads existing applications from CSV.
     * Initializes the application list and populates it with data from the file.     */
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

    /**
     * Stores all applications to the CSV file.
     * This method should be called when exiting the program to ensure data persistence.
     */
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


    /**
     * Filters applications based on specified criteria.
     *
     * @param userId User ID to filter by, or empty string to ignore this filter
     * @param projectId Project ID to filter by, or empty string to ignore this filter
     * @param applicationId Application ID to filter by, or empty string to ignore this filter
     * @param statusBlacklist List of statuses to exclude from results
     * @return List of applications matching the filter criteria
     */
    private List<Application> searchFilter(String userId, String projectId, String applicationId, List<Application.ApplicationStatus> statusBlacklist) {
        List<Application> out = new ArrayList<>();
        for (Application a : applicationList) {
            if (a.filter(userId, projectId, applicationId, statusBlacklist)) {
                out.add(a);
            }
        }
        return out;
    }

    /**
     * Counts the number of active applications for a user.
     * Applications with status WITHDRAWN or UNSUCCESSFUL are not counted.
     *
     * @param usr The user whose applications are being counted
     * @return Number of active applications for the user
     */
    private int countByUser(user usr) {
        List<Application> filteredApps = searchFilter(usr.getUserID(),"","", List.of(Application.ApplicationStatus.WITHDRAWN,Application.ApplicationStatus.UNSUCCESSFUL));
        return filteredApps.size();
    }

    /**
     * Lists all applications for a specific user.
     *
     * @param usr The user whose applications are being listed
     * @param enqMan The EnquiriesManager to retrieve project details
     * @param proMan The ProjectManager to retrieve project details
     * @return List of formatted strings containing application details
     */
    public List<String> listByUser(user usr, EnquiriesManager enqMan, ProjectManager proMan) {
        List<Application> filteredApps = searchFilter(usr.getUserID(),"","", List.of());
        List<String> output = new ArrayList<>(List.of(""));
        for (Application a : filteredApps) {
            output.set(0, output.get(0) + proMan.getProjectByName(usr, a.getProjectId(), enqMan, false).get(0) + "\n\n" + a.view());
        }
        return output;
    }

    /**
     * Lists all applications for a specific project.
     * If the user is an Applicant, only their own applications for the project are shown.
     * If the user is an HDBOfficer, all applications for the project are shown.
     *
     * @param usr The user requesting the list
     * @param projectId The ID of the project to list applications for
     * @return List of formatted strings containing application details
     */
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

    /**
     * Checks if a user has any active applications for a specific project.
     * Used to verify if an HDB officer is eligible to process applications.
     *
     * @param usr The user to check
     * @param projectId The project ID to check against
     * @return Number of active applications for the user on this project
     */
    public int checkForOfficer(user usr, String projectId) {
        List<Application> filteredApps = searchFilter(usr.getUserID(), projectId, "",
        List.of(PENDING,Application.ApplicationStatus.BOOKED, SUCCESSFUL));
        return filteredApps.size();
    }

    /**
     * Generates a new unique application ID.
     *
     * @return An integer ID
     */
    private int generateNewApplicationId() {
        int maxId = 0;
        for (Application a : applicationList) {
            if (Integer.parseInt(a.getApplicationId()) > maxId) {
                maxId = Integer.parseInt(a.getApplicationId());
            }
        }
        return maxId + 1;
    }

    /**
     * Creates a new application for a user.
     * Checks if the user is eligible to apply (e.g., not an officer of the project)
     * and if they don't already have an active application.
     *
     * @param usr The user creating the application
     * @param projectId The ID of the project being applied for
     * @param flatType The type of flat being applied for
     * @param proMan The ProjectManager to verify project details
     */
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
                        PENDING,
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

    /**
     * Retrieves an application by its ID.
     *
     * @param applicationId The ID of the application to retrieve
     * @return The Application object if found, null otherwise
     */
    private Application retrieveApplication(String applicationId) {
        for (Application app : applicationList) {
            if (app.getApplicationId().equalsIgnoreCase(applicationId)) {
                return app;
            }
        }
        System.out.println("No application found with ID: " + applicationId);
        return null;
    }

    public List<String> processApplicationOptions(String applicationId) {
        Application app = retrieveApplication(applicationId);
        List<String> options = new ArrayList<>(List.of("CANCEL"));
        if (app == null) {
            System.out.println("This application ID does not exist");
            options = List.of("CANCEL");
        } else {
            if (app.getStatus() == PENDING) {
                options = List.of("SUCCESSFUL","UNSUCCESSFUL","CANCEL");
            } else if (app.getStatus() == SUCCESSFUL) {
                options = List.of("BOOKED","CANCEL");
            }
        }
        return options;
    }

    /**
     * Processes an application's status change.
     * Only HDB officers assigned to the project can process applications.
     * Available actions are: BOOKED, SUCCESSFUL, UNSUCCESSFUL.
     *
     * @param usr The user processing the application (must be an HDBOfficer)
     * @param applicationId The ID of the application to process
     * @param action The action to perform (status to change to)
     * @param proMan The ProjectManager to verify project details
     */
    public void processApplication(user usr, String applicationId, String action, ProjectManager proMan) {
        if (!(usr instanceof HDBOfficer || usr instanceof HDBManager)) {
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

    /**
     * Processes a withdrawal request for an application.
     * Only HDB officers assigned to the project can process withdrawal requests.
     * Available actions are: WITHDRAW (approve), REJECT (reject withdrawal).
     *
     * @param usr The user processing the withdrawal (must be an HDBOfficer)
     * @param applicationId The ID of the application with withdrawal request
     * @param action The action to perform (WITHDRAW or REJECT)
     * @param proMan The ProjectManager to verify project details
     */
    public void processWithdrawal(user usr, String applicationId, String action, ProjectManager proMan) {
        if (!(usr instanceof HDBOfficer || usr instanceof HDBManager)) {
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

    /**
     * Submits a withdrawal request for an application.
     * Only the applicant who owns the application can request withdrawal.
     *
     * @param usr The applicant requesting withdrawal
     * @param applicationId The ID of the application to withdraw
     */
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

    /**
     * Generates booking receipts based on filter criteria.
     * This is used for reporting on booked applications.
     *
     * @param actor The user requesting the report
     * @param projectFilter Filter by project name (empty string to ignore)
     * @param flatTypeFilter Filter by flat type (empty string to ignore)
     * @param maritalFilter Filter by marital status (empty string to ignore)
     * @param proMan The ProjectManager to retrieve project details
     * @param managersList List of GenericManager to retrieve user details by user ID
     * @return List of BookingReceipt objects matching the filter criteria
     */
    public List<BookingReceipt> getBookingReceipts(
            user actor,
            String projectFilter,
            String flatTypeFilter,
            String maritalFilter,
            ProjectManager proMan,
            List<GenericManager<user>> managersList
    ) {
        List<BookingReceipt> receipts = new ArrayList<>();
    
        for (Application a : applicationList) {
            if (a.getStatus() != Application.ApplicationStatus.BOOKED) continue; //if not booked, skip
            if (!projectFilter.isEmpty() && !a.getProjectId().equals(projectFilter)) continue; //if not the right project, skip
            if (!flatTypeFilter.isEmpty() && !a.getFlatType().equals(flatTypeFilter)) continue; //if not the right flat type, skip

            user applicant = null;
            for (GenericManager<user> man : managersList) {
                applicant = man.findById(a.getApplicantId()); //get the user by id
                if (applicant != null) {
                    break;
                }
            }
            if (applicant == null) continue;

            if (!maritalFilter.isEmpty()
             && !applicant.getMaritalStatus().equalsIgnoreCase(maritalFilter))
                continue; //if not the right marital status, skip

            // build the flat-type details from the Project
            Project p = proMan.getProjectObjByName(actor, a.getProjectId(), false);
            if (p == null) continue; //if cant find the project obj, skip

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

    /**
     * Gets the complete list of applications managed by this ApplicationManager.
     *
     * @return The list of all Application objects
     */
    public List<Application> getApplicationList() {
        return applicationList;
    }
} 






