/**
 * Manages the enquiries within the system. (Controller)
 *
 * <p>This class implements the EnquiryAction interface to provide functionality for creating,
 * managing, and responding to enquiries. Also handles persistence of enquiry data. </p>
 *
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.action.enquiry;

import org.UI.ConfigLDR;
import org.Users.*;
import org.Users.HDBManager.HDBManager;
import org.Users.HDBOfficer.HDBOfficer;
import org.action.project.Project;
import org.action.project.ProjectManager;

import java.time.LocalDateTime;
import java.util.*;


public class EnquiriesManager implements EnquiryAction {

    /** List of all enquiries in the BTO system */
    private final List<Enquiries> enquiriesList;

    /** Path to the data directory */
    private final String path = "data/db";

    /** Filename for the enquiries data storage */
    private final String filename = "/enquiries.csv";
    /**
     * Constructor for the EnquiriesManager.
     * Loads enquiries content from the CSV file during initialization.
     */
    public EnquiriesManager() {
        //load enquiries from csv
        this.enquiriesList = new ArrayList<>();

        ConfigLDR ldr = new ConfigLDR();
        Map<String,String[]> enq_map = ldr.ReadToArrMap(path + filename);
        for (String key : enq_map.keySet()) {
            String[] items = enq_map.get(key);
            if (items.length < 6) {
                System.out.println("Enquiry ID " + key + " missing params");
                continue;
            } //if param length too short, skip

            String projectID = items[0];
            String userId = items[1];
            String username = items[2];
            String text = items[3].replace("\\:",",");
            String reply = items[4].replace("\\:",",");
            LocalDateTime timestamp = LocalDateTime.parse(items[5]);

            this.enquiriesList.add(new Enquiries(key, projectID, userId, username, text, reply, timestamp));
        }
    }

    /**
     * Stores the current state of enquiries to CSV file.
     * Should be called when quitting the program.
     */
    public void store() {
        // run this when quitting program to store to csv
        Map<String,String[]> enq_map = new HashMap<>();
        for (Enquiries e : enquiriesList) {
            String[] items = {e.getProjectID(),e.getUserID(),e.getUsername(),e.getText().replace(",","\\:"),e.getReply().replace(",","\\:"), String.valueOf(e.getTimestamp())};
            enq_map.put(String.valueOf(e.getID()),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(path + filename,enq_map);
    }

    /**
     * Filters enquiries based on the provided parameters.
     *
     * @param usr The user performing the search
     * @param userId Filter by user ID
     * @param projectId Filter by project ID
     * @param enquiryId Filter by enquiry ID
     * @return A list of enquiries matching the filter criteria
     */
    private List<Enquiries> searchFilter(user usr, String userId, String projectId, String enquiryId) {
        List<Enquiries> out = new ArrayList<>();
        for (Enquiries e : enquiriesList) {
            if (e.filter(userId, projectId, enquiryId)) {
                out.add(e);
            }
        }
        return out;
    }

    /**
     * Gets enquiries for a specific project.
     *
     * @param usr The user requesting the enquiries
     * @param projectName The name/ID of the project
     * @return A list of strings containing the project name and enquiries information
     */
    public List<String> getEnquiriesByProject(user usr, String projectName) {
        List<Enquiries> filteredEnquiries;
        List<String> out = new ArrayList<>(List.of(String.format("%s",projectName),""));
        filteredEnquiries = searchFilter(usr,"",projectName,"");
        for (Enquiries e : filteredEnquiries) {
            out.set(1, out.get(1) + "\n" + e.toString());
        }
        if (out.get(1).isEmpty()) {
            out.set(1, "No enquiries for this project");
        }
        return out;
    }
    /**
     * Counts the number of enquiries for a specific project.
     *
     * @param usr The user requesting the count
     * @param projectName The name/ID of the project
     * @return The number of enquiries for the project
     */
    public int countProjectEnquiries(user usr, String projectName) {
        List<Enquiries> filteredEnquiries;
        filteredEnquiries = searchFilter(usr,"",projectName,"");
        return filteredEnquiries.size();
    }

    /**
     * Gets enquiries created by a specific user.
     *
     * @param usr The user whose enquiries to retrieve
     * @return A list of strings containing the user ID and enquiries information
     */
    public List<String> getEnquiriesByUser(user usr) {
        List<Enquiries> filteredEnquiries;
        List<String> out = new ArrayList<>(List.of(String.format("%s",usr.getUserID()),""));
        filteredEnquiries = searchFilter(usr,usr.getUserID(),"","");
        for (Enquiries e : filteredEnquiries) {
            out.set(1, out.get(1) + "\n" + e.toString());
        }
        if (out.get(1).isEmpty()) {
            out.set(1, "No enquiries by this user");
        }
        return out;
    }

    /**
     * Gets an enquiry by its ID.
     *
     * @param usr The user requesting the enquiry
     * @param enquiryId The ID of the enquiry
     * @return A list of strings containing the project ID and enquiry details
     */
    public List<String> getEnquiriesById(user usr, String enquiryId) {
        List<Enquiries> filteredEnquiries;
        List<String> out = new ArrayList<>(List.of("",""));
        filteredEnquiries = searchFilter(usr,"","",enquiryId);
        for (Enquiries e : filteredEnquiries) {
            out.set(0, e.getProjectID());
            out.set(1, out.get(1) + "\n" + e.view_full());
        }
        if (out.get(1).isEmpty()) {
            out.set(1, "No such enquiry");
        }
        return out;
    }

    /**
     * Generates a new enquiry ID.
     *
     * @return A new unique ID for an enquiry
     */
    private int generateNewEnquiryId() {
        int maxId = 0;
        for (Enquiries e : enquiriesList) {
            if (Integer.parseInt(e.getID()) > maxId) {
                maxId = Integer.parseInt(e.getID());
            }
        }
        return maxId + 1;
    }

    /**
     * Submits a new enquiry.
     *
     * @param usr The user submitting the enquiry
     * @param text The text content of the enquiry
     * @param projectID The ID of the project the enquiry is related to
     * @return A list of strings containing the result of the operation and any messages
     */
    // Modified to match Context class expectations
    public List<String> submitEnquiry(user usr, String text, String projectID) {
        List<String> result = new ArrayList<>();

        if(text == null || text.isEmpty()) {
            System.out.println("Text is empty");
            result.set(0, "ERROR: Text is empty");
            return result;
        }

        if(usr == null || usr.getUserID().isEmpty()) {
            System.out.println("UserID is empty");
            result.set(0, "ERROR: UserID is empty");
            return result;
        }

        if(projectID == null || projectID.isEmpty()) {
            System.out.println("Project ID is empty");
            result.set(0, "ERROR: Project ID is empty");
            return result;
        }

        String newID = String.valueOf(generateNewEnquiryId());
        Enquiries newEnquiry = new Enquiries(
                newID,
                projectID,
                usr.getUserID(),
                usr.getUsername(),
                text,
                "",
                LocalDateTime.now()
        );

        // Add to the enquiries list
        enquiriesList.add(newEnquiry);

        System.out.println("Enquiry successfully submitted with ID: " + newID);
        result = getEnquiriesByProject(usr, projectID);
        return result;
    }

    /**
     * Retrieves an enquiry by its ID.
     *
     * @param id The ID of the enquiry to retrieve
     * @return The enquiry object, or null if not found
     */
    private Enquiries getEnquiry(String id) {
        for (Enquiries e : enquiriesList) {
            if (Objects.equals(e.getID(), id)) {
                return e;
            }
        }
        return null;
    }

    /**
     * Deletes an enquiry.
     *
     * @param usr The user attempting to delete the enquiry
     * @param enquiryId The ID of the enquiry to delete
     * @return A list of strings containing the result of the operation and any messages
     */
    @Override
    public List<String> deleteEnquiries(user usr, String enquiryId) {
        List<String> result = new ArrayList<>(List.of("",""));

        try {
            Enquiries enquiry = getEnquiry(enquiryId);

            if(enquiry == null) {
                System.out.println("Enquiry not found");
                result.set(0, "ERROR: Enquiry not found");
                return result;
            } else if ((enquiry.getUserID().equals(usr.getUserID()) && (enquiry.getReply().isEmpty())) || usr instanceof HDBManager) {
                enquiriesList.remove(enquiry);
                result.set(0, "SUCCESS");
                result.set(1, "Enquiry " + enquiryId + " deleted successfully");
                return result;
            } else {
                System.out.println("You are not allowed to delete this enquiry");
                result.set(0, "ERROR: Unauthorized to delete enquiry");
                return result;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID format");
            result.set(0, "ERROR: Invalid enquiry ID format");
            return result;
        }
    }

    /**
     * Checks if a user can reply to an enquiry.
     *
     * @param usr The user attempting to reply
     * @param enquiryID The ID of the enquiry to reply to
     * @param proMan Checking if the user is a project manager or project officer
     * @return true if the user can reply, false otherwise
     */
    public boolean canReply(user usr, String enquiryID, ProjectManager proMan) {
        if (!(usr instanceof HDBOfficer) && !(usr instanceof HDBManager)) {
            System.out.println("Only HDB Officers and Managers can reply to enquiries.");
            return false;
        }

        // Get the enquiry details to check the project
        Enquiries enquiry = getEnquiry(enquiryID);
        if (enquiry == null) {
            System.out.println("Enquiry not found.");
            return false;
        }

        // Get the project ID from the enquiry
        Project pro = proMan.getProjectObjByName(usr, enquiry.getProjectID(), false);
        // If user is an officer, check if they are handling this project
        if ((usr instanceof HDBManager && Objects.equals(pro.getManagerId(), usr.getUserID())) || (usr instanceof HDBOfficer && pro.getOfficersIDList().contains(usr.getUserID()))) {
            return true;
        } else {
            System.out.println("You can only reply to enquiries regarding projects you are in charge of");
            return false;
        }
    }

    /**
     * Edits the text of an existing enquiry.
     *
     * @param usr The user attempting to edit the enquiry
     * @param newText The new text content for the enquiry
     * @param enquiryId The ID of the enquiry to edit
     * @return A list of strings containing the result of the operation and any messages
     */
    @Override
    public List<String> editEnquiries(user usr, String newText, String enquiryId) {
        List<String> result = new ArrayList<>(List.of("",""));

        try {
            Enquiries enquiry = getEnquiry(enquiryId);

            if(enquiry == null) {
                System.out.println("Enquiry not found");
                result.set(0, "ERROR: Enquiry not found");
                return result;
            } else if(enquiry.getUserID().equals(usr.getUserID()) && enquiry.getReply().isEmpty()) {
                enquiry.setText(newText);
                result = getEnquiriesByProject(usr,enquiry.getProjectID());
                return result;
            } else {
                System.out.println("You are not allowed to edit this enquiry");
                result.set(0, "ERROR: Unauthorized to edit enquiry");
                return result;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID format");
            result.set(0, "ERROR: Invalid enquiry ID format");
            return result;
        }
    }

    /**
     * Adds a reply to an existing enquiry.
     * It also checks if it is a HDB officer or HDB manager replying the enquiries
     * @param usr The user replying to the enquiry
     * @param reply The text of the reply
     * @param enquiryId The ID of the enquiry to reply to
     * @return A list of strings containing the result of the operation and any messages
     */
    @Override
    public List<String> replyEnquiries(user usr, String reply, String enquiryId) {
        List<String> result = new ArrayList<>(List.of("",""));

        try {
            Enquiries enquiry = getEnquiry(enquiryId);

            if(enquiry == null) {
                System.out.println("Enquiry not found");
                result.set(0, "ERROR: Enquiry not found");
                return result;
            }

            if(usr instanceof HDBManager || usr instanceof HDBOfficer) {
                enquiry.setReply(reply);
                result = getEnquiriesById(usr,enquiryId);
            } else {
                System.out.println("You are not authorized to reply to enquiries");
                result.set(0, "ERROR: Unauthorized to reply to enquiries");
            }
            return result;

        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID format");
            result.set(0, "ERROR: Invalid enquiry ID format");
            return result;
        }
    }
}