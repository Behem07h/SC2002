package org.action.receipt;


public class BookingReceipt {
   private String flatType;
   private String applicantName;
   private int applicantAge;
   private String applicantNRIC;
   private String projectDetails;
   private String applicantMartialStatus;
   private boolean isConfirmed = false;


   public BookingReceipt(String flatType, String applicantName, int applicantAge, String applicantNRIC, String projectDetails, String applicantMartialStatus) {
       this.flatType = flatType;
       this.applicantName = applicantName;
       this.applicantAge = applicantAge;
       this.applicantNRIC = applicantNRIC;
       this.projectDetails = projectDetails;
       this.applicantMartialStatus = applicantMartialStatus;


   }


   public String getFlatType() {
       return flatType;
   }


   public String getApplicantName() {
       return applicantName;
   }


   public int getApplicantAge() {
       return applicantAge;
   }


   public String getApplicantNRIC() {
       return applicantNRIC;
   }


   public String getProjectDetails() {
       return projectDetails;
   }


   public String getApplicantMartialStatus() {
       return applicantMartialStatus;
   }


   public boolean isConfirmed() {
       return isConfirmed;
   }


   public void confirm() {
       this.isConfirmed = true;
   }


   public void displayBooking() {
       System.out.println("Booking Info");
       System.out.println("Applicant Name : " + applicantName);
       System.out.println("Applicant NRIC: " + applicantNRIC);
       System.out.println("Applicant Age: " + applicantAge);
       System.out.println("Applicant Martial Status: " + applicantMartialStatus);
       System.out.println("Booked Flat Type: " + flatType);
       System.out.println("Project Details: " + projectDetails);


   }


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
