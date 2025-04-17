package org.action;

import java.util.ArrayList;
import java.util.List;

public class projectcontroller {
    private List<Project> projectList;

    public projectcontroller() {
        projectList = new ArrayList<>();
    }

    public void deleteProjectByName(String name) {
        boolean removed = projectList.removeIf(p -> p.filterproject(name));
        if (removed) {
            System.out.println("Project '" + name + "' deleted from the system.");
        } else {
            System.out.println("No matching project found with name: " + name);
        }
    }

    public void j() {
        System.out.println("=== All Projects ===");
        for (Project p : projectList) {
            p.view();
            System.out.println("--------------------");
        }
    }

    public void updateProject(String keyword, Project updatedProject) {
        for (int i = 0; i < projectList.size(); i++) {
            if (projectList.get(i).filterproject(keyword)) {
                projectList.set(i, updatedProject);
                System.out.println("Project updated for keyword: " + keyword);
                return;
            }
        }
        System.out.println("No project found for update with keyword: " + keyword);
    }

    public void submitProject(String keyword) {
        for (Project p : projectList) {
            if (p.filterproject(keyword)) {
                p.submit();
                return;
            }
        }
        System.out.println("No project found to submit for keyword: " + keyword);
    }

    public void categorizeByFlatType(String flatType) {
        System.out.println("=== Projects with Flat Type: " + flatType + " ===");
        for (Project p : projectList) {
            if (p.filterproject(flatType)) {
                p.view();
                System.out.println("--------------------");
            }
        }
    }

    public List<Project> getProjectList() {
        return projectList;
    }
} 

