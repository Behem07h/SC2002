/**
 * Manages projects within the system, handling creation, storage, retrieval, and filtering.
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23
 */
package org.action.project;

import org.UI.ConfigLDR;
import org.Users.HDBManager.HDBManager;
import org.Users.HDBOfficer.HDBOfficer;
import org.Users.user;
import org.action.enquiry.EnquiriesManager;



import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

import static java.lang.Integer.max;

/**
 * This class provides functionality for managing HDB projects, including creating, editing, and deleting projects,
 * searching and filtering projects based on various criteria, checking project eligibility for users,
 * and storing/loading project data from persistent storage. It serves as the central management point
 * for all project-related operations in the system.
 */
public class ProjectManager {
    /**
     * List containing all projects in the system.
     */
    private final List<Project> projectList;
    /**
     * The directory path where database files are stored.
     */
    private final String path = "data/db";
    /**
     * The filename for project data storage.
     */
    private final String filename = "/project.csv";
    
    //private RegistrationManager registrationManager;
    /**
     * Constructs a new ProjectManager and loads existing projects from persistent storage.
     * Projects are loaded from a CSV file and added to the project list.
     * If a project entry is missing required parameters, it is skipped with a warning message.
     */
    public ProjectManager() {
        projectList = new ArrayList<>();

        ConfigLDR ldr = new ConfigLDR();
        Map<String,String[]> pro_map = ldr.ReadToArrMap(path + filename);
        for (String key : pro_map.keySet()) {
            String[] items = pro_map.get(key);
            if (items.length < 17) {
                System.out.println("Project ID " + key + " missing params");
                continue;
            } //if param length too short, skip

            String neighbourhood = items[0];
            String type1 = items[1];
            int type1_count = Integer.parseInt(items[2]);
            int type1_price = Integer.parseInt(items[3]);
            String type2 = items[4];
            int type2_count = Integer.parseInt(items[5]);
            int type2_price = Integer.parseInt(items[6]);
            LocalDate opening_date = LocalDate.parse(items[7]);
            LocalDate closing_date = LocalDate.parse(items[8]);
            String manager = items[9];
            String managerID = items[10];
            int officer_slots = Integer.parseInt(items[11]);
            String officers = items[12];
            String officersID = items[13];
            String flatType1Bookings = items[14];
            String flatType2Bookings = items[15];
            boolean visible = Boolean.parseBoolean(items[16]);

             this.projectList.add(new Project(key, neighbourhood, type1, type1_count, type1_price, type2, type2_count, type2_price, opening_date, closing_date, manager, managerID, officer_slots, officers, officersID, flatType1Bookings, flatType2Bookings, visible));
        }

        //registrationManager = new RegistrationManager();
    }
    /**
     * Stores all project data to the CSV file.
     * This method should be called when quitting the program to save all project changes.
     */
    public void store() {
        // run this when quitting program to store to csv
        Map<String,String[]> pro_map = new HashMap<>();
        for (Project p : projectList) {
            String[] items = {p.getNeighbourhood(),p.getFlatType1(), String.valueOf(p.getFlatCount1()), String.valueOf(p.getFlatPrice1()),p.getFlatType2(), String.valueOf(p.getFlatCount2()), String.valueOf(p.getFlatPrice2()), String.valueOf(p.getOpeningDate()), String.valueOf(p.getClosingDate()),p.getManagerName(),p.getManagerId(), String.valueOf(p.getOfficerSlotCount()),p.getOfficersList(),p.getOfficersIDList(), p.getFlatType1Bookings(), p.getFlatType2Bookings(), String.valueOf(p.isVisible())};
            pro_map.put(String.valueOf(p.getProjectName()),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(path + filename,pro_map);
    }

    /**
     * Creates a new project with the specified parameters.
     * Only users with HDBManager role can create projects.
     * Validates that no project with the same name exists and that the manager
     * is not already managing a project with overlapping application period.
     *
     * @param usr The user attempting to create the project
     * @param projectName The unique name for the new project
     * @param neighbourhood The neighbourhood where the project is located
     * @param type1 The first flat type offered in the project
     * @param type1_count The number of units available for the first flat type
     * @param type1_price The price of the first flat type
     * @param type2 The second flat type offered in the project
     * @param type2_count The number of units available for the second flat type
     * @param type2_price The price of the second flat type
     * @param opening_date The project's opening date (format: YYYY-MM-DD)
     * @param closing_date The project's closing date (format: YYYY-MM-DD)
     * @param officer_slots The number of officer slots available for this project
     */
    public void createProject(user usr, String projectName, String neighbourhood, String type1, int type1_count, int type1_price, String type2, int type2_count, int type2_price, String opening_date, String closing_date, int officer_slots) {
        if (usr instanceof HDBManager) {
            boolean exists = false;
            for (Project p : projectList) {
                if (p.filter("", projectName,"","", null,false, false)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                System.out.println("Cannot create project with identical name");
            } else {
                try {
                    LocalDate start_date = LocalDate.parse(opening_date);
                    LocalDate end_date = LocalDate.parse(closing_date);
                    for (Project p : projectList) {
                        if (p.filter("", "","","", usr,false, false) && p.overlapCheck(start_date, end_date)) {
                            System.out.println("You are already managing a project that overlaps this application period: " + p.getProjectName());
                            return;
                        }
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date. Dates must be in format YYYY-MM-DD");
                    return;
                }
                projectList.add(new Project(projectName, neighbourhood, type1, type1_count, type1_price, type2, type2_count, type2_price, LocalDate.parse(opening_date), LocalDate.parse(closing_date), usr.getUsername(), usr.getUserID(), officer_slots, "", "","","",false));
                System.out.println("New project created: " + projectName);
            }
        } else {
            System.out.println("No perms to create project");
        }
    }

    /**
     * Deletes a project from the system.
     * Only users with HDBManager role who manage the project can delete it.
     *
     * @param usr The user attempting to delete the project
     * @param projectName The name of the project to delete
     */
    public void deleteProject(user usr, String projectName) {
        if (usr instanceof HDBManager) {
            boolean removed = projectList.removeIf(p -> p.filter("",projectName,"","",usr,false,false));
            if (removed) {
                System.out.println("Project '" + projectName + "' deleted from the system.");
            } else {
                System.out.println("No matching project found with name: " + projectName);
            }
        } else {
            System.out.println("No perms to edit project");
        }
    }
    /**
     * Edits an existing project with updated parameters.
     * Only users with HDBManager role who manage the project can edit it.
     * Ensures flat counts don't decrease below current bookings.
     *
     * @param usr The user attempting to edit the project
     * @param projectNameOld The current name of the project
     * @param projectName The new name for the project
     * @param neighbourhood The updated neighbourhood
     * @param flatType1 The updated first flat type
     * @param flatCount1 The updated number of units for first flat type
     * @param flatPrice1 The updated price for first flat type
     * @param flatType2 The updated second flat type
     * @param flatCount2 The updated number of units for second flat type
     * @param flatPrice2 The updated price for second flat type
     * @param openingDate The updated opening date
     * @param closingDate The updated closing date
     * @param officerSlots The updated number of officer slots
     */
    public void editProject(user usr, String projectNameOld, String projectName, String neighbourhood, String flatType1, int flatCount1, int flatPrice1, String flatType2, int flatCount2, int flatPrice2, String openingDate, String closingDate, int officerSlots) {
        if (usr instanceof HDBManager) {
            for (Project p : projectList) {
                if (p.filter("", projectNameOld,"","",usr,false,false)) {
                    p.edit(projectName, neighbourhood, flatType1, max(p.getBookingCount1(),flatCount1), flatPrice1, flatType2, max(p.getBookingCount2(),flatCount2), flatPrice2, openingDate, closingDate, officerSlots);
                    System.out.println("Project updated for keyword: " + projectName);
                    return;
                }
            }
            System.out.println("No project found for update with keyword: " + projectName);
        } else {
            System.out.println("No perms to edit project");
        }
    }

    /**
     * Toggles the visibility of a project.
     * Only users with HDBManager role who manage the project can change its visibility.
     *
     * @param usr The user attempting to toggle project visibility
     * @param keyword The name of the project to toggle visibility for
     */
    public void toggleVisibility(user usr, String keyword) {
        if (usr instanceof HDBManager) {
            for (Project p : projectList) {
                if (p.filter("",keyword,"","",null,false,false)) {
                    if (p.managerOfficerOf(usr)) {
                        p.toggle_visibility();
                    } else {
                        System.out.println("You are not the manager of this project");
                    }
                    return;
                }
            }
            System.out.println("No project found to toggle for keyword: " + keyword);
        } else {
            System.out.println("No perms to alter project visibility");
        }
    }

    /**
     * Filters projects based on specified criteria.
     *
     * @param usr The user performing the search
     * @param name Partial project name to search for
     * @param nameExact Exact project name to match
     * @param neighbourhood Neighbourhood to filter by
     * @param flat Flat type to filter by
     * @param managedBy Whether to filter by projects managed by the user
     * @param visChk Whether to check project visibility
     * @return List of projects matching the filter criteria
     */
    private List<Project> searchFilter(user usr, String name, String nameExact, String neighbourhood, String flat, boolean managedBy, boolean visChk) {
        List<Project> out = new ArrayList<>();
        boolean visible_check = visChk; //default to true so new roles default to minimum perms
        boolean date_check = true;
        if (usr instanceof HDBManager) {
            visible_check = false;
            date_check = false;
        }

        for (Project p : projectList) {
            if (p.filter(name,nameExact,neighbourhood,flat, (managedBy ? usr : null), date_check, visible_check)) {
                out.add(p);
            }
        }
        return out;
    }

    /**
     * Converts a list of projects to formatted strings.
     *
     * @param usr The user requesting the project information
     * @param projects The list of projects to convert
     * @return List containing a formatted string representation of the projects
     */
    public List<String> projectsToString(user usr, List<Project> projects) {
        List<String> out = new ArrayList<>(List.of(""));
        for (Project p : projects) {
            out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
        }
        return out;
    }

    /**
     * Merges two lists of projects, removing duplicates.
     *
     * @param p1 First list of projects
     * @param p2 Second list of projects
     * @return A merged list with no duplicate projects
     */
    public List<Project> mergeProjects(List<Project> p1, List<Project> p2) {
        Set<Project> set = new HashSet<>(p1);
        set.addAll(p2);
        return new ArrayList<>(set);
    }

    /**
     * Filters projects related to the user (projects the user manages or is assigned to).
     *
     * @param usr The user to find related projects for
     * @return List of projects related to the user
     */
    public List<Project> filterRelated(user usr) {
        List<Project> filteredProjects;
        filteredProjects = searchFilter(usr,"","","", "",true,false);
        return filteredProjects;
    }


    /**
     * Filters projects by flat type.
     *
     * @param usr The user requesting the filtering
     * @param flatType The flat type to filter by
     * @return List of projects matching the flat type filter
     */
    public List<Project> filterFlat(user usr, String flatType) {
        List<Project> filteredProjects = new ArrayList<>();
        if (flatType.contains(getUserValidFlatTypes(usr))) {
            filteredProjects = searchFilter(usr,"","","", flatType,false,true);
        }
        return filteredProjects;
    }

    /**
     * Filters projects by neighbourhood and returns formatted string representations.
     *
     * @param usr The user requesting the filtering
     * @param neighbourhood The neighbourhood to filter by
     * @return List containing a formatted string of projects in the specified neighbourhood
     */
    public List<String> filterNeighbourhood(user usr, String neighbourhood) {
        List<Project> filteredProjects;
        List<String> out = new ArrayList<>(List.of(""));
        filteredProjects = searchFilter(usr,"","",neighbourhood,getUserValidFlatTypes(usr),false,true);
        for (Project p : filteredProjects) {
            out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
        }
        return out;
    }

    /**
     * Searches for projects by name and returns formatted string representations.
     *
     * @param usr The user performing the search
     * @param name The project name to search for
     * @return List containing a formatted string of projects matching the name search
     */
    public List<String> searchName(user usr, String name) {
        List<Project> filteredProjects;
        List<String> out = new ArrayList<>(List.of(""));
        filteredProjects = searchFilter(usr, name,"","",getUserValidFlatTypes(usr),false,true);
        for (Project p : filteredProjects) {
            out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
        }
        return out;
    }

    /**
     * Checks if a project with the specified name exists.
     *
     * @param usr The user performing the check
     * @param projectName The project name to check
     * @param visChk Whether to check project visibility
     * @return Number of matching projects found (typically 0 or 1)
     */
    public int projectExists(user usr, String projectName, boolean visChk) {
        List<Project> filteredProjects;
        filteredProjects = searchFilter(usr,"",projectName,"", getUserValidFlatTypes(usr),false, visChk);
        return filteredProjects.size();
    }

    /**
     * Checks if the user is a manager or officer of the specified project.
     *
     * @param usr The user to check
     * @param projectName The project name to check
     * @param visChk Whether to check project visibility
     * @param manager True to check if user is a manager, false to check if user is an officer
     * @return True if the user is a manager/officer of the project, false otherwise
     */
    public boolean checkManagedOfficerOf(user usr, String projectName, boolean visChk, boolean manager) {
        if (!((manager && usr instanceof HDBManager) || (!manager && usr instanceof HDBOfficer))) {
            return false;
        }
        if (projectName == null) {
            return true;
        }
        Project pro = getProjectObjByName(usr, projectName, visChk);
        if (pro != null) {
            return pro.managerOfficerOf(usr);
        } else {
            return false;
        }
    }

    /**
     * Retrieves a project object by its name.
     *
     * @param usr The user requesting the project
     * @param projectName The name of the project to retrieve
     * @param visChk Whether to check project visibility
     * @return The Project object if found, null otherwise
     */
    public Project getProjectObjByName(user usr, String projectName, boolean visChk) {
        List<Project> filteredProjects;
        filteredProjects = searchFilter(usr,"",projectName,"", getUserValidFlatTypes(usr), false,visChk);
        if (!filteredProjects.isEmpty()) {
            return filteredProjects.get(0);
        } else {
            return null;
        }
    }
    /**
     * Retrieves detailed information about a project by its name.
     *
     * @param usr The user requesting the project details
     * @param projectName The name of the project to retrieve details for
     * @param enqMan The EnquiriesManager for handling project-related enquiries
     * @param visChk Whether to check project visibility
     * @return List containing a formatted string with detailed project information
     */
    public List<String> getProjectByName(user usr, String projectName, EnquiriesManager enqMan, boolean visChk) {
        List<String> out = new ArrayList<>(List.of(""));
        Project p = getProjectObjByName(usr, projectName, visChk);
        if (p != null) {
            out.set(0, "\n" + p.viewFull(usr, getUserValidFlatTypes(usr), enqMan));
        }
        return out;
    }

    /**
     * Determines which flat types a user is eligible for based on age and marital status.
     *
     * @param usr The user to check eligibility for
     * @return String representing the valid flat types for the user
     */
    private String getUserValidFlatTypes(user usr) {
        if (usr instanceof HDBOfficer || usr instanceof HDBManager) {
            return "Room";
        } else {
            if (Objects.equals(usr.getMaritalStatus(), "Single") && usr.getAge() >= 35) {
                return "2-Room";
            } else if (Objects.equals(usr.getMaritalStatus(), "Married") && usr.getAge() >= 21) {
                return "Room"; //returns any flat type
            }
        }
        return "NONE"; //if you fall outside those ranges, you cannot see anything according to SG law
    }

    /**
     * Gets the list of valid filter options available to a user based on their role.
     *
     * @param usr The user to get filter options for
     * @return List of filter options available to the user
     */
    public List<String> getValidFilters(user usr) {
        if (usr instanceof HDBOfficer || usr instanceof HDBManager) {
            return List.of("Flat", "Neighbourhood", "My Projects","Reset Filter");
        } else {
            return List.of("Flat", "Neighbourhood","Reset Filter");
        }
    }

    /**
     * Gets the flat options available to a user for a specific project or all projects.
     *
     * @param usr The user to get flat options for
     * @param projectName The project name to check options for, or empty string for all projects
     * @return List of flat types available to the user
     */
    public List<String> userFlatOptions(user usr, String projectName) {
        List<String> flatChoices;
        List<String> outputChoices = new ArrayList<>();
        if (usr instanceof HDBOfficer || usr instanceof HDBManager) {
            flatChoices = List.of("2-Room","3-Room");
        } else {
            if (Objects.equals(usr.getMaritalStatus(), "Single") && usr.getAge() >= 35) {
                flatChoices = List.of("2-Room");
            } else if (Objects.equals(usr.getMaritalStatus(), "Married") && usr.getAge() >= 21) {
                flatChoices = List.of("2-Room","3-Room"); //returns any flat type
            } else {
                flatChoices = List.of("NONE"); //if you fall outside those ranges, you cannot see anything according to SG law
            }
        }

        if (projectName.isEmpty()) {
            for (String flatStr : flatChoices) {
                if (!searchFilter(usr, "", projectName, "", flatStr, false,true).isEmpty()) {
                    outputChoices.add(flatStr);
                }
            }
            if (outputChoices.isEmpty()) {outputChoices.add("NONE");}
            return outputChoices;
        } else {
            return flatChoices;
        }


    }


    /**
     * Gets a list of all projects the user can view based on their eligibility.
     *
     * @param usr The user requesting the project list
     * @return List containing a formatted string of viewable projects
     */
    public List<String> getProjectList(user usr) {
        return projectsToString(usr, mergeProjects(filterFlat(usr, getUserValidFlatTypes(usr)), filterRelated(usr)));
    }
} 

