package org.action.registration;

import java.util.ArrayList;
import java.util.List;

public class RegistrationManager implements RegistrationAction {
    private List<Register> registrations;

    public RegistrationManager(){
        this.registrations = new ArrayList<>();
    }

    public void processRegistration(Register registration){
        // not done yet
        RegistrationCriteria criteria = registration.getCriteria();
        String user = registration.getUser();
        String projectID = registration.getProjectID();

        if(criteria.noIntention(user, projectID) && criteria.notHDBofficer(user, projectID)){
            registrations.add(registration);
        }
        else{
            registration.setStatus("Rejected");
        }
    }
    
    public void generateReport() {
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
    }

    @Override
    public void approveRegistration(Register registration) {
        // not done yet
        if(criteria.noIntention() && criteria.notHDBofficer()){
            registration.setStatus("Approved");
            System.out.println("Registration approved for ID: " + registration.getID());
        }
        else{
            rejectRegistration(registration);
        }
    }

    @Override
    public void rejectRegistration(Register registration) {
        registration.setStatus("Rejected");
        System.out.println("Registration denied for ID: " + registration.getID() + " due to criteria mismatch.");
    }

    @Override
    public void registerProject() {
        // not done yet
    }

    @Override
    public void viewRegistrationProject(Register registration) {
        System.out.println("Status for ID: " + registration.getID());

        System.out.println("Registration ID: " + registration.getID());
        System.out.println("Status: " + registration.getStatus());
        System.out.println("User: " + registration.getUser());
        System.out.println("Project: " + registration.getProjectID());
    }
}
