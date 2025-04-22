package org.action.project;

import org.Users.user;
import org.action.enquiry.EnquiriesManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Project {
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
    List<String> officersIDList;
    List<String> flatType1Bookings;
    List<String> flatType2Bookings;
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

    public String getOfficersIDList() {
        return String.join(":",officersIDList);
    }
    public String getFlatType1Bookings() {
        return String.join(":",flatType1Bookings);
    }
    public String getFlatType2Bookings() {
        return String.join(":",flatType2Bookings);
    }
    public int getBookingCount1() {
        return flatType1Bookings.size();
    }
    public int getBookingCount2() {
        return flatType2Bookings.size();
    }
    public boolean isVisible() {
        return visible;
    }

    public Project(String projectName, String neighbourhood, String flatType1, int flatCount1, int flatPrice1, String flatType2, int flatCount2, int flatPrice2, LocalDate openingDate, LocalDate closingDate, String managerId, int officerSlotCount, String officersList, String officersIDList, String flatType1Bookings, String flatType2Bookings, boolean visible) {
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
        this.officersIDList = new ArrayList<>(List.of(officersIDList.split(":")));
        this.flatType1Bookings = new ArrayList<>(List.of(flatType1Bookings.split(":")));
        this.flatType2Bookings = new ArrayList<>(List.of(flatType2Bookings.split(":")));
        this.visible = visible;
    }

    public String view(String flatsFilter) {
        return String.format("%s | %s",projectName, viewFlatDetails(flatsFilter,false));
    }

    public String viewFull(user usr, String flatsFilter, EnquiriesManager enqMan) {
        int enquiriesCount = enqMan.countProjectEnquiries(usr, projectName);
        return String.format("%s | %s\nApplication Period: %s to %s\nManager: %s\nOfficers: %s\nVisible: %s\n\nEnquiries: %s",projectName, viewFlatDetails(flatsFilter,true), openingDate, closingDate, managerId, officersList, visible, enquiriesCount);
    }

    public void addBooking(String appID, String flatType) {
        if (Objects.equals(flatType, flatType1)) {
            flatType1Bookings.add(appID);
        } else if (Objects.equals(flatType, flatType2)){
            flatType2Bookings.add(appID);
        }
    }

    public void removeBooking(String appID, String flatType) {
        if (Objects.equals(flatType, flatType1)) {
            flatType1Bookings.remove(appID);
        } else if (Objects.equals(flatType, flatType2)){
            flatType2Bookings.remove(appID);
        }
    }

    private String viewFlatDetails(String flatsFilter, boolean full) {
        String flatstring = "";
        if (flatType1.contains(flatsFilter)) {
            flatstring = flatstring + String.format("%s $%s%s", flatType1, flatPrice1, (full ? String.format(", %s/%s units free | ",flatCount1-flatType1Bookings.size(), flatCount1) : " | "));
        }
        if (flatType2.contains(flatsFilter)) {
            flatstring = flatstring + String.format("%s $%s%s", flatType2, flatPrice2, (full ? String.format(", %s/%s units free | ",flatCount2-flatType2Bookings.size(),flatCount2) : " | "));
        }
        return flatstring;
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

    public int flatAvailability(String flat_type) {
        if (flatType1.toLowerCase().contains(flat_type.toLowerCase())) {
            return flatCount1 - flatType1Bookings.size();
        } else if (flatType2.toLowerCase().contains(flat_type.toLowerCase())) {
            return flatCount2 - flatType2Bookings.size();
        }
        return 0;
    }

    public boolean filter(String name, String nameExact, String neighbourhood, String flat, boolean date, boolean visible) {
        boolean out = true;
        if (!nameExact.isEmpty()) {
            out = projectName.equalsIgnoreCase(nameExact);
        } else if (!name.isEmpty()) {
            out = projectName.toLowerCase().contains(name.toLowerCase());
        }
        if (!neighbourhood.isEmpty()) {
            out = out && neighbourhood.toLowerCase().contains(neighbourhood.toLowerCase());
        }
        if (!flat.isEmpty()) {
            out = out && (flatType1.toLowerCase().contains(flat.toLowerCase()) || flatType2.toLowerCase().contains(flat.toLowerCase()));
        }
        if (visible && date) {
            out = out && ((LocalDate.now().isAfter(openingDate) || LocalDate.now().isEqual(openingDate)) && LocalDate.now().isBefore(closingDate));
        }
        if (visible) {
            out = out && this.visible;
        }
        return out;
    }
}

   