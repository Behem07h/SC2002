package org.action.registration;

public class Register {
    private String status;
    private String ID;
    private RegistrationCriteria criteria;
    private String user;
    private String projectID;

    public Register(String ID, String user, String projectID, RegistrationCriteria criteria) {
        this.ID = ID;
        this.status = "Pending";
        this.criteria = criteria;
        this.user = user;
        this.projectID = projectID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getID() {
        return ID;
    }

    public RegistrationCriteria getCriteria() {
        return criteria;
    }

    public String getUser() {
        return user;
    }

    public String getProjectID() {
        return projectID;
    }
}
