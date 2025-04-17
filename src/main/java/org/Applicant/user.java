package org.Applicant;

public interface user {

    String getUserID();

    void setUserID(String userID);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getMaritalStatus();

    void setMaritalStatus(String maritalStatus);

    int getAge();

    void setAge(int age);


    boolean changePassword(String oldPassword, String newPassword);

    default String[] act(String action) {
        String[] output = new String[] {};
        switch (action){
            //enquiry methods
            case "submit-enquiry":
                break;
            case "delete-enquiry":
                break;
            case "edit-enquiry":
                break;
            case "reply-enquiry":
                break;
            //application methods
            case "view-applications":
                break;
            case "add-application":
                break;
            case "retrieve-application":
                break;
            case "withdraw-application":
                break;
            case "approve-application":
                break;
            case "reject-application":
                break;
            //project methods
            case "view-projects":
                break;
            case "search-projects":
                break;
            case "filter-projects":
                break;
            case "delete-projects":
                break;
            default:
                return output;
        }
    } //wrapper fn that handles user inputs for all actions
}


