package org.action.registration;

public class Register {
    private String status;
    private String regID;
    private String user;
    private String projectID;
    private RegistrationCriteria criteria;

    public Register(String ID, String user, String projectID, RegistrationCriteria criteria) {
        this.regID = ID;
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
        return regID;
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
