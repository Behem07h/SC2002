/**
 * Manages registration operations for the project management system.
 *
 * <p>This class provides functionality for creating, storing, retrieving,
 * and processing registrations. It handles user registration for projects,
 * registration approval/rejection by managers, and officer assignment to projects.</p>
 *
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 */
package org.action.registration;

import org.Users.HDBManager.HDBManager;
import org.Users.HDBOfficer.HDBOfficer;
import org.action.ApplicationManager;
import org.action.project.Project;
import org.action.project.ProjectManager;
import org.UI.ConfigLDR;
import org.Users.user;

import java.time.LocalDate;
import java.util.*;

/**
 * This class manages all registrations in the system.
 * It handles the creation, storage, retrieval, and processing of registrations.
 */
public class RegistrationManager{

    private final List<Register> registrationList;
    private final String path = "data/db";
    private final String filename =  "/registrations.csv";

    /**
     * Loads existing registrations from the CSV file.
     */
    public RegistrationManager(){
        this.registrationList = new ArrayList<>();

        ConfigLDR ldr = new ConfigLDR();
        Map<String,String[]> reg_map = ldr.ReadToArrMap(path + filename);
        for (String key : reg_map.keySet()) {
            String[] items = reg_map.get(key);
            if (items.length < 6) {
                System.out.println("Registration ID " + key + " missing params");
                continue;
            } //if param length too short, skip
            String projectID = items[0];
            String userID = items[1];
            String username = items [2];
            Register.RegistrationStatus status = Register.RegistrationStatus.valueOf(items[3]);
            String openingDate = items[4];
            String closingDate = items[5];
            this.registrationList.add(new Register(key, userID, username, projectID, status, openingDate, closingDate));
        }
    }

    /**
     * Stores all registrations to the CSV file.
     * Should be called when quitting the program.
     */
    public void store() {
        // run this when quitting program to store to csv
        Map<String,String[]> reg_map = new HashMap<>();
        for (Register reg : registrationList) {
            String[] items = {reg.getProjectID(), reg.getUserID(), reg.getUsername(), String.valueOf(reg.getStatus()), String.valueOf(reg.getSubmissionDate()), String.valueOf(reg.getClosingDate())};
            reg_map.put(String.valueOf(reg.getRegistrationID()),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(path + filename,reg_map);
    }

    /**
     * Filters registrations based on various criteria.
     *
     * @param userID The user ID to filter by (empty string to ignore)
     * @param username The username to filter by (empty string to ignore)
     * @param projectID The project ID to filter by (empty string to ignore)
     * @param registrationID The registration ID to filter by (empty string to ignore)
     * @param statusWhitelist List of acceptable status values to filter by
     * @return A list of registrations matching the filter criteria
     */
    private List<Register> searchFilter(String userID, String username, String projectID, String registrationID, List<Register.RegistrationStatus> statusWhitelist) {
        List<Register> out = new ArrayList<>();
        for (Register reg : registrationList) {
            if (reg.filter(userID, username, projectID, registrationID, statusWhitelist)) {
                out.add(reg);
            }
        }
        return out;
    }

    /**
     * Counts the number of approved registrations for a user.
     *
     * @param usr The user to count registrations for
     * @return The number of approved registrations for the user
     */
    private int countByUser(user usr) {
        List<Register> filteredReg = searchFilter(usr.getUserID(),"","","", List.of(Register.RegistrationStatus.APPROVED));
        return filteredReg.size();
    }

    /**
     * Generates a new unique registration ID.
     *
     * @return A new registration ID
     */
    private int generateNewRegistrationID() {
        int maxId = 0;
        for (Register reg : registrationList) {
            if (Integer.parseInt(reg.getRegistrationID()) > maxId) {
                maxId = Integer.parseInt(reg.getRegistrationID());
            }
        }
        return maxId + 1;
    }

    /**
     * Checks if a user has already applied to a project as an applicant.
     *
     * @param usr The user to check
     * @param projectID The project ID to check
     * @param appMan The ApplicationManager instance to check for officer
     * @return true if the user has applied as an applicant, false otherwise
     */
    public boolean appliedAsApplicant(user usr, String projectID, ApplicationManager appMan) {
        if (appMan.checkForOfficer(usr, projectID) != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Registers a user for a project.
     * Validates the registration against various business rules.
     *
     * @param usr The user to register
     * @param projectId The project to register for
     * @param proMan The ProjectManager instance to use for validation
     * @param appMan The ApplicationManager instance to use for validation
     */
    public void registerProject(user usr, String projectId, ProjectManager proMan, ApplicationManager appMan) {
        if (usr instanceof HDBOfficer) {
            Project proj = proMan.getProjectObjByName(usr, projectId, true);
            if ((proj != null) && (proj.getOfficersIDList().contains(usr.getUserID()))) {
                System.out.println("You are currently an officer for a project");
                return;
            } //else continue to check for existing applications
        } else {
            System.out.println("Your user type cannot register for projects");
            return;
        }
        // allow reg if user does not have approved registration and has not applied as applicant
        if (countByUser(usr) == 0 && !(appliedAsApplicant(usr, projectId, appMan))) {
            if (proMan.projectExists(usr, projectId, true) > 0) {
                Register newRegistration =  new Register(
                        String.valueOf(generateNewRegistrationID()),
                        usr.getUserID(),
                        usr.getUsername(),
                        projectId,
                        Register.RegistrationStatus.PENDING,
                        String.valueOf(LocalDate.now()),
                        "" 
                );
                registrationList.add(newRegistration);
            } else {
                System.out.println("No such project");
            }
        }
        if (countByUser(usr) != 0) {
            System.out.println("You have already registered for another project");
        }
        if (appliedAsApplicant(usr, projectId, appMan)) {
            System.out.println("You have applied for this project as an applicant");
        }
    }

    /**
     * Retrieves a registration by its ID.
     *
     * @param registrationID The ID of the registration to retrieve
     * @return The Register object, or null if not found
     */
    private Register retrieveRegistration(String registrationID) {
        for (Register reg : registrationList) {
            if (reg.getRegistrationID().equalsIgnoreCase(registrationID)) {
                return reg;
            }
        }
        System.out.println("No registration found with ID: " + registrationID);
        return null;
    }

    /**
     * Processes a registration by approving or rejecting it.
     * Only HDBManager users can process registrations.
     *
     * @param usr The user processing the registration
     * @param registrationID The ID of the registration to process
     * @param action The action to take ("APPROVED" or "REJECTED")
     * @param proMan The ProjectManager instance to use for assigning officers
     */
    public void processRegistration(user usr, String registrationID, String action, ProjectManager proMan) {
        if (!(usr instanceof HDBManager)) {
            System.out.println("You do not have the perms to process project registrations");
            return;
        }
        Register reg = retrieveRegistration(registrationID);
        if (reg == null) {
            System.out.println("Registration ID \"" + registrationID + "\" not found.");
            return;
        }
        switch (action) {
            case "APPROVED":
                System.out.println("Processing approval for registration ID: " + registrationID);
                reg.approveRegistration();
                assignOfficer(usr, reg, proMan);
                break;

            case "REJECTED":
                System.out.println("Processing rejection for registration ID: " + registrationID);
                reg.rejectRegistration();
                break;

            default:
                System.out.println("Unsupported target status: " + action);
        }
    }

    public void assignOfficer(Register reg, ProjectManager proMan) {
        Project proj = proMan.getProjectObjByName(null, reg.getProjectID(), false);

        if (proj != null) {
            String officerID = reg.getUserID();
            String currentOfficerIDList = proj.getOfficersIDList();

            if(!currentOfficerIDList.contains(reg.getUserID())) {
                List<String> officersIDList;
                officersIDList = new ArrayList<>(Arrays.asList(currentOfficerIDList.split(":")));
                officersIDList.add(officerID);
                proj.editOfficerList(String.join(":", officersIDList));
            }
        }
    }

    /**
     * Lists all pending registrations for a project.
     * Only HDBManager users can view this information.
     *
     * @param usr The user requesting the list
     * @param projectID The project ID to filter by
     * @return A list of strings containing the pending registrations, or an empty list if none found
     */
    public List<String> listPendingReg(user usr, String projectID) {
        List<String> output = new ArrayList<>(List.of(""));
        if(usr instanceof HDBManager) {
            List<Register> pendingReg = searchFilter("","",projectID,"", List.of(Register.RegistrationStatus.PENDING));
            if (pendingReg.isEmpty()) {
                System.out.println("No pending registrations found for this project");
                return output;
            }
            output.set(0, output.get(0)+String.format("Pending Registrations for %s\n", projectID));
            for (Register reg : pendingReg) {
                output.set(0,output.get(0)+reg.view_full());
            }
            return output;
        } else {
            System.out.println("You do not have the perms to view pending project registrations");
            return output;
        }
    }
    /**
     * Only HDBOfficer users can view their own pending registrations.
     *
     * @param usr The user requesting the list
     * @return A list of strings containing the pending registrations, or an empty list if none found
     */
    public List<String> listPendingReg(user usr) {
        List<String> output = new ArrayList<>(List.of(""));
        if(usr instanceof HDBOfficer) {
            List<Register> pendingReg = searchFilter(usr.getUserID(),"","","", List.of(Register.RegistrationStatus.PENDING));
            if (pendingReg.isEmpty()) {
                System.out.println("No pending registrations found");
                return output;
            }
            System.out.println("Your Pending Registrations:");
            for (Register reg : pendingReg) {
                output.set(0,output.get(0)+reg.view());
            }
            return output;
        } else {
            System.out.println("You do not have the perms to view pending project registrations");
            return output;
        }
    }

    /**
     * Gets the list of all registrations.
     *
     * @return The list of all registrations
     */
    public List<Register> getRegistrationList() {
        return registrationList;
    }

}
