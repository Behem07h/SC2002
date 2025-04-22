package org.action.registration;

import org.Users.HDBManager.HDBManager;
import org.Users.HDBOfficer.HDBOfficer;
import org.action.Application;
import org.action.ApplicationManager;
import org.action.project.Project;
import org.action.project.ProjectManager;
import org.UI.ConfigLDR;
import org.Users.user;

import java.time.LocalDate;
import java.util.*;

public class RegistrationManager{
    private final List<Register> registrationList;
    private final String path = "data/db";
    private final String filename =  "/registrations.csv";
    private final ApplicationManager appMan;

    public RegistrationManager(ApplicationManager appMan){
        this.registrationList = new ArrayList<>();
        this.appMan = appMan;

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

    private List<Register> searchFilter(String userID, String username, String projectID, String registrationID, List<Register.RegistrationStatus> statusBlacklist) {
        List<Register> out = new ArrayList<>();
        for (Register reg : registrationList) {
            if (reg.filter(userID, username, projectID, registrationID, statusBlacklist)) {
                out.add(reg);
            }
        }
        return out;
    }

    private int countByUser(user usr) {
        List<Register> filteredReg = searchFilter(usr.getUserID(),"","","", List.of(Register.RegistrationStatus.APPROVED));
        return filteredReg.size();
    }

    private int generateNewRegistrationID() {
        int maxId = 0;
        for (Register reg : registrationList) {
            if (Integer.parseInt(reg.getRegistrationID()) > maxId) {
                maxId = Integer.parseInt(reg.getRegistrationID());
            }
        }
        return maxId + 1;
    }

    public boolean appliedAsApplicant(user usr, String projectID){
        List<Application> applicationList = appMan.getApplicationList();
        for (Application a : applicationList) {
            if (a.getApplicantId().equals(usr.getUserID())) {
                return true;
            }
        }
        return false;
    }

    public void registerProject(user usr, String projectId, ProjectManager proMan) {
        if (usr instanceof HDBOfficer) {
            Project proj = proMan.getProjectObjByName(usr, projectId, true);
            if ((proj != null) && (proj.getOfficersIDList().contains(usr.getUserID()))) {
                System.out.println("You are currently an officer for a project");
                return;
            } //else continue to check for existing applications
        } else if (!(usr instanceof HDBOfficer)) {
            System.out.println("Your user type cannot register for projects");
            return;
        }
        // allow reg if user does not have approved registration and has not applied as applicant
        if (countByUser(usr) == 0 && !(appliedAsApplicant(usr, projectId))) {
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
        if (appliedAsApplicant(usr, projectId)) {
            System.out.println("You have applied for this project as an applicant");
        }

    }

    private Register retrieveRegistration(String registrationID) {
        for (Register reg : registrationList) {
            if (reg.getRegistrationID().equalsIgnoreCase(registrationID)) {
                return reg;
            }
        }
        System.out.println("No registration found with ID: " + registrationID);
        return null;
    }

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
                assignOfficer(reg, proMan);
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

    public void listPendingReg(user usr, String projectID) {
        if(usr instanceof HDBManager) {
            List<Register> pendingReg = searchFilter("","",projectID,"", List.of(Register.RegistrationStatus.PENDING));
            if (pendingReg.isEmpty()) {
                System.out.println("No pending registrations found for this project");
                return;
            }
            System.out.println("Pending Registrations for " + projectID);
            for (Register reg : pendingReg) {
                System.out.println("RegID: " + reg.getRegistrationID() +
                                ", Officer: " + reg.getUserID() +
                                ", Submitted On: " + reg.getSubmissionDate());
            }
        } else {
            System.out.println("You do not have the perms to view pending project registrations");
            return;
        }
    }

    public void listPendingReg(user usr) {
        if(usr instanceof HDBOfficer) {
            List<Register> pendingReg = searchFilter(usr.getUserID(),"","","", List.of(Register.RegistrationStatus.PENDING));
            if (pendingReg.isEmpty()) {
                System.out.println("No pending registrations found");
                return;
            }
            System.out.println("Your Pending Registrations:");
            for (Register reg : pendingReg) {
                System.out.println("RegID: " + reg.getRegistrationID() +
                                ", Project: " + reg.getProjectID() +
                                ", Submitted On: " + reg.getSubmissionDate());
            }
        } else {
            System.out.println("You do not have the perms to view pending project registrations");
            return;
        }
    }

    public List<Register> getRegistrationList() {
        return registrationList;
    }

    
}
