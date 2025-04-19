package org.action.project;

import org.UI.ConfigLDR;
import org.Users.HDBManager.HDBManager;
import org.Users.HDBOfficer.HDBOfficer;
import org.Users.user;
import org.action.enquiry.EnquiriesManager;
import org.action.registration.Register;
import org.action.registration.RegistrationCriteria;
import org.action.registration.RegistrationManager;

import java.time.LocalDate;
import java.util.*;

public class ProjectManager {
    private final List<Project> projectList;
    private final String path = "data/db";
    private final String filename = "/project.csv";
    private RegistrationManager registrationManager;
    
    public ProjectManager() {
        projectList = new ArrayList<>();

        ConfigLDR ldr = new ConfigLDR();
        Map<String,String[]> pro_map = ldr.ReadToArrMap(path + filename);
        for (String key : pro_map.keySet()) {
            String[] items = pro_map.get(key);
            if (items.length < 13) {
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
            int officer_slots = Integer.parseInt(items[10]);
            String officers = items[11];
            boolean visible = Boolean.parseBoolean(items[12]);

            this.projectList.add(new Project(key, neighbourhood, type1, type1_count, type1_price, type2, type2_count, type2_price, opening_date, closing_date, manager, officer_slots, officers, visible));
        }

        registrationManager = new RegistrationManager();
    }

    public void store() {
        // run this when quitting program to store to csv
        Map<String,String[]> pro_map = new HashMap<>();
        for (Project p : projectList) {
            String[] items = {p.getNeighbourhood(),p.getFlatType1(), String.valueOf(p.getFlatCount1()), String.valueOf(p.getFlatPrice1()),p.getFlatType2(), String.valueOf(p.getFlatCount2()), String.valueOf(p.getFlatPrice2()), String.valueOf(p.getOpeningDate()), String.valueOf(p.getClosingDate()),p.getManagerId(), String.valueOf(p.getOfficerSlotCount()),p.getOfficersList(), String.valueOf(p.isVisible())};
            pro_map.put(String.valueOf(p.getProjectName()),items);
        }
        ConfigLDR ldr = new ConfigLDR();
        ldr.saveCSV(path + filename,pro_map);
    }

    public void createProject(user usr, String projectName, String neighbourhood, String type1, int type1_count, int type1_price, String type2, int type2_count, int type2_price, String opening_date, String closing_date, String manager, int officer_slots) {
        if (usr instanceof HDBManager) {
            boolean exists = false;
            for (Project p : projectList) {
                if (p.filter("", projectName,"","", false, false)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                System.out.println("Cannot create project with identical name");
            } else {
                projectList.add(new Project(projectName, neighbourhood, type1, type1_count, type1_price, type2, type2_count, type2_price, LocalDate.parse(opening_date), LocalDate.parse(closing_date), manager, officer_slots, "", false));
                System.out.println("New project created: " + projectName);
            }
        } else {
            System.out.println("No perms to edit project");
        }
    }
    public void deleteProject(user usr, String projectName) {
        if (usr instanceof HDBManager) {
            boolean removed = projectList.removeIf(p -> p.filter("",projectName,"","",false,false));
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
                if (p.filter("", projectNameOld,"","",false,false)) {
                    p.edit(projectName, neighbourhood, flatType1, flatCount1, flatPrice1, flatType2, flatCount2, flatPrice2, openingDate, closingDate, officerSlots);
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
                if (p.filter("",keyword,"","",false,false)) {
                    p.toggle_visibility();
                    return;
                }
            }
            System.out.println("No project found to toggle for keyword: " + keyword);
        } else {
            System.out.println("No perms to alter project visibility");
        }
    }
    private List<Project> searchFilter(user usr, String name, String nameExact, String neighbourhood, String flat, boolean visChk) {
        List<Project> out = new ArrayList<>();
        boolean visible_check = visChk; //default to true so new roles default to minimum perms
        boolean date_check = true;
        if (usr instanceof HDBOfficer || usr instanceof HDBManager) {
            visible_check = false;
            date_check = false;
        }

        for (Project p : projectList) {
            if (p.filter(name,nameExact,neighbourhood,flat, date_check, visible_check)) {
                out.add(p);
            }
        }
        return out;
    }

    public List<String> filterFlat(user usr, String flatType) {
        List<Project> filteredProjects;
        List<String> out = new ArrayList<>(List.of(""));
        if (flatType.contains(getUserValidFlatTypes(usr))) {
            filteredProjects = searchFilter(usr,"","","", flatType,true);
            for (Project p : filteredProjects) {
                out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
            }
            return out;
        }
        return new ArrayList<>(List.of(""));
    }
    public List<String> filterNeighbourhood(user usr, String neighbourhood) {
        List<Project> filteredProjects;
        List<String> out = new ArrayList<>(List.of(""));
        filteredProjects = searchFilter(usr,"","",neighbourhood,getUserValidFlatTypes(usr),true);
        for (Project p : filteredProjects) {
            out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
        }
        return out;
    }
    public List<String> searchName(user usr, String name) {
        List<Project> filteredProjects;
        List<String> out = new ArrayList<>(List.of(""));
        filteredProjects = searchFilter(usr, name,"","",getUserValidFlatTypes(usr),true);
        for (Project p : filteredProjects) {
            out.set(0, out.get(0) + "\n" + p.view(getUserValidFlatTypes(usr)));
        }
        return out;
    }
    public int projectExists(user usr, String projectName, boolean visChk) {
        List<Project> filteredProjects;
        filteredProjects = searchFilter(usr,"",projectName,"", getUserValidFlatTypes(usr), visChk);
        return filteredProjects.size();
    }
    public List<String> getProjectByName(user usr, String projectName, EnquiriesManager enqMan, boolean visChk) {
        List<Project> filteredProjects;
        List<String> out = new ArrayList<>(List.of(""));
        filteredProjects = searchFilter(usr,"",projectName,"", getUserValidFlatTypes(usr), visChk);
        for (Project p : filteredProjects) {
            out.set(0, out.get(0) + "\n" + p.viewFull(usr, getUserValidFlatTypes(usr), enqMan));
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
    public List<String> userFlatOptions(user usr, String projectName) {
        List<String> flatChoices = new ArrayList<>();
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
                if (!searchFilter(usr, "", projectName, "", flatStr, true).isEmpty()) {
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
        //Singles, 35 years old and above, can ONLY apply for 2-Room
        //Married, 21 years old and above, can apply for any flat types (2-
        //Room or 3-Room)
        return filterFlat(usr, getUserValidFlatTypes(usr));
    }

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

} 

