package org.UI;

import org.Users.user;
import org.action.ApplicationManager;
import org.action.enquiry.EnquiriesManager;
import org.action.project.ProjectManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Context {
    private final EnquiriesManager enqMan;
    private final ApplicationManager appMan;
    private final ProjectManager proMan;
    private final user usr;
    private String currentViewedProjectID;
    private String currentViewedEnquiryID;

    public Context(user currentUser) {
        usr = currentUser;
        enqMan = new EnquiriesManager();
        appMan = new ApplicationManager();
        proMan = new ProjectManager();
    }

    public void endContext() {
        enqMan.store();
        appMan.store();
        proMan.store();
    }

    public List<String> act(String action, Scanner sc) {
        List<String> output = new ArrayList<>(List.of(""));
        List<String> input = new ArrayList<>(List.of(""));
        switch (action){
            //enquiry methods
            //todo: add a view enquiries option. projects store list of enquiry ids, use that to look up and list enquiries
            //if no current project, show the user's submitted enquiries (user also stores a list of enquiry ids that are owned by them, as well as applications that are owned by them
            case "view-enquiries":
                if (currentViewedProjectID.isEmpty()) {
                    output = enqMan.getEnquiriesByUser(usr);
                } else {
                    output = enqMan.getEnquiriesByProject(usr, currentViewedProjectID);
                }
                currentViewedEnquiryID = "";
                return output;
            case "view-enquiry":
                System.out.println("Enter enquiry ID: ");
                currentViewedEnquiryID = sc.nextLine();
                output = enqMan.getEnquiriesById(usr, currentViewedEnquiryID);
                return output;
            case "submit-enquiry":
                //take user input of project id and enquiry text.
                //return full enquiry details
                if (currentViewedProjectID.isEmpty()) {
                    System.out.println("Enter project ID: ");
                    currentViewedProjectID = sc.nextLine();
                }
                System.out.println("Enter enquiry: ");
                input.set(0, sc.nextLine());

                output = enqMan.submitEnquiry(usr, input.get(0), currentViewedProjectID);
                currentViewedEnquiryID = "";
                return output;
            case "delete-enquiry":
                //user input enquiry id
                //return success/failure and new project enquiries list
                System.out.println("Enter enquiry ID to confirm deletion: ");
                input.set(0, sc.nextLine());
                output = enqMan.deleteEnquiries(usr, input.get(0));
                currentViewedEnquiryID = "";
                return output;
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
                System.out.println("Enter edited enquiry: ");
                input.set(0, sc.nextLine());
                output = enqMan.editEnquiries(usr, input.get(0), currentViewedEnquiryID);
                return output;
            case "reply-enquiry":
                //user input enquiry id and reply text
                //return full enquiry details
                if (currentViewedEnquiryID.isEmpty()) {
                    System.out.println("Enter enquiry ID to edit: ");
                    currentViewedEnquiryID = sc.nextLine();
                }
                System.out.println("Enter reply to enquiry: ");
                input.set(0, sc.nextLine());
                output = enqMan.replyEnquiries(usr, input.get(0), currentViewedEnquiryID);
                return output;
            //application methods
            case "view-applications":
                //user input project id to view applications for project, or none to view own application
                System.out.println("Enter project ID to view applications (Blank to view own applications): ");
                input.set(0, sc.nextLine());
                if (!input.get(0).isEmpty()) {
                    output = appMan.listByProject(usr, input.get(0));
                } else {
                    output = appMan.listByUser(usr);
                }
                return output;
            case "add-application":
                //??
                break;
            case "retrieve-application":
                System.out.println("Enter an application ID to view: ");
                input.set(0, sc.nextLine());
                //appMan.retrieveApplication(usr,input[0]);
                //??
                break;
            case "withdraw-application":
                //input application id, sends withdrawal for current application view
                //return withdrawal status
                System.out.println("Enter application ID to confirm withdrawal request: ");
                input.set(0, sc.nextLine());
                //appMan.withdrawApplication(usr, input[0]);
                break;
            case "approve-application":
                //input application id, changes application status
                //return new application status
                System.out.println("Enter application ID to confirm approval: ");
                input.set(0, sc.nextLine());
                //appMan.approveApplication(usr, input[0]);
                break;
            case "reject-application":
                //input application id, changes application status
                //return new application status
                System.out.println("Enter application ID to confirm rejection: ");
                input.set(0, sc.nextLine());
                //appMan.rejectApplication(usr, input[0]);
                break;
            //project methods
            case "view-all-projects":
                //returns list of visible projects based on user
                output = proMan.getProjectList(usr);
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                if (output.isEmpty()) {
                    output.add("No projects found");
                }
                return output;
            case "view-project":
                if (currentViewedProjectID.isEmpty()) {
                    System.out.println("Enter exact project name to view: ");
                    input.set(0, sc.nextLine());
                } else {
                    input.set(0, currentViewedProjectID);
                }
                output = proMan.getProjectByName(usr, input.get(0), enqMan);
                currentViewedProjectID = input.get(0);
                if (output.isEmpty()) {
                    output.add("No projects found");
                }
                return output;
            case "search-projects":
                //input search term, output projects by keyword
                System.out.println("Enter search term: ");
                input.set(0, sc.nextLine());
                output = proMan.searchName(usr, input.get(0));
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                return output;
            case "filter-projects":
                System.out.println("Filter by [flat type] or [neighbourhood]?");
                input.set(0, sc.nextLine());
                switch (input.get(0)) {
                    case "flat type":
                        System.out.println("Enter flat type to filter by: ");
                        input.set(0, sc.nextLine());
                        output = proMan.filterFlat(usr, input.get(0));
                        break;
                    case "neighbourhood":
                        System.out.println("Enter neighbourhood to filter by: ");
                        output = proMan.filterNeighbourhood(usr, input.get(0));
                        break;
                    default:
                        output = new ArrayList<>(List.of(""));
                }
                //input category, output projects by category
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                return output;
            case "delete-project":
                //input project id, output success/failure and new projects list
                System.out.println("Enter project ID to be deleted: ");
                input.set(0, sc.nextLine());
                proMan.deleteProject(usr, input.get(0));
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                output = proMan.getProjectList(usr);
                return output;
            case "hide-show-project":
                //input project id, output success/failure and new projects list
                System.out.println("Enter project ID to toggle visibility: ");
                input.set(0, sc.nextLine());
                proMan.toggleVisibility(usr, input.get(0));
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                output = proMan.getProjectList(usr);
                return output;
            case "edit-project":
                //input project id, output success/failure and new projects list
                System.out.println("Enter project ID to edit: ");
                input.set(0, sc.nextLine());
                if (!proMan.getProjectByName(usr, input.get(0), enqMan).isEmpty()) {
                    System.out.println("Enter new project ID (Blank to skip): ");
                    input.set(1, sc.nextLine());
                    System.out.println("Enter new neighbourhood (Blank to skip): ");
                    input.set(2, sc.nextLine());
                    System.out.println("Enter flat type 1 (Blank to skip): ");
                    input.set(3, strIn(sc, List.of("2-Room","3-Room","")));
                    System.out.println("Enter flat type 1 amount (0 to skip): ");
                    input.set(4, numberIn(sc, -1, -1));
                    System.out.println("Enter flat type 1 price (0 to skip): ");
                    input.set(5, numberIn(sc, -1, -1));
                    System.out.println("Enter flat type 2 (Blank to skip, None to set no 2nd type): ");
                    input.set(6, strIn(sc, List.of("2-Room","3-Room","None","")));
                    System.out.println("Enter flat type 2 amount (0 to skip): ");
                    input.set(7, numberIn(sc, -1, -1));
                    System.out.println("Enter flat type 2 price (0 to skip): ");
                    input.set(8, numberIn(sc, -1, -1));
                    System.out.println("Enter new opening date (Blank to skip): ");
                    input.set(10, dateIn(sc, "", true));
                    System.out.println("Enter new closing date (Blank to skip): ");
                    input.set(10, dateIn(sc, input.get(9), true));
                    System.out.println("Enter amt of Officer slots (0 to skip): ");
                    input.set(11, numberIn(sc, -1, -1));

                    proMan.editProject(usr,input.get(0),input.get(1),input.get(2),input.get(3),Integer.getInteger(input.get(4)),Integer.getInteger(input.get(5)),input.get(6),Integer.getInteger(input.get(7)),Integer.getInteger(input.get(8)),input.get(9),input.get(10),Integer.getInteger(input.get(11)));
                }

                proMan.toggleVisibility(usr, input.get(0));
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                output = proMan.getProjectList(usr);
                return output;
            case "create-project":
                //input project id, output success/failure and new projects list
                if (!proMan.getProjectByName(usr, input.get(0), enqMan).isEmpty()) {
                    System.out.println("Enter new project ID: ");
                    input.set(1, sc.nextLine());
                    System.out.println("Enter neighbourhood: ");
                    input.set(2, sc.nextLine());
                    System.out.println("Enter flat type 1: ");
                    input.set(3, strIn(sc, List.of("2-Room","3-Room")));
                    System.out.println("Enter flat type 1 amount: ");
                    input.set(4, numberIn(sc, 1, -1));
                    System.out.println("Enter flat type 1 price: ");
                    input.set(5, numberIn(sc, 1, -1));
                    System.out.println("Enter flat type 2 (\"None\" to set no 2nd type): ");
                    input.set(6, strIn(sc, List.of("2-Room","3-Room","None")));
                    System.out.println("Enter flat type 2 amount: ");
                    input.set(7, numberIn(sc, 0, -1));
                    System.out.println("Enter flat type 2 price: ");
                    input.set(8, numberIn(sc, 0, -1));
                    System.out.println("Enter new opening date: ");
                    input.set(9, dateIn(sc, "", false));
                    System.out.println("Enter new closing date: ");
                    input.set(10, dateIn(sc, input.get(9), false));
                    System.out.println("Enter amt of Officer slots: ");
                    input.set(11, numberIn(sc, 1, -1));

                    proMan.createProject(usr,input.get(0),input.get(1),input.get(2),Integer.getInteger(input.get(3)),Integer.getInteger(input.get(4)),input.get(5),Integer.getInteger(input.get(6)),Integer.getInteger(input.get(7)),input.get(8),input.get(9),input.get(10),Integer.getInteger(input.get(11)));
                }

                proMan.toggleVisibility(usr, input.get(0));
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                output = proMan.getProjectList(usr);
                return output;
            default:
                System.out.printf("Invalid action [%s]\n", action);
                return output;
        }
        return output;
    }

    private String numberIn(Scanner sc, int min, int max) {
        int input;
        do {
            input = sc.nextInt();
        } while ((min != -1 && input < min)  || (max != -1 && input > max));
        sc.nextLine(); //clear newlines
        return String.valueOf(input);
    }

    private String strIn(Scanner sc, List<String> options) {
        String input;
        System.out.println("Options:");
        for (String i : options) {
            System.out.printf("%s\n", i);
        }
        do {
            input = sc.nextLine();
        } while (!options.contains(input));
        return input;
    }

    private String dateIn(Scanner sc, String minDate, boolean allowBlank) {
        String input;
        LocalDate inDate;
        boolean done = false;
        do {
            input = sc.nextLine();
            if (allowBlank && input.isEmpty()) {
                done = true;
                continue;
            }

            try {
                inDate = LocalDate.parse(input);
                if (minDate.isEmpty()) {
                    done = true;
                } else {
                    if (inDate.isAfter(LocalDate.parse(minDate))) {
                        done = true;
                    } else {
                        System.out.printf("Inputted date %s cannot be before %s\n", input, minDate);
                    }
                }
            } catch (DateTimeParseException e) {
                System.out.printf("%s is an invalid date\n", input);
            }
        } while (!done);
        return input;
    }
    public user getCurrentUser() {
        return usr;
    }
}