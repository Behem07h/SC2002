package org.action.enquiry;
import org.Users.user;
import org.action.*;
import org.UI.*;
import org.action.project.ProjectManager;

import java.util.List;
/**
 * Interface for managing enquiries in the system.
 * Provides methods for submitting, deleting, editing, and replying to enquiries.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 */
public interface EnquiryAction {

    /**
     * Submits a new enquiry to the system.
     *
     * @param usr The user submitting the enquiry
     * @param text The text content of the enquiry
     * @param projectID The ID of the project the enquiry is related to
     * @return A list of strings containing the result of the operation and any messages
     */
    List<String> submitEnquiry(user usr, String text, String projectID);

    /**
     * Deletes an existing enquiry from the system.
     *
     * @param usr The user attempting to delete the enquiry
     * @param enquiryIdStr The ID of the enquiry to be deleted
     * @return A list of strings containing the result of the operation and any messages
     */

    List<String> deleteEnquiries(user usr, String enquiryIdStr);

    /**
     * Edits the text of an existing enquiry.
     *
     * @param usr The user attempting to edit the enquiry
     * @param newText The new text content for the enquiry
     * @param enquiryIdStr The ID of the enquiry to be edited
     * @return A list of strings containing the result of the operation and any messages
     */
    List<String> editEnquiries(user usr, String newText, String enquiryIdStr);

    /**
     * Adds a reply to an existing enquiry.
     *
     * @param usr The user replying to the enquiry
     * @param reply The text of the reply
     * @param enquiryIdStr The ID of the enquiry to reply to
     * @return A list of strings containing the result of the operation and any messages
     */
    List<String> replyEnquiries(user usr, String reply, String enquiryIdStr);
}

