package org.Applicant;

public interface Applicant {
    String UserID = "";
    String username = "";
    String Password = "";
    String MaritalStatus = ""; //not used for login
    Integer Age = 0; //not used for login
    ApplicantPermissions perms = null; //user perms stored here
    void changePassword(String oldPassword, String newPassword);
}
