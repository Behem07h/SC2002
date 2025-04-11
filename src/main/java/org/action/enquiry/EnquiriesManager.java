package org.action.enquiry;

import org.UI.ConfigLDR;
import org.action.*;

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

    public boolean deleteEnquiries(int enquiriesId,String username,boolean HDBManger, boolean HDBOfficer) {
        Enquiries enquiries = getEnquiry(enquiriesId);
        if(enquiries == null) {
            System.out.println("Enquiries not found");
            return false;
        }
        if(enquiries.getUsername().equals(username)  || HDBManger || HDBOfficer) {
            enquiriesList.remove(enquiries);
            return true;
        }
        else {
            System.out.println("You are not allowed to edit the enquiries");
            return false;
        }
    }
    @Override
    public void deleteEnquiries() {
        System.out.println("Enquiries deleted");
    }
    public boolean editEnquiries(int enquiriesId,String username,String text, boolean HDBManger, boolean HDBOfficer) {
        Enquiries enquiries = getEnquiry(enquiriesId);
        if(enquiries == null) {
            System.out.println("Enquries not found");
        }
        if(enquiries.getUsername().equals(username)  || HDBManger || HDBOfficer) {
            enquiries.setText(text);
            return true;
        }
        else {
            System.out.println("Enquiries not found");
            return false;
        }
    }
    @Override
    public void editEnquiries() {
        System.out.println("Enquiries edited");
    }
    public boolean replyEnquiries(int enquriesId,String reply, boolean HDBManger, boolean HDBOfficer) {
        Enquiries enquiries = getEnquiry(enquriesId);
        if(enquiries == null) {
            System.out.println("Enquiries not found");
        }
        boolean alreadyResponded = !enquiries.getReply().isEmpty();
        if(alreadyResponded  || HDBManger || HDBOfficer) {
            enquiries.setReply(reply);
            return true;
        }
        else {
            System.out.println("Enquiries not found");
            return false;
        }
    }
    @Override
    public void replyEnquiries() {
        System.out.println("Enquiries replied");
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

    public void updateStatus(String projectID){

    }
    public void categorizedEnquiries(){

    }
    public void logEnquiries(){

    }
}
