package org.action;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//todo: track how many flats have already been booked, update this whenever an application status is updated
//todo: track how many applications have been made for the project
//todo: track amount of enquiries there are about the project
public class Project implements Act {
    String projectName;
    String neighbourhood;
    String flatType1;
    int flatCount1;
    int flatPrice1;
    String flatType2;
    int flatCount2;
    int flatPrice2;
    LocalDate openingDate;
    LocalDate closingDate;
    String managerId;
    int officerSlotCount;
    List<String> officersList;
    boolean visible;

    public String getProjectName() {
        return projectName;
    }

    public String getNeighbourhood() {
        return neighbourhood;
    }

    public String getFlatType1() {
        return flatType1;
    }

    public int getFlatCount1() {
        return flatCount1;
    }

    public int getFlatPrice1() {
        return flatPrice1;
    }

    public String getFlatType2() {
        return flatType2;
    }

    public int getFlatCount2() {
        return flatCount2;
    }

    public int getFlatPrice2() {
        return flatPrice2;
    }

    public LocalDate getOpeningDate() {
        return openingDate;
    }

    public LocalDate getClosingDate() {
        return closingDate;
    }

    public String getManagerId() {
        return managerId;
    }

    public int getOfficerSlotCount() {
        return officerSlotCount;
    }

    public String getOfficersList() {
        return String.join(":",officersList);
    }

    public boolean isVisible() {
        return visible;
    }

    public Project(String projectName, String neighbourhood, String flatType1, int flatCount1, int flatPrice1, String flatType2, int flatCount2, int flatPrice2, LocalDate openingDate, LocalDate closingDate, String managerId, int officerSlotCount, String officersList, boolean visible) {
        this.projectName = projectName;
        this.neighbourhood = neighbourhood;
        this.flatType1 = flatType1;
        this.flatCount1 = flatCount1;
        this.flatPrice1 = flatPrice1;
        this.flatType2 = flatType2;
        this.flatCount2 = flatCount2;
        this.flatPrice2 = flatPrice2;
        this.openingDate = openingDate;
        this.closingDate = closingDate;
        this.managerId = managerId;
        this.officerSlotCount = officerSlotCount;
        this.officersList = new ArrayList<>(List.of(officersList.split(":")));
        this.visible = visible;
        //todo: a project can have multiple flat types, but an applicant only can view and apply for specific flat types (is this always 1?)
        //todo: .: we need to display the appropriate flat type based on the applicant
    }

    @Override
    public String view() {
        return String.format("%s | %s $%s | %s $%s",projectName, flatType1, flatPrice1, flatType2, flatPrice2);
    }

    public String viewFull() {
        return String.format("%s | %s $%s, %s units | %s $%s, %s units\nApplication Period: %s to %s\nManager: %s\nOfficers: %s\nVisible: %s",projectName, flatType1, flatPrice1, flatCount1, flatType2, flatPrice2, flatCount2, openingDate, closingDate, managerId, officersList, visible);
    }

    public void toggle_visibility() {
        this.visible = !this.visible;
        System.out.println("Visibility has been toggled to " + this.visible);
    }

    public void edit(String projectName, String neighbourhood, String flatType1, int flatCount1, int flatPrice1, String flatType2, int flatCount2, int flatPrice2, String openingDate, String closingDate, int officerSlots) {
        if (!projectName.isEmpty()) {this.projectName = projectName;}
        if (!neighbourhood.isEmpty()) {this.neighbourhood = neighbourhood;}
        if (!flatType1.isEmpty()) {this.flatType1 = flatType1;}
        if (flatCount1 > 0) {this.flatCount1 = flatCount1;}
        if (flatPrice1 > 0) {this.flatPrice1 = flatPrice1;}
        if (!flatType2.isEmpty()) {this.flatType2 = flatType2;}
        if (flatCount2 > 0) {this.flatCount2 = flatCount2;}
        if (flatPrice2 > 0) {this.flatPrice2 = flatPrice2;}
        if (!openingDate.isEmpty()) {this.openingDate = LocalDate.parse(openingDate);}
        if (!closingDate.isEmpty()) {this.closingDate = LocalDate.parse(closingDate);}
        if (officerSlots > 0) {this.officerSlotCount = officerSlots;}
        System.out.println("Project details updated successfully.");

    }

    public boolean filter(String name, String nameExact, String neighbourhood, String flat, boolean date, boolean visible) {
        boolean out = true;
        if (!nameExact.isEmpty()) {
            out = projectName.equalsIgnoreCase(nameExact);
        } else if (!name.isEmpty()) {
            out = projectName.toLowerCase().contains(name.toLowerCase());
        }
        if (!neighbourhood.isEmpty()) {
            out = out && neighbourhood.toLowerCase().contains(neighbourhood);
        }
        if (!flat.isEmpty()) {
            out = out && (flatType1.toLowerCase().contains(flat) || flatType2.toLowerCase().contains(flat));
        }
        if (date) {
            out = out && ((LocalDate.now().isAfter(openingDate) || LocalDate.now().isEqual(openingDate)) && LocalDate.now().isBefore(closingDate));
        }
        if (visible) {
            out = out && this.visible;
        }
        return out;
    }
}

   