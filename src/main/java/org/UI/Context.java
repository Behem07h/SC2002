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
    private final user usr;
    private String currentViewedProjectID;
    private String currentViewedEnquiryID;

    public Context(user currentUser) {
        usr = currentUser;
        enqMan = new EnquiriesManager();
        appMan = new ApplicationManager();
        proMan = new projectcontroller(); //todo: load and store projects to csv
    }

    public void endContext() {
        enqMan.storeEnquiries();
        appMan.storeApplications();
    }

    public String[] act(String action, Scanner sc) {
        String[] output = {"","","",""};
        String[] input = {"","","",""};
        switch (action){
            //enquiry methods
            case "submit-enquiry":
                //take user input of project id and enquiry text.
                //return full enquiry details
                if (currentViewedProjectID.isEmpty()) {
                    System.out.println("Enter project ID: ");
                    currentViewedProjectID = sc.nextLine();
                }
                System.out.println("Enter enquiry: ");
                input[0] = sc.nextLine();

                //output = enqMan.submitEnquiry(usr, input[0], currentViewedProjectID);
                currentViewedEnquiryID = output[0];
                break;
            case "delete-enquiry":
                //user input enquiry id
                //return success/failure and new project enquiries list
                System.out.println("Enter enquiry ID to confirm deletion: ");
                input[0] = sc.nextLine();
                //output = enqMan.deleteEnquiries(usr, input[0]);
                currentViewedEnquiryID = "";
                break;
            case "edit-enquiry":
                //user input enquiry id and new text
                //return new enquiry text
                if (currentViewedProjectID.isEmpty()) {
                    System.out.println("Enter project ID: ");
                    currentViewedProjectID = sc.nextLine();
                }
                if (currentViewedEnquiryID.isEmpty()) {
                    System.out.println("Enter enquiry ID to edit: ");
                    currentViewedEnquiryID = sc.nextLine();
                }
                //todo:call fn to print user's enquiry ids by project id
                input[1] = sc.nextLine();

                System.out.println("Enter edited enquiry: ");
                input[0] = sc.nextLine();
                //enqMan.editEnquiries(usr, input[0], currentViewedEnquiryID);
                break;
            case "reply-enquiry":
                //user input enquiry id and reply text
                //return full enquiry details
                if (currentViewedEnquiryID.isEmpty()) {
                    System.out.println("Enter enquiry ID to edit: ");
                    currentViewedEnquiryID = sc.nextLine();
                }
                System.out.println("Enter reply to enquiry: ");
                input[0] = sc.nextLine();
                //enqMan.replyEnquiries(usr, input[0], currentViewedEnquiryID);
                break;
            //application methods
            case "view-applications":
                //user input project id to view applications for project, or none to view own application
                System.out.println("Enter project ID to view applications: ");
                input[0] = sc.nextLine();
                if (input[0].length() > 0) {
                    //appMan.viewAllApplications(usr, input[0]);
                } else {
                    //appMan.viewApplicationsByUsr(usr);
                }

                break;
            case "add-application":
                //??
                break;
            case "retrieve-application":
                System.out.println("Enter an application ID to view: ");
                input[0] = sc.nextLine();
                //appMan.retrieveApplication(usr,input[0]);
                //??
                break;
            case "withdraw-application":
                //input application id, sends withdrawal for current application view
                //return withdrawal status
                System.out.println("Enter application ID to confirm withdrawal request: ");
                input[0] = sc.nextLine();
                //appMan.withdrawApplication(usr, input[0]);
                break;
            case "approve-application":
                //input application id, changes application status
                //return new application status
                System.out.println("Enter application ID to confirm approval: ");
                input[0] = sc.nextLine();
                //appMan.approveApplication(usr, input[0]);
                break;
            case "reject-application":
                //input application id, changes application status
                //return new application status
                System.out.println("Enter application ID to confirm rejection: ");
                input[0] = sc.nextLine();
                //appMan.rejectApplication(usr, input[0]);
                break;
            //project methods
            case "view-all-projects":
                //returns list of visible projects based on user
                //proMan.getProjectList(usr);
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                break;
            case "view-project":
                System.out.println("Enter project ID to view: ");
                input[0] = sc.nextLine();
                //output = proMan.getProjectDetails(usr, input[0]);
                currentViewedProjectID = input[0];
            case "search-projects":
                //input search term, output projects by keyword
                System.out.println("Enter search term: ");
                input[0] = sc.nextLine();
                //proMan.updateProject(usr, input[0]);
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                break;
            case "filter-projects":
                //input category, output projects by category
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                break;
            case "delete-projects":
                //input project id, output success/failure and new projects list
                System.out.println("Enter project ID to be deleted: ");
                input[0] = sc.nextLine();
                //proMan.deleteProjectByName(usr, input[0]);
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                break;
            default:
                return output;
        }
        return output;
    }

    public user getCurrentUser() {
        return usr;
    }

    public ApplicationManager getAppMan() {
        return appMan;
    }

    public EnquiriesManager getEnqMan() {
        return enqMan;
    }
}