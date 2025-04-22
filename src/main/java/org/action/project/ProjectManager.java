package org.action.project;

import org.UI.ConfigLDR;
import org.Users.HDBManager.HDBManager;
import org.Users.HDBOfficer.HDBOfficer;
import org.Users.user;
import org.action.enquiry.EnquiriesManager;
import org.action.registration.Register;
import org.action.registration.RegistrationManager;
import org.action.project.Project;



import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Integer.max;

public class ProjectManager {
    private final List<Project> projectList;
    private final String path = "data/db";
    private final String filename = "/project.csv";
    
    //private RegistrationManager registrationManager;
    
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

    public List<String> projectsToString(user usr, List<Project> projects) {
        List<String> out = new ArrayList<>(List.of(""));
        for (Project p : projects) {
            out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
        }
        return out;
    }

    public List<Project> mergeProjects(List<Project> p1, List<Project> p2) {
        Set<Project> set = new HashSet<>(p1);
        set.addAll(p2);
        return new ArrayList<>(set);
    }

    public List<Project> filterRelated(user usr) {
        List<Project> filteredProjects;
        filteredProjects = searchFilter(usr,"","","", "",true,false);
        return filteredProjects;
    }

    public List<Project> filterFlat(user usr, String flatType) {
        List<Project> filteredProjects = new ArrayList<>();
        if (flatType.contains(getUserValidFlatTypes(usr))) {
            filteredProjects = searchFilter(usr,"","","", flatType,false,true);
        }
        return filteredProjects;
    }
    public List<String> filterNeighbourhood(user usr, String neighbourhood) {
        List<Project> filteredProjects;
        List<String> out = new ArrayList<>(List.of(""));
        filteredProjects = searchFilter(usr,"","",neighbourhood,getUserValidFlatTypes(usr),false,true);
        for (Project p : filteredProjects) {
            out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
        }
        return out;
    }
    public List<String> searchName(user usr, String name) {
        List<Project> filteredProjects;
        List<String> out = new ArrayList<>(List.of(""));
        filteredProjects = searchFilter(usr, name,"","",getUserValidFlatTypes(usr),false,true);
        for (Project p : filteredProjects) {
            out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
        }
        return out;
    }
    public int projectExists(user usr, String projectName, boolean visChk) {
        List<Project> filteredProjects;
        filteredProjects = searchFilter(usr,"",projectName,"", getUserValidFlatTypes(usr),false, visChk);
        return filteredProjects.size();
    }

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

    public Project getProjectObjByName(user usr, String projectName, boolean visChk) {
        List<Project> filteredProjects;
        filteredProjects = searchFilter(usr,"",projectName,"", getUserValidFlatTypes(usr), false,visChk);
        if (!filteredProjects.isEmpty()) {
            return filteredProjects.get(0);
        } else {
            return null;
        }
    }
    public List<String> getProjectByName(user usr, String projectName, EnquiriesManager enqMan, boolean visChk) {
        List<String> out = new ArrayList<>(List.of(""));
        Project p = getProjectObjByName(usr, projectName, visChk);
        if (p != null) {
            out.set(0, "\n" + p.viewFull(usr, getUserValidFlatTypes(usr), enqMan));
        }
        return out;
    }
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

    public List<String> getValidFilters(user usr) {
        if (usr instanceof HDBOfficer || usr instanceof HDBManager) {
            return List.of("Flat", "Neighbourhood", "My Projects","Reset Filter");
        } else {
            return List.of("Flat", "Neighbourhood","Reset Filter");
        }
    }
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
    
   
    
    public List<String> getProjectList(user usr) {
        /*
        // Managers see *all* project IDs
        if (usr.getPerms() == user.PermissionLevel.MANAGER) {
            return projectList.stream()
                              .map(Project::getProjectName)
                              .collect(Collectors.toList());
        }
        // Officers see only those they’re assigned to
        else if (usr.getPerms() == user.PermissionLevel.OFFICER) {
            return projectList.stream()
                              .filter(p -> p.getOfficersIDList().contains(usr.getUserID()))
                              .map(Project::getProjectName)
                              .collect(Collectors.toList());
        }
        // Applicants (and everyone else) now also see only the IDs of projects
        // they’re eligible for, filtered by flat‑type legality
        else {
            return projectList.stream()
                .filter(p -> {
                    // reuse your flat‑type weeding logic
                    String legal = getUserValidFlatTypes(usr);
                    return p.getFlatType1().contains(legal)
                        || p.getFlatType2().contains(legal);
                })
                .map(Project::getProjectName)
                .collect(Collectors.toList());
        }
        */
        //return projectsToString(filterFlat(usr, getUserValidFlatTypes(usr)));
        return projectsToString(usr, mergeProjects(filterFlat(usr, getUserValidFlatTypes(usr)), filterRelated(usr)));
    }
    
/**
 * Returns the names (IDs) of all projects this officer is assigned to.
 */








    /*
    public void joinProject(user usr, String projectName, RegistrationCriteria criteria){
        if(usr instanceof HDBOfficer) {
            String regID = UUID.randomUUID().toString();
            Register newRegistration = new Register(regID, usr.getUserID(), projectName, criteria);
            registrationManager.addRegistration(newRegistration);
            System.out.println("Registration submitted. Awaiting approval.");
        } else {
            System.out.println("No perms to join project");
        }
    }
    */
} 

