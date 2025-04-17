package org.Users;

import org.UI.Context;
import org.action.ApplicationManager;
import org.action.enquiry.EnquiriesManager;

import java.util.Scanner;

public interface user {
    PermissionLevel perms = PermissionLevel.NONE;


    String getUserID();

    void setUserID(String userID);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getMaritalStatus();

    void setMaritalStatus(String maritalStatus);

    int getAge();

    void setAge(int age);


    boolean changePassword(String oldPassword, String newPassword);

    // Enum for Permission Levels
    public enum PermissionLevel {
        READ,   // Read permission
        WRITE,  // Write permission
        ADMIN,  // Administrator privileges
        NONE    // No special permissions (default value)
    }

    default String[] act(String action, Scanner sc, EnquiriesManager enqMan, ApplicationManager appMan) {
        String[] output = new String[] {};
        switch (action){
            //enquiry methods
            case "submit-enquiry":
                //take user input of project id and enquiry text.
                //return full enquiry details
                break;
            case "delete-enquiry":
                //user input enquiry id
                //return success/failure and new project enquiries list
                break;
            case "edit-enquiry":
                //user input enquiry id and new text
                //return new enquiry text
                break;
            case "reply-enquiry":
                //user input enquiry id and reply text
                //return full enquiry details
                break;
            //application methods
            case "view-applications":
                //user input project id to view applications for project, or none to view own application
                break;
            case "add-application":
                //??
                break;
            case "retrieve-application":
                //??
                break;
            case "withdraw-application":
                //input application id, sends withdrawal for current application view
                //return withdrawal status
                break;
            case "approve-application":
                //input application id, changes application status
                //return new application status
                break;
            case "reject-application":
                //input application id, changes application status
                //return new application status
                break;
            //project methods
            case "view-projects":
                //returns list of visible projects based on user
                break;
            case "search-projects":
                //input search term, output projects by keyword
                break;
            case "filter-projects":
                //input category, output projects by category
                break;
            case "delete-projects":
                //input project id, output success/failure and new projects list
                break;
            default:
                return output;
        }
        return output;
    } //wrapper fn that handles user inputs for all actions
}


