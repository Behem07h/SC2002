package org.action.enquiry;

import org.action.*;
import java.util.*;

public class EnquiriesManager implements EnquiryAction {
    private List<Enquiries> enquiriesList;
    public EnquiriesManager() {
        this.enquiriesList = new ArrayList<Enquiries>();
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
}
