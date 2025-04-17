package org.UI;

import org.Users.user;
import org.action.ApplicationManager;
import org.action.enquiry.EnquiriesManager;
import org.action.projectcontroller;

import java.util.Scanner;

public class Context {
    private final EnquiriesManager enqMan;
    private final ApplicationManager appMan;
    private final projectcontroller proMan;
    private final user currentUser;
    public Context(user currentUser) {
        this.currentUser = currentUser;
        enqMan = new EnquiriesManager();
        appMan = new ApplicationManager();
    }

    public void endContext() {
        enqMan.storeEnquiries();
        appMan.storeApplications();
    }

    public String[] act(String action, Scanner sc) {
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
                proMan.getProjectList(user);
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
    }

    public user getCurrentUser() {
        return currentUser;
    }

    public ApplicationManager getAppMan() {
        return appMan;
    }

    public EnquiriesManager getEnqMan() {
        return enqMan;
    }
}
