/**
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.UI;

import org.Users.GenericManager;
import org.Users.user;
import org.action.application.ApplicationManager;
import org.action.enquiry.EnquiriesManager;
import org.action.project.ProjectManager;
import org.action.registration.RegistrationManager;
import org.receipt.BookingReceipt;


import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

/**
 * Context class manages the interaction between user inputs, business logic, and various manager classes.
 * It serves as a controller that processes user actions and coordinates between different modules of the application.
 */
public class Context {
    private final EnquiriesManager enqMan;
    private final ApplicationManager appMan;
    private final ProjectManager proMan;
    private final RegistrationManager regMan;
    private final user usr;
    private final List<GenericManager<user>> managersList;
    private String currentViewedProjectID;
    private String currentViewedEnquiryID;

    /**
     * Constructs a new Context associated with the specified user.
     * Initializes all manager classes needed for handling different actions.
     *
     * @param currentUser the user for which this context is created
     * @param managersList List of the Managers
     */
    public Context(user currentUser, List<GenericManager<user>> managersList) {
        usr = currentUser;
        this.managersList = managersList;
        enqMan = new EnquiriesManager();
        appMan = new ApplicationManager();
        proMan = new ProjectManager();
        regMan = new RegistrationManager();
    }

    /**
     * Terminates the current context by saving all modified data.
     * Saves enquiries, applications, and projects to their respective storage locations.
     */
    public void endContext() {
        System.out.println("Saving Enquiries");
        enqMan.store();
        System.out.println("Saving Applications");
        appMan.store();
        System.out.println("Saving Projects");
        proMan.store();
        System.out.println("Saving Registrations");
        regMan.store();
    }

    /**
     * Processes a user action based on the action type and handles user input/output.
     * This method serves as the main controller for all user interactions with the system.
     *
     * @param action the action to be performed (e.g., "view-enquiries", "submit-enquiry", etc.)
     * @param sc the Scanner object for reading user input
     * @return a List of strings representing the output/response to be displayed to the user
     */
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
                input.set(1, strIn(sc, appMan.processApplicationOptions(input.get(0))));
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

            case "list-registrations":
                System.out.println("Enter project name to view pending registrations (Blank to view own applications): ");
                input.set(0, sc.nextLine());
                if (!input.get(0).isEmpty()) {
                    output = regMan.listPendingReg(usr, input.get(0));
                } else {
                    output = regMan.listPendingReg(usr);
                }
                return output;
            case "add-registration":
                System.out.println("Enter project name you want to register for: ");
                input.set(0, sc.nextLine());
                regMan.registerProject(usr, input.get(0), proMan, appMan);
                return List.of("");
            case "process-registration":
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
                    managersList
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
                List<String> options = proMan.getValidFilters(usr);
                input.set(1, strIn(sc, options));

                switch (input.get(1)) {
                    case "Flat":
                        System.out.println("Enter flat type to filter by:");
                        input.set(2, strIn(sc, proMan.userFlatOptions(usr, "")));
                        output = proMan.projectsToString(usr,proMan.filterFlat(usr, input.get(2)));
                        break;

                    case "Neighbourhood":
                        System.out.println("Enter neighbourhood to filter by:");
                        input.set(2, sc.nextLine());
                        output = proMan.filterNeighbourhood(usr, input.get(2));
                        break;

                    case "My Projects":
                        output = proMan.projectsToString(usr,proMan.filterRelated(usr));
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
                System.out.println("Enter project ID to confirm toggle visibility: ");
                input.set(0, sc.nextLine());
                proMan.toggleVisibility(usr, input.get(0));
                currentViewedProjectID = "";
                currentViewedEnquiryID = "";
                output = proMan.getProjectByName(usr, input.get(0), enqMan, true);
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

    /**
     * Reads an integer input from the user with optional range validation.
     *
     * @param sc the Scanner object for reading user input
     * @param min the minimum acceptable value (-1 to ignore minimum constraint)
     * @param max the maximum acceptable value (-1 to ignore maximum constraint)
     * @return a String representation of the validated integer input
     */
    private String numberIn(Scanner sc, int min, int max) {
        int input;
        do {
            input = sc.nextInt();
        } while ((min != -1 && input < min)  || (max != -1 && input > max));
        sc.nextLine(); //clear newlines
        return String.valueOf(input);
    }

    /**
     * Reads a string input from the user that must match one of the provided options.
     * Displays the list of valid options to the user before accepting input.
     *
     * @param sc the Scanner object for reading user input
     * @param options a List of strings representing valid input options
     * @return the validated string input that matches one of the provided options
     */
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

    /**
     * Reads a non-blank string input from the user.
     * Continues prompting until a non-empty input is provided.
     *
     * @param sc the Scanner object for reading user input
     * @return the validated non-blank string input
     */
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

    /**
     * Reads a date input from the user with optional constraints.
     * Validates that the input is a valid date and optionally checks that it is after a minimum date.
     *
     * @param sc the Scanner object for reading user input
     * @param minDate a string representing the minimum acceptable date (empty string to ignore)
     * @param allowBlank whether blank input is allowed
     * @return a string representation of the validated date
     */
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
    /**
     * Returns the current user associated with this context.
     *
     * @return the user object
     */
    public user getCurrentUser() {
        return usr;
    }
}