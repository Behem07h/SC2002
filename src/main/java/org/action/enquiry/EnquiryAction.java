package org.action.enquiry;
import org.Users.user;
import org.action.*;
import org.UI.*;

public interface EnquiryAction {
//    void submitEnquiries();
//    void deleteEnquiries();
//    void editEnquiries();
//    void replyEnquiries();
//    String [] submitEnquiry();
//    String [] deleteEnquiry();
//    void editEnquiry();
//    void replyEnquiry();
    String[] submitEnquiry(user usr, String text, String projectID);
    String[] deleteEnquiries(user usr, String enquiryIdStr);
    String[] editEnquiries(user usr, String newText, String enquiryIdStr);
    String[] replyEnquiries(user usr, String reply, String enquiryIdStr);
//    void replyEnquiries();
}

