package org.UI;

import org.Users.user;
import org.action.ApplicationManager;
import org.action.enquiry.EnquiriesManager;
import org.action.project.Project;
import org.action.project.ProjectManager;
import org.action.registration.RegistrationManager;
import org.receipt.BookingReceipt;
import org.Users.Applicant.ApplicantManager;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Context {
    private final EnquiriesManager enqMan;
    private final ApplicationManager appMan;
    private final ProjectManager proMan;
    private final RegistrationManager regMan;
    private final user usr;
    private final  ApplicantManager applicantMan;
    private String currentViewedProjectID;
    private String currentViewedEnquiryID;

    public Context(user currentUser) {
        usr = currentUser;
        enqMan = new EnquiriesManager();
        appMan = new ApplicationManager();
        proMan = new ProjectManager();
        regMan = new RegistrationManager();
        applicantMan = new ApplicantManager();
    }

    public void endContext() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Saving Enquiries");
        enqMan.store();
        System.out.println("Saving Applications");
        appMan.store();
        System.out.println("Saving Projects");
        proMan.store();
    }

    public List<String> act(String action, Scanner sc) {
        List<String> output = new ArrayList<>(List.of("",""));
        List<String> input = new ArrayList<>(List.of("","","","","","","","","","","",""));
        //need as many entries in the initial array as we have expected inputs
        switch (action){
            //enquiry methods
            //if no current project, show the user's submitted enquiries (user also stores a list of enquiry ids that are owned by them, as well as applications that are owned by them
            case "view-enquiries":
                if (currentViewedProjectID.isEmpty()) {
                    output = enqMan.getEnquiriesByUser(usr);
                } else {
                    output = enqMan.getEnquiriesByProject(usr, currentViewedProjectID);
                }
                currentViewedEnquiryID = "";
                if (output.get(0).isEmpty()) {
                    System.out.println("No enquiries found");
                }
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
                    System.out.println("Enter enquiry ID to reply to: ");
                    currentViewedEnquiryID = sc.nextLine();
                }
                if (enqMan.canReply(usr, currentViewedEnquiryID, proMan)) {
                    System.out.println("Enter reply to enquiry: ");
                    input.set(0, sc.nextLine());
                    output = enqMan.replyEnquiries(usr, input.get(0), currentViewedEnquiryID);
                } else {
                    output = List.of("");
                }


                return output;
            //application methods
            case "view-applications":
                //user input project id to view applications for project, or none to view own application
                System.out.println("Enter project name to view applications (Blank to view own applications): ");
                input.set(0, sc.nextLine());
                if (!input.get(0).isEmpty()) {
                    output = appMan.listByProject(usr, input.get(0));
                } else {
                    output = appMan.listByUser(usr, enqMan, proMan);
                }
                if (output.get(0).isEmpty()) {
                    System.out.println("No applications found");
                }
                return output;
            case "add-application":
                if (currentViewedProjectID.isEmpty()) {
                    System.out.println("Enter project name to apply for: ");
                    currentViewedProjectID = sc.nextLine();
                }
                System.out.println("Enter chosen flat type: ");
                input.set(0, strIn(sc, proMan.userFlatOptions(usr, currentViewedProjectID)));
                appMan.newApplication(usr, currentViewedProjectID, input.get(0), proMan);
                output = appMan.listByUser(usr, enqMan, proMan);
                return output;
            case "retrieve-application":
                System.out.println("Enter an application ID to view: ");
                input.set(0, sc.nextLine());
                //appMan.retrieveApplication(usr,input[0]);
                //??
                return List.of("");
            case "withdraw-application":
                //input application id, sends withdrawal for current application view
                //return withdrawal status
                System.out.println("Enter application ID to confirm withdrawal request: ");
                input.set(0, sc.nextLine());
                appMan.requestWithdrawal(usr, input.get(0));
                return List.of("");
            case "process-application":
                //input application id, changes application status
                //return new application status
                System.out.println("Enter application ID to update status: ");
                input.set(0, sc.nextLine());
                System.out.println("Enter new application status (CANCEL to exit): ");
                input.set(1, strIn(sc, List.of("SUCCESSFUL","UNSUCCESSFUL","CANCEL")));
                if (!Objects.equals(input.get(1), "CANCEL")) {
                    appMan.processApplication(usr, input.get(0), input.get(1), proMan);
                }
                return List.of("");
            case "process-withdrawal":
                //input application id, changes application status
                //return new application status
                System.out.println("Enter application ID to update withdrawal status: ");
                input.set(0, sc.nextLine());
                System.out.println("Enter new application status (CANCEL to exit): ");
                input.set(1, strIn(sc, List.of("WITHDRAW","REJECT","CANCEL")));
                if (!Objects.equals(input.get(1), "CANCEL")) {
                    appMan.processWithdrawal(usr, input.get(0), input.get(1), proMan);
                }
                return List.of("");
            //todo: add registration methods, which is just the application class but again
            //todo: officers register for BTO, view their registered BTOs by viewing the associated registrations
            //todo: filterable by visibility
            case "list-registrations":
                //todo: managers need a way to see all pending registrations for a project
                //todo: officers need a way to see their own pending registrations
                System.out.println("Enter project name to view pending registration: ");
                input.set(0, sc.nextLine());
                regMan.listPendingReg(usr, input.get(0));
                return List.of("");
            case "add-registration":
                //todo:officers register to manage project
                //todo:check the project application period does not overlap with other ones they are managing
                //todo:check that they have no pending or successful applications for the project
                System.out.println("Enter project name you want to register for: ");
                input.set(0, sc.nextLine());
                regMan.registerProject(usr, input.get(0), proMan);
                return List.of("");
            case "process-registration":
                //todo:managers accept or reject registrations
                System.out.println("Enter registration ID to update status: ");
                input.set(0, sc.nextLine());
                System.out.println("Enter new registration status (CANCEL to exit): ");
                input.set(1, strIn(sc, List.of("APPROVED","REJECTED","CANCEL")));
                if (!Objects.equals(input.get(1), "CANCEL")) {
                    regMan.processRegistration(usr, input.get(0), input.get(1), proMan);
                }
                return List.of("");
                case "bookings-receipt":
                // 1) Project filter (blank = all)
                System.out.println("Enter project ID (blank = all your projects):");
                String projectFilter  = sc.nextLine().trim();
            
                // 2) Flat‑type filter
                System.out.println("Filter by flat type (blank = any):");
                String flatTypeFilter = sc.nextLine().trim();
            
                // 3) Marital‑status filter
                System.out.println("Filter by marital status (blank = any):");
                String maritalFilter  = sc.nextLine().trim();
            
                // 4) Fetch and print receipts
                List<BookingReceipt> receipts = appMan.getBookingReceipts(
                    usr,
                    projectFilter,
                    flatTypeFilter,
                    maritalFilter,
                    proMan,
                    id -> applicantMan.findById(id)
                    );
            
                if (receipts.isEmpty()) {
                    System.out.println("No bookings match your criteria.");
                } else {
                    for (BookingReceipt r : receipts) {
                        r.generateReceipt();
                        System.out.println();
                    }
                }
                return List.of("");
            

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
                    input.set(0, strInNoBlank(sc));
                } else {
                    input.set(0, currentViewedProjectID);
                }
                output = proMan.getProjectByName(usr, input.get(0), enqMan, true);
                currentViewedProjectID = input.get(0);
                return output;
            case "search-projects":
                //input search term, output projects by keyword
                System.out.println("Enter search term: ");
                input.set(0, sc.nextLine());
                output = proMan.searchName(usr, input.get(0));
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                if (output.get(0).isEmpty()) {
                    output.add("No projects found");
                }
                return output;
                case "filter-projects": {
                    System.out.println("Filter by:");
                    // Build the menu
                    List<String> options = new ArrayList<>(List.of(
                        "Flat", "Neighbourhood", "My Projects", "Reset Filter"
                    ));
                    String choice = strIn(sc, options);
                
                    switch (choice) {
                        case "Flat":
                            System.out.println("Enter flat type to filter by:");
                            String flat = strIn(sc, proMan.userFlatOptions(usr, ""));
                            output = proMan.filterFlat(usr, flat);
                            break;
                
                        case "Neighbourhood":
                            System.out.println("Enter neighbourhood to filter by:");
                            String hood = sc.nextLine();
                            output = proMan.filterNeighbourhood(usr, hood);
                            break;
                
                        case "My Projects":
                            // getProjectList() now returns exactly the right IDs for managers, officers or applicants
                            output = proMan.getProjectList(usr);
                            break;
                        
                
                        case "Reset Filter":
                            output = proMan.getProjectList(usr);
                            break;
                
                        default:
                            output = List.of("Invalid filter");
                    }
                
                    // clear any context
                    currentViewedProjectID = "";
                    currentViewedEnquiryID = "";
                
                    // ensure we always return at least one line
                    if (output.isEmpty() || output.get(0).isEmpty()) {
                        output = List.of("No projects found");
                    }
                    return output;
                }
            case "delete-project":
                //input project id, output success/failure and new projects list
                System.out.println("Enter project ID to be deleted: ");
                input.set(0, strInNoBlank(sc));
                proMan.deleteProject(usr, input.get(0));
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                output = proMan.getProjectList(usr);
                if (output.get(0).isEmpty()) {
                    output.add("No projects found");
                }
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
                if (!proMan.checkManagedOfficerOf(usr, input.get(0), true, true)) {
                    System.out.println("You cannot edit this project");
                    return List.of("");
                }
                if (!proMan.getProjectByName(usr, input.get(0), enqMan, true).isEmpty()) {
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

                    proMan.editProject(usr,input.get(0),input.get(1),input.get(2),input.get(3),Integer.parseInt(input.get(4)),Integer.parseInt(input.get(5)),input.get(6),Integer.parseInt(input.get(7)),Integer.parseInt(input.get(8)),input.get(9),input.get(10),Integer.parseInt(input.get(11)));
                }
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                output = proMan.getProjectList(usr);
                return output;
            case "create-project":
                //input project id, output success/failure and new projects list
                if (!proMan.checkManagedOfficerOf(usr, null, false, true)) {
                    System.out.println("You do not have the permissions to create projects");
                    return List.of("");
                }
                System.out.println("Enter new project ID: ");
                input.set(0, sc.nextLine());
                if (!proMan.getProjectByName(usr, input.get(0), enqMan, false).isEmpty()) {
                    System.out.println("Enter neighbourhood: ");
                    input.set(1, sc.nextLine());
                    System.out.println("Enter flat type 1: ");
                    input.set(2, strIn(sc, List.of("2-Room","3-Room")));
                    System.out.println("Enter flat type 1 amount: ");
                    input.set(3, numberIn(sc, 1, -1));
                    System.out.println("Enter flat type 1 price: ");
                    input.set(4, numberIn(sc, 1, -1));
                    System.out.println("Enter flat type 2 (\"None\" to set no 2nd type): ");
                    input.set(5, strIn(sc, List.of("2-Room","3-Room","None")));
                    System.out.println("Enter flat type 2 amount: ");
                    input.set(6, numberIn(sc, 0, -1));
                    System.out.println("Enter flat type 2 price: ");
                    input.set(7, numberIn(sc, 0, -1));
                    System.out.println("Enter new opening date: ");
                    input.set(8, dateIn(sc, "", false));
                    System.out.println("Enter new closing date: ");
                    input.set(9, dateIn(sc, input.get(9), false));
                    System.out.println("Enter amt of Officer slots: ");
                    input.set(10, numberIn(sc, 1, 10));

                    proMan.createProject(usr,input.get(0),input.get(1),input.get(2),Integer.parseInt(input.get(3)),Integer.parseInt(input.get(4)),input.get(5),Integer.parseInt(input.get(6)),Integer.parseInt(input.get(7)),input.get(8),input.get(9),Integer.parseInt(input.get(10)));
                }
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                output = proMan.getProjectList(usr);
                return output;
            default:
                System.out.printf("Invalid action [%s]\n", action);
                return output;
        }
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

    private String strInNoBlank(Scanner sc) {
        String input;
        do {
            input = sc.nextLine();
            if (input.isEmpty()) {
                System.out.println("Input cannot be blank");
            }
        } while (input.trim().isEmpty());
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