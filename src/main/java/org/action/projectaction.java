package org.action;

import org.action.act;
import java.time.LocalDateTime;

public class projectaction implements act {  
    private String projectName;
    private String Neighborhood;
    private String TypeOfFlat;
    private int NumberOfUnits;
    private String ApplicationOpeningDate;
    private String ApplicationClosingDate;
    private int OfficerSlot;
    private boolean submitted;  

    
    public projectaction(String projectName, String Neighborhood, String TypeOfFlat, int NumberOfUnits,
                         String ApplicationOpeningDate, String ApplicationClosingDate, int OfficerSlot) {
        this.projectName = projectName;
        this.Neighborhood = Neighborhood;
        this.TypeOfFlat = TypeOfFlat;
        this.NumberOfUnits = NumberOfUnits;
        this.ApplicationOpeningDate = ApplicationOpeningDate;
        this.ApplicationClosingDate = ApplicationClosingDate;
        this.OfficerSlot = OfficerSlot;
        this.submitted = false;  
    }

    @Override
    public void view() {
        System.out.println("Project Name: " + projectName);
        System.out.println("Neighborhood: " + Neighborhood);
        System.out.println("Flat Type: " + TypeOfFlat);
        System.out.println("Number of Units: " + NumberOfUnits);
        System.out.println("Application Opening Date: " + ApplicationOpeningDate);
        System.out.println("Application Closing Date: " + ApplicationClosingDate);
        System.out.println("Officer Slot: " + OfficerSlot);
        System.out.println("Submitted: " + (submitted ? "Yes" : "No"));
    }

   
    @Override
    public void submit() {
        if (!submitted) {
            submitted = true;
            System.out.println("Project \"" + projectName + "\" has been submitted successfully.");
        } else {
            System.out.println("Project \"" + projectName + "\" has already been submitted.");
        }
    }

    public void editproject(String newProjectName, String newNeighborhood, String newTypeOfFlat, int newNumberOfUnits,
    String newOpeningDate, String newClosingDate, int newOfficerSlot) {
        this.projectName = newProjectName;
        this.Neighborhood = newNeighborhood;
        this.TypeOfFlat = newTypeOfFlat;
        this.NumberOfUnits = newNumberOfUnits;
        this.ApplicationOpeningDate = newOpeningDate;
        this.ApplicationClosingDate = newClosingDate;
        this.OfficerSlot = newOfficerSlot;

        System.out.println("Project details updated successfully.");

    }
    public void createproject( String projectName, String neighborhood, String typeOfFlat, int numberOfUnits,
    String openingDate, String closingDate, int officerSlot) {
        this.projectName = projectName;
        this.Neighborhood = neighborhood;
        this.TypeOfFlat = typeOfFlat;
        this.NumberOfUnits = numberOfUnits;
        this.ApplicationOpeningDate = openingDate;
        this.ApplicationClosingDate = closingDate;
        this.OfficerSlot = officerSlot;
        this.submitted = false;

        System.out.println("New project \"" + projectName + "\" has been created.");
    
    }
    public boolean filterproject(String keyword) {
    keyword = keyword.toLowerCase();

    return projectName.toLowerCase().contains(keyword)
        || Neighborhood.toLowerCase().contains(keyword)
        || TypeOfFlat.toLowerCase().contains(keyword)
        || ApplicationOpeningDate.toLowerCase().contains(keyword)
        || ApplicationClosingDate.toLowerCase().contains(keyword);
    }
}

   