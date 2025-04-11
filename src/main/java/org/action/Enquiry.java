package org.action;

import java.time.LocalDateTime;

public class Enquiry implements act {
    private final String enquiryId;
    private String enquiryTxt;
    private String enquiryResponse;
    private LocalDateTime postDate;
    private LocalDateTime responseDate;
    private boolean withdrawn = false;

    public Enquiry(String enquiryId, String enquiryTxt) {
        this.enquiryId = enquiryId;
        this.enquiryTxt = enquiryTxt;
        this.postDate = LocalDateTime.now();
    }

    @Override
    public void view() {
        if (withdrawn) {
            System.out.println("This Enquiry has been withdrawn and its details are no longer visible.");
            return;
        }
        System.out.println("Enquiry ID: " + enquiryId);
    }

    @Override
    public void submit() {
        //todo: submit action (for editing?)
    }
    //todo: reply action


    public void withdraw() {
        if (!this.enquiryResponse.isEmpty()) {
            // Allow withdrawal only if there has not been a response
            withdrawn = true;
            System.out.println("Enquiry " + enquiryId + " withdrawn successfully.");
        } else {
            System.out.println("Enquiry " + enquiryId + " cannot be withdrawn because it has been answered.");
        }
    }

    public String getApplicationId() {
        return this.enquiryId;
    }

}




