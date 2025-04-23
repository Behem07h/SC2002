/**
 * Represents a housing project with details about available flats, dates, management team, and bookings.
 * <p>
 * This class encapsulates all information related to a Housing Development Board (HDB) project,
 * including project metadata, flat types, pricing, availability, application periods, and the
 * management team responsible for the project. It provides methods for viewing, editing,
 * and filtering project information based on various criteria. </p>
 * @author Group 1- Beitricia Jassindah, Bryan, Cai Yuqin, Lin Jia Rong, Tan Min
 * @version 1.0
 * @since 2025-04-23

 */
package org.action.project;

import org.Users.HDBManager.HDBManager;
import org.Users.HDBOfficer.HDBOfficer;
import org.Users.user;
import org.action.enquiry.EnquiriesManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Project {

    /** Name of the project */
    String projectName;

    /** Neighborhood location of the project */
    String neighbourhood;

    /** Type identifier of flag in this project */
    String flatType1;

    /** Total number of flats available in the first flat category */
    int flatCount1;

    /** Price of flat1 category */
    int flatPrice1;

    /** Type identifier of flag in this project */
    String flatType2;

    /** Type identifier for the flag2 available in this project */
    int flatCount2;

    /** Price of flat3 category */
    int flatPrice2;

    /** Date when applications for this project open */
    LocalDate openingDate;

    /** Date when applications for this project close */
    LocalDate closingDate;

    /** Name of the HDB manager responsible for this project */
    String managerName;

    /** User ID of the HDB manager responsible for this project */
    String managerId;

    /** Number of officers that can be assigned to this project */
    int officerSlotCount;

    /** List of officers' names assigned to this project */
    List<String> officersList;

    /** List of officers' IDs assigned to this project */
    List<String> officersIDList;

    /** List of booking application IDs for flats of the first type */
    List<String> flatType1Bookings;

    /** List of booking application IDs for flats of the second type */
    List<String> flatType2Bookings;

    /** Flag indicating whether the project is visible for public listings */
    boolean visible;

    /**
     * Gets the name of the project.
     *
     * @return The project name
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Gets the location of the neighbourhood is located.
     *
     * @return The neighborhood name
     */
    public String getNeighbourhood() {
        return neighbourhood;
    }

    /**
     * Gets the type identifier for flag1.
     *
     * @return The flat type 1 identifier
     */
    public String getFlatType1() {
        return flatType1;
    }

    /**
     * Gets the total number of flats available for flag1.
     *
     * @return The count of flats of type 1
     */
    public int getFlatCount1() {
        return flatCount1;
    }

    /**
     * Gets the price of flats1.
     *
     * @return The price of flat1
     */
    public int getFlatPrice1() {
        return flatPrice1;
    }

    /**
     * Gets the type identifier for the flag2.
     *
     * @return The flat type 2 identifier
     */
    public String getFlatType2() {
        return flatType2;
    }

    /**
     * Gets the total number of flats available for flag2.
     *
     * @return The count of flats of type 2
     */
    public int getFlatCount2() {
        return flatCount2;
    }

    /**
     * Gets the price of flats in the second category.
     *
     * @return The price of flat type 2
     */
    public int getFlatPrice2() {
        return flatPrice2;
    }

    /**
     * Gets the date when applications for this project open.
     *
     * @return The opening date
     */
    public LocalDate getOpeningDate() {
        return openingDate;
    }

    /**
     * Gets the date when applications for this project close.
     *
     * @return The closing date
     */
    public LocalDate getClosingDate() {
        return closingDate;
    }

    /**
     * Gets the name of the HDB manager responsible for this project.
     *
     * @return The manager's name
    */
    public String getManagerName() {
        return managerName;
    }

    /**
     * Gets the user ID of the manager responsible for this project.
     *
     * @return The manager's ID
     */
    public String getManagerId() {
        return managerId;
    }

    /**
     * Gets the number of officers that can be assigned to this project.
     *
     * @return The officer slot count
     */
    public int getOfficerSlotCount() {
        return officerSlotCount;
    }

    /**
     * Gets the list of officers assigned to this project as a colon-separated string.
     *
     * @return The officers' names as a colon-separated string
     */
    public String getOfficersList() {
        return String.join(":",officersList);
    }

    /**
     * Gets the list of officer IDs assigned to this project as a colon-separated string.
     *
     * @return The officers' IDs as a colon-separated string
     */
    public String getOfficersIDList() {
        return String.join(":",officersIDList);
    }

    /**
     * Gets the list of booking application IDs for flats of flag1 as a colon-separated string.
     *
     * @return The flat type 1 bookings as a colon-separated string
     */
    public String getFlatType1Bookings() {
        return String.join(":",flatType1Bookings);
    }

    /**
     * Gets the list of booking application IDs for flats of flag2 as a colon-separated string.
     *
     * @return The flat type 2 bookings as a colon-separated string
     */
    public String getFlatType2Bookings() {
        return String.join(":",flatType2Bookings);
    }

    /**
     * Gets the number of bookings for flats of flag1.
     *
     * @return The booking count for flat1
     */
    public int getBookingCount1() {
        return flatType1Bookings.size();
    }

    /**
     * Gets the number of bookings for flats of the second type.
     *
     * @return The booking count for flat type 2
     */
    public int getBookingCount2() {
        return flatType2Bookings.size();
    }

    /**
     * Checks if the project is visible in public listings.
     *
     * @return true if the project is visible, false otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Constructs a new Project with all required details.
     *
     * @param projectName The name of the project
     * @param neighbourhood The location of where the neighbourhood is located
     * @param flatType1 The type identifier for the first flat category
     * @param flatCount1 The total number of flats available in the flag1
     * @param flatPrice1 The price of flats in the flag1
     * @param flatType2 The type identifier for the flag2
     * @param flatCount2 The total number of flats available for flag2
     * @param flatPrice2 The price of flats in the flag2
     * @param openingDate The date when applications for this project open
     * @param closingDate The date when applications for this project close
     * @param managerName The name of the HDB manager responsible for this project
     * @param managerId The user ID of the HDB manager responsible for this project
     * @param officerSlotCount The number of officers that can be assigned to this project
     * @param officersList The list of officers' names assigned to this project as a colon-separated string
     * @param officersIDList The list of officers' IDs assigned to this project as a colon-separated string
     * @param flatType1Bookings The list of booking application IDs for flats of the first type as a colon-separated string
     * @param flatType2Bookings The list of booking application IDs for flats of the second type as a colon-separated string
     * @param visible Flag indicating whether the project is visible in public listings
     */
    public Project(String projectName, String neighbourhood, String flatType1, int flatCount1, int flatPrice1, String flatType2, int flatCount2, int flatPrice2, LocalDate openingDate, LocalDate closingDate, String managerName, String managerId, int officerSlotCount, String officersList, String officersIDList, String flatType1Bookings, String flatType2Bookings, boolean visible) {
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
        this.managerName = managerName;
        this.managerId = managerId;
        this.officerSlotCount = officerSlotCount;
        this.officersList = new ArrayList<>(List.of(officersList.split(":")));
        this.officersIDList = new ArrayList<>(List.of(officersIDList.split(":")));
        this.flatType1Bookings = new ArrayList<>(List.of(flatType1Bookings.split(":")));
        this.flatType1Bookings.remove("");
        this.flatType2Bookings = new ArrayList<>(List.of(flatType2Bookings.split(":")));
        this.flatType2Bookings.remove("");
        this.visible = visible;
    }

    /**
     * Generates a view of the project with basic information.
     *
     * @param flatsFilter Filter to specify which flat types to include in the view
     * @return A formatted string with basic project information
     */
    public String view(String flatsFilter) {
        return String.format("%s | %s",projectName, viewFlatDetails(flatsFilter,false));
    }

    /**
     * Generates a detailed view of the project including management information and enquiries.
     *
     * @param usr The user requesting the view
     * @param flatsFilter Filter to specify which flat types to include in the view
     * @param enqMan Enquiries manager to count project-related enquiries
     * @return A formatted string with detailed project information
     */
    public String viewFull(user usr, String flatsFilter, EnquiriesManager enqMan) {
        int enquiriesCount = enqMan.countProjectEnquiries(usr, projectName);
        return String.format("%s | %s\nApplication Period: %s to %s\nManager: %s\nOfficers: %s\nVisible: %s\n\nEnquiries: %s",projectName, viewFlatDetails(flatsFilter,true), openingDate, closingDate, managerName, officersList, visible, enquiriesCount);
    }

    /**
     * Adds a booking application ID to the appropriate flat type booking list.
     *
     * @param appID The application ID to add
     * @param flatType The flat type for which the booking is being made
     */
    public void addBooking(String appID, String flatType) {
        if (Objects.equals(flatType, flatType1)) {
            flatType1Bookings.add(appID);
        } else if (Objects.equals(flatType, flatType2)){
            flatType2Bookings.add(appID);
        }
    }

    /**
     * Removes a booking application ID from the appropriate flat type booking list.
     *
     * @param appID The application ID to remove
     * @param flatType The flat type from which the booking is being removed
     */
    public void removeBooking(String appID, String flatType) {
        if (Objects.equals(flatType, flatType1)) {
            flatType1Bookings.remove(appID);
        } else if (Objects.equals(flatType, flatType2)){
            flatType2Bookings.remove(appID);
        }
    }

    /**
     * Generates a formatted string with details about flat types based on filtering.
     *
     * @param flatsFilter Filter to specify which flat types to include
     * @param full Whether the flag is full
     * @return A formatted string with flat type details
     */
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

    /**
     * Toggles the visibility of the project in public listings.
     */
    public void toggle_visibility() {
        this.visible = !this.visible;
        System.out.println("Visibility has been toggled to " + this.visible);
    }

    /**
     * Updates the project's details with new values.
     *
     * @param projectName New project name (or empty to keep current)
     * @param neighbourhood New neighborhood (or empty to keep current)
     * @param flatType1 New flat type 1 identifier (or empty to keep current)
     * @param flatCount1 New flat count for type 1 (or 0 to keep current)
     * @param flatPrice1 New flat price for type 1 (or 0 to keep current)
     * @param flatType2 New flat type 2 identifier (or empty to keep current)
     * @param flatCount2 New flat count for type 2 (or 0 to keep current)
     * @param flatPrice2 New flat price for type 2 (or 0 to keep current)
     * @param openingDate New opening date as string (or empty to keep current)
     * @param closingDate New closing date as string (or empty to keep current)
     * @param officerSlots New officer slot count (or 0 to keep current)
     */
    public void edit(String projectName, String neighbourhood, String flatType1, int flatCount1, int flatPrice1, String flatType2, int flatCount2, int flatPrice2, String openingDate, String closingDate, int officerSlots) {
        try {
            if (!projectName.isEmpty()) {
                this.projectName = projectName;
            }
            if (!neighbourhood.isEmpty()) {
                this.neighbourhood = neighbourhood;
            }
            if (!flatType1.isEmpty()) {
                this.flatType1 = flatType1;
            }
            if (flatCount1 > 0) {
                this.flatCount1 = flatCount1;
            }
            if (flatPrice1 > 0) {
                this.flatPrice1 = flatPrice1;
            }
            if (!flatType2.isEmpty()) {
                this.flatType2 = flatType2;
            }
            if (flatCount2 > 0) {
                this.flatCount2 = flatCount2;
            }
            if (flatPrice2 > 0) {
                this.flatPrice2 = flatPrice2;
            }
            if (!openingDate.isEmpty()) {
                this.openingDate = LocalDate.parse(openingDate);
            }
            if (!closingDate.isEmpty()) {
                this.closingDate = LocalDate.parse(closingDate);
            }
            if (officerSlots > 0) {
                this.officerSlotCount = officerSlots;
            }
            System.out.println("Project details updated successfully.");
        } catch (Error e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Updates the list of officers assigned to this project.
     *
     * @param officersIDList New list of officer IDs as a colon-separated string
     */
    public void editOfficerList(String officersIDList) {
         this.officersIDList = new ArrayList<>(Arrays.asList(officersIDList.split(":")));
    }

    /**
     * Checks the availability of a specific flat type in this project.
     *
     * @param flat_type The flat type to check availability for
     * @return The number of available units of the specified flat type
     */
    public int flatAvailability(String flat_type) {
        if (flatType1.toLowerCase().contains(flat_type.toLowerCase())) {
            return flatCount1 - flatType1Bookings.size();
        } else if (flatType2.toLowerCase().contains(flat_type.toLowerCase())) {
            return flatCount2 - flatType2Bookings.size();
        }
        return 0;
    }

    /**
     * Checks if a user is the manager or an officer of this project.
     *
     * @param usr The user to check
     * @return true if the user is the manager or an officer of the project, false otherwise
     */
    public boolean managerOfficerOf(user usr) {
        if (usr instanceof HDBManager) {
            return getManagerId().contains(usr.getUserID());
        } else if (usr instanceof HDBOfficer) {
            return getOfficersIDList().contains(usr.getUserID());
        } else {
            return false;
        }
    }

    /**
     * Checks if one date is before or equal to another date.
     *
     * @param date1 The first date
     * @param date2 The second date
     * @return true if date1 is before or equal to date2, false otherwise
     */
    private boolean beforeEq(LocalDate date1, LocalDate date2) {
        return date1.isBefore(date2) || date1.isEqual(date2);
    }

    /**
     * Checks if this project's application period overlaps with another date range.
     *
     * @param start_date Start date of the period to check
     * @param end_date End date of the period to check
     * @return true if there is an overlap, false otherwise
     */
    public boolean overlapCheck(LocalDate start_date, LocalDate end_date) {
        if (beforeEq(openingDate, start_date) && beforeEq(start_date, closingDate)) {
            //if start of p lies between the opening and closing of this project, they overlap
            return true;
        } else if (beforeEq(openingDate, end_date) && beforeEq(end_date, closingDate)) {
            //if end of p lies between the opening and closing of this project, they overlap
            return true;
        } else if (beforeEq(start_date, openingDate) && beforeEq(openingDate, end_date)) {
            //if the start of this lies between the opening and closing of p, they overlap
            return true;
        } else if (beforeEq(start_date, closingDate) && beforeEq(closingDate, end_date)) {
            //if the end of this lies between the opening and closing of p, they overlap
            return true;
        }
        return false;
    }

    /**
     * Filters the project based on various criteria.
     *
     * @param name Project name contains filter (case-insensitive)
     * @param nameExact Project name exact match filter (case-insensitive)
     * @param neighbourhood Neighborhood contains filter (case-insensitive)
     * @param flat Flat type contains filter (case-insensitive)
     * @param usr User filter for projects manager/officer with this user (null to skip)
     * @param date Whether to filter by current date being within application period
     * @param visible Whether to filter by project visibility
     * @return true if the project matches all applied filters, false otherwise
     */
    public boolean filter(String name, String nameExact, String neighbourhood, String flat, user usr, boolean date, boolean visible) {
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
        if (usr != null) {
            return managerOfficerOf(usr);
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

   