package org.action.registration;

import org.Users.HDBManager.HDBManager;
import org.Users.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class RegistrationManager implements RegistrationAction {
    private List<Register> registrations;

    public RegistrationManager(){
        this.registrations = new ArrayList<>();
    }

    // public void processRegistration(Register registration){
    //     RegistrationCriteria criteria = registration.getCriteria();
    //     String user = registration.getUser();
    //     String projectID = registration.getProjectID();

    //     if(criteria.noIntention(user, projectID) && criteria.notHDBofficer(user, projectID)){
    //         registrations.add(registration);
    //     }
    //     else{
    //         registration.setStatus("Rejected");
    //     }
    // }

    public void addRegistration(Register registration) {
        registrations.add(registration);
    }
    
    public void generateReport(user usr) {
        if(usr instanceof HDBManager) {
            System.out.println("=== Registration Report ===");
            
            int pendingCount = 0;
            int approvedCount = 0;
            int rejectedCount = 0;
            
            for (Register reg : registrations) {
                switch (reg.getStatus()) {
                    case "Pending":
                        pendingCount++;
                        break;
                    case "Approved":
                        approvedCount++;
                        break;
                    case "Rejected":
                        rejectedCount++;
                        break;
                }
            }

            System.out.println("Pending registrations: " + pendingCount);
            System.out.println("Approved registrations: " + approvedCount);
            System.out.println("Rejected registrations: " + rejectedCount);
            System.out.println("Total registrations: " + registrations.size());
        } else {
            System.out.println("No perms to handle registrations");
        }
    }

    @Override
    public void approveRegistration(Register registration, user usr) {
        RegistrationCriteria criteria = registration.getCriteria();
        String user = registration.getUser();
        String projectID = registration.getProjectID();

        if(usr instanceof HDBManager) {
            if(criteria.noIntention(user, projectID) && criteria.notHDBofficer(user, projectID)){
                registration.setStatus("Approved");
                System.out.println("Registration approved for ID: " + registration.getID());
            }
            else{
                rejectRegistration(registration, usr);
            }
        } else {
            System.out.println("No perms to handle registrations");
        }
    }

    @Override
    public void rejectRegistration(Register registration, user usr) {
        if(usr instanceof HDBManager) {
            registration.setStatus("Rejected");
            System.out.println("Registration denied for ID: " + registration.getID() + " due to criteria mismatch.");
        } else {
            System.out.println("No perms to handle registrations");
        }
    }

    @Override
    public void registerProject(Register registration, user usr) {
        String user = registration.getUser();
        String projectID = registration.getProjectID();

        if (usr instanceof HDBManager) {
            String projectsFile = "projects.csv";
            List<List<String>> data = new ArrayList<>();
            try (Scanner scanner = new Scanner(new File(projectsFile))) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    String[] values = line.split(",");
                    List<String> lineData = Arrays.asList(values);
                    data.add(lineData);
                }
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            for (int i = 1; i < data.size(); i++) {
                List<String> row = data.get(i);
                if (!row.isEmpty() && row.get(0).equalsIgnoreCase(projectID)) {
                    while (row.size() <= 12) {
                        row.add("");
                    }

                    String existing = row.get(12).trim();
                    Set<String> officers = new LinkedHashSet<>(Arrays.asList(existing.split(",")));

                    if (!officers.contains(user)) {
                        officers.add(user);
                        row.set(12, String.join(",", officers));
                        System.out.println("Project registered for " + user);
                    }
                    else {
                        System.out.println("Failed to register officer for project");
                    }
                    break;
                }

                try (PrintWriter writer = new PrintWriter(new FileWriter(projectsFile))) {
                    for (List<String> rowData : data) {
                        writer.println(String.join(",", rowData));
                    }
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("No perms to handle registrations");
        }
    }

    @Override
    public void viewRegistrationProject(Register registration, user usr) {
        if (usr instanceof HDBManager) {
            System.out.println("Status for ID: " + registration.getID());
            System.out.println("Registration ID: " + registration.getID());
            System.out.println("Status: " + registration.getStatus());
            System.out.println("User: " + registration.getUser());
            System.out.println("Project: " + registration.getProjectID());
        } else {
            System.out.println("No perms to handle registrations");
        }
    }
}
