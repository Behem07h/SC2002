/**
 * Represents a receipt for an HDB flat booking.
 * @author Group 1
 * @version 1.0
 * @since 2025-04-23
 */
package org.receipt;

/**
 * This class contains information about the flat booking, including applicant details,
 * flat type, and project information. It provides methods to generate and display receipts,
 * as well as track the confirmation status of the booking.
 */
public class BookingReceipt {
    /**
     * Type of flat being booked (e.g., 2-Room, 3-Room, 4-Room, etc.)
     */
   private String flatType;
    /**
     * Name of the applicant
     */
   private String applicantName;
    /**
     * Age of the applicant
     */
   private int applicantAge;
    /**
     * NRIC number of the applicant
     */
   private String applicantNRIC;
    /**
     * Details about the HDB project
     */
   private String projectDetails;
    /**
     * Marital status of the applicant
     */
   private String applicantMartialStatus;
    /**
     * Boolean Indicator whether the booking is confirmed
     */
   private boolean isConfirmed = false;

    /**
     * Constructs a new booking receipt with the specified details.
     *
     * @param flatType the type of flat being booked
     * @param applicantName the name of the applicant
     * @param applicantAge the age of the applicant
     * @param applicantNRIC the NRIC of the applicant
     * @param projectDetails details about the HDB project
     * @param applicantMartialStatus the marital status of the applicant
     */
   public BookingReceipt(String flatType, String applicantName, int applicantAge, String applicantNRIC, String projectDetails, String applicantMartialStatus) {
       this.flatType = flatType;
       this.applicantName = applicantName;
       this.applicantAge = applicantAge;
       this.applicantNRIC = applicantNRIC;
       this.projectDetails = projectDetails;
       this.applicantMartialStatus = applicantMartialStatus;


   }

    /**
     * Returns the type of flat being booked.
     *
     * @return the flat type
     */
   public String getFlatType() {
       return flatType;
   }

    /**
     * Returns the name of the applicant.
     *
     * @return the applicant's name
     */
   public String getApplicantName() {
       return applicantName;
   }

    /**
     * Returns the age of the applicant.
     *
     * @return the applicant's age
     */
   public int getApplicantAge() {
       return applicantAge;
   }

    /**
     * Returns the NRIC of the applicant.
     *
     * @return the applicant's NRIC
     */
   public String getApplicantNRIC() {
       return applicantNRIC;
   }

    /**
     * Returns the details of the HDB project.
     *
     * @return the project details
     */
   public String getProjectDetails() {
       return projectDetails;
   }

    /**
     * Returns the marital status of the applicant.
     *
     * @return the applicant's marital status
     */
   public String getApplicantMartialStatus() {
       return applicantMartialStatus;
   }

    /**
     * Returns whether the booking is confirmed.
     *
     * @return true if the booking is confirmed, false otherwise
     */
   public boolean isConfirmed() {
       return isConfirmed;
   }

    /**
     * Returns whether the booking is confirmed.
     **/
   public void confirm() {
       this.isConfirmed = true;
   }

    /**
     * Displays the booking information to the console.

     * This method prints all relevant details of the booking in a user-friendly format.
     */
   public void displayBooking() {
       System.out.println("Booking Info");
       System.out.println("Applicant Name : " + applicantName);
       System.out.println("Applicant NRIC: " + applicantNRIC);
       System.out.println("Applicant Age: " + applicantAge);
       System.out.println("Applicant Martial Status: " + applicantMartialStatus);
       System.out.println("Booked Flat Type: " + flatType);
       System.out.println("Project Details: " + projectDetails);


   }

    /**
     * Generates and prints a formatted receipt to the console.
     * This method creates a detailed receipt with the current date and time.
     */
   public void generateReceipt() {
       System.out.println("============== HDB BOOKING RECEIPT ==============");
       System.out.println("NRIC: " + applicantNRIC);
       System.out.println("Name: " + applicantName);
       System.out.println("Age: " + applicantAge);
       System.out.println("Martial Status: " + applicantMartialStatus);
       System.out.println("Flat Type: " + flatType);
       System.out.println("Project Details: " + projectDetails);
       System.out.println("Receipt Issued At: " + java.time.LocalDateTime.now());
       System.out.println("=================================================");
   }


  
}
