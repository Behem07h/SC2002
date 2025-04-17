package org.action.enquiry;

import org.UI.ConfigLDR;
import org.Users.user;
import java.time.LocalDateTime;
import java.util.*;

public class EnquiriesManager implements EnquiryAction {
    private List<Enquiries> enquiriesList;
    private final String path = "data/db";
    public EnquiriesManager() {
        //load enquiries from csv
        this.enquiriesList = new ArrayList<Enquiries>();

        ConfigLDR ldr = new ConfigLDR();
        Map<String,String[]> enq_map = ldr.ReadToArrMap(path + "/enquiries.csv");
        for (String key : enq_map.keySet()) {
            String[] items = enq_map.get(key);
            if (items.length < 5) {
                System.out.println("Enquiry ID " + key + " missing params");
                continue;
            } //if param length too short, skip

            String text = items[3];
            String reply = items[4];
            int id = Integer.parseInt(key);
            LocalDateTime timestamp = LocalDateTime.parse(items[2]);
            String username = items[1];
            String projectID = items[0];
            this.enquiriesList.add(new Enquiries(text, id, reply, timestamp, username, projectID));
        }
    }

    public void storeEnquiries() {
        // run this when quitting program to store to csv
        Map<String,String[]> enq_map = new HashMap<>();
        for (Enquiries e : enquiriesList) {
            String[] items = {e.getProjectID(),e.getUsername(), String.valueOf(e.getTimestamp()),e.getText(),e.getReply()};
            enq_map.put(String.valueOf(e.getId()),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(path + "/enquiries.csv",enq_map);
    }


    public Enquiries getEnquiry(int id) {
        for (Enquiries e : enquiriesList) {
            if (e.getId() == id) {
                return e;
            }
        }
        return null;
    }

    private int generateNewEnquiryId() {
        int maxId = 0;
        for (Enquiries e : enquiriesList) {
            if (e.getId() > maxId) {
                maxId = e.getId();
            }
        }
        return maxId + 1;
    }

    @Override
    public String[] submitEnquiry(user usr, String text, String projectID) {
        String[] result = {"", "", "", ""};

        if(text == null || text.equals("")) {
            System.out.println("Text is empty");
            result[0] = "ERROR: Text is empty";
            return result;
        }

        if(usr == null || usr.getUsername().equals("")) {
            System.out.println("Username is empty");
            result[0] = "ERROR: Username is empty";
            return result;
        }

        if(projectID == null || projectID.equals("")) {
            System.out.println("Project ID is empty");
            result[0] = "ERROR: Project ID is empty";
            return result;
        }

        int newID = generateNewEnquiryId();
        Enquiries newEnquiry = new Enquiries(
                text,
                newID,
                "",
                LocalDateTime.now(),
                usr.getUsername(),
                projectID
        );

        // Add to the enquiries list
        enquiriesList.add(newEnquiry);

        System.out.println("Enquiry successfully submitted with ID: " + newID);
        result[0] = String.valueOf(newID);
        result[1] = text;
        result[2] = usr.getUsername();
        result[3] = projectID;
        return result;
    }

//    @Override
//    public void submitEnquiries() {
//        // Interface implementation - used as placeholder
//        System.out.println("Default submit enquiries method called");
//    }
//
//    @Override
//    public void deleteEnquiries() {
//        // Interface implementation - used as placeholder
//        System.out.println("Default delete enquiries method called");
//    }
    @Override
    public String[] deleteEnquiries(user usr, String enquiryIdStr) {
        String[] result = {"", "", "", ""};

        try {
            int enquiryId = Integer.parseInt(enquiryIdStr);
            Enquiries enquiry = getEnquiry(enquiryId);

            if(enquiry == null) {
                System.out.println("Enquiry not found");
                result[0] = "ERROR: Enquiry not found";
                return result;
            }

            if(enquiry.getUsername().equals(usr.getUsername()) || usr.isHDBManager() || usr.isHDBOfficer()) {
                enquiriesList.remove(enquiry);
                result[0] = "SUCCESS";
                result[1] = "Enquiry " + enquiryId + " deleted successfully";
                return result;
            } else {
                System.out.println("You are not allowed to delete this enquiry");
                result[0] = "ERROR: Unauthorized to delete enquiry";
                return result;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID format");
            result[0] = "ERROR: Invalid enquiry ID format";
            return result;
        }
    }
    @Override
    public String[] editEnquiries(user usr, String newText, String enquiryIdStr) {
        String[] result = {"", "", "", ""};

        try {
            int enquiryId = Integer.parseInt(enquiryIdStr);
            Enquiries enquiry = getEnquiry(enquiryId);

            if(enquiry == null) {
                System.out.println("Enquiry not found");
                result[0] = "ERROR: Enquiry not found";
                return result;
            }

            if(enquiry.getUsername().equals(usr.getUsername()) || usr.isHDBManager() || usr.isHDBOfficer()) {
                enquiry.setText(newText);
                result[0] = "SUCCESS";
                result[1] = newText;
                result[2] = String.valueOf(enquiryId);
                return result;
            } else {
                System.out.println("You are not allowed to edit this enquiry");
                result[0] = "ERROR: Unauthorized to edit enquiry";
                return result;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID format");
            result[0] = "ERROR: Invalid enquiry ID format";
            return result;
        }
    }
//
//    @Override
//    public void editEnquiries() {
//        // Interface implementation - used as placeholder
//        System.out.println("Default edit enquiries method called");
//    }
@Override
public String[] replyEnquiries(user usr, String reply, String enquiryIdStr) {
        String[] result = {"", "", "", ""};

        try {
            int enquiryId = Integer.parseInt(enquiryIdStr);
            Enquiries enquiry = getEnquiry(enquiryId);

            if(enquiry == null) {
                System.out.println("Enquiry not found");
                result[0] = "ERROR: Enquiry not found";
                return result;
            }

            if(usr.isHDBManager() || usr.isHDBOfficer()) {
                enquiry.setReply(reply);
                result[0] = "SUCCESS";
                result[1] = reply;
                result[2] = String.valueOf(enquiryId);
                result[3] = enquiry.getText();
                return result;
            } else {
                System.out.println("You are not authorized to reply to enquiries");
                result[0] = "ERROR: Unauthorized to reply to enquiries";
                return result;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid enquiry ID format");
            result[0] = "ERROR: Invalid enquiry ID format";
            return result;
        }
    }

    @Override
    public void replyEnquiries() {
        // Interface implementation - used as placeholder
        System.out.println("Default reply enquiries method called");
    }

    public void processEnquiries(){
        int pending = 0;
        System.out.println("Enquiries processing");
        for (Enquiries e : enquiriesList) {
            if(e.getReply().isEmpty()){
                pending++;
                System.out.println("Enquiries id:" + e.getId());
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

    // Added to get all enquiries for a user and project
    public String[] getUserEnquiries(user usr, String projectID) {
        List<String> enquiryIds = new ArrayList<>();

        for (Enquiries e : enquiriesList) {
            if (e.getUsername().equals(usr.getUsername()) &&
                    (projectID.isEmpty() || e.getProjectID().equals(projectID))) {
                enquiryIds.add(String.valueOf(e.getId()));
            }
        }

        return enquiryIds.toArray(new String[0]);
    }
}