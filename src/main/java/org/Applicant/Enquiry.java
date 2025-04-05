package org.Applicant;

import java.time.LocalDateTime;
import java.util.UUID;

public class Enquiry {
    private final String enquiryID;
    private final String projectID;
    private final String creatorUserID;
    private String content;
    private String response;
    private final LocalDateTime createdAt;
    private boolean isResolved;

    public Enquiry(String projectID, String creatorUserID, String content) {
        this.enquiryID = UUID.randomUUID().toString();
        this.projectID = projectID;
        this.creatorUserID = creatorUserID;
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.response = "";
        this.isResolved = false;
    }

    // Getters
    public String getEnquiryID() { return enquiryID; }
    public String getProjectID() { return projectID; }
    public String getCreatorUserID() { return creatorUserID; }
    public String getContent() { return content; }
    public String getResponse() { return response; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isResolved() { return isResolved; }

    public void editContent(String newContent) { this.content = newContent; }
    public void resolveEnquiry(String response) {
        this.response = response;
        this.isResolved = true;
    }
}