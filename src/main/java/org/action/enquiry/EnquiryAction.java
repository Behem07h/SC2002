package org.action.enquiry;
import org.Users.user;
import org.action.*;
import org.UI.*;
import org.action.project.ProjectManager;

import java.util.List;

public interface EnquiryAction {
//    void submitEnquiries();
//    void deleteEnquiries();
//    void editEnquiries();
//    void replyEnquiries();
//    String [] submitEnquiry();
//    String [] deleteEnquiry();
//    void editEnquiry();
//    void replyEnquiry();
    List<String> submitEnquiry(user usr, String text, String projectID);
    List<String> deleteEnquiries(user usr, String enquiryIdStr);
    List<String> editEnquiries(user usr, String newText, String enquiryIdStr);
    List<String> replyEnquiries(user usr, String reply, String enquiryIdStr);
//    void replyEnquiries();
}

