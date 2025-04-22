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
    private final List<Enquiries> enquiriesList;
    private final String path = "data/db";
    private final String filename = "/enquiries.csv";
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
    private List<Enquiries> searchFilter(user usr, String userId, String projectId, String enquiryId) {
        List<Enquiries> out = new ArrayList<>();
        for (Enquiries e : enquiriesList) {
            if (e.filter(userId, projectId, enquiryId)) {
                out.add(e);
            }
        }
        return out;
    }
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

    public int countProjectEnquiries(user usr, String projectName) {
        List<Enquiries> filteredEnquiries;
        filteredEnquiries = searchFilter(usr,"",projectName,"");
        return filteredEnquiries.size();
    }
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


    private int generateNewEnquiryId() {
        int maxId = 0;
        for (Enquiries e : enquiriesList) {
            if (Integer.parseInt(e.getID()) > maxId) {
                maxId = Integer.parseInt(e.getID());
            }
        }
        return maxId + 1;
    }

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

    private Enquiries getEnquiry(String id) {
        for (Enquiries e : enquiriesList) {
            if (Objects.equals(e.getID(), id)) {
                return e;
            }
        }
        return null;
    }
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


    public void processEnquiries(){
        int pending = 0;
        System.out.println("Enquiries processing");
        for (Enquiries e : enquiriesList) {
            if(e.getReply().isEmpty()){
                pending++;
                System.out.println("Enquiries id:" + e.getID());
            }
        }
        System.out.println("Enquiries needed to be processed:" + pending);
    }

    public void trackStatus(String projectID){
        int pending = 0;
        int answered = 0;
        System.out.println("Tracking status");
        for(Enquiries e : enquiriesList){
            if(e.getProjectID().equals(projectID))
            {
                if(e.getReply().isEmpty()){
                    pending++;
                }
                else
                {
                    answered++;
                }
            }
        }
        System.out.println("Enquiries pending:" + pending);
        System.out.println("Enquiries answered:" + answered);
    }

    public List<String> getUserEnquiries(user usr, String projectID) {
        List<String> enquiryIds = new ArrayList<>();

        for (Enquiries e : enquiriesList) {
            if (e.getUserID().equals(usr.getUserID()) &&
                    (projectID.isEmpty() || e.getProjectID().equals(projectID))) {
                enquiryIds.add(String.valueOf(e.getID()));
            }
        }

        return enquiryIds;
    }
}