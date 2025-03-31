package org.User;

public interface User {
    String UserID = "";
    String username = "";
    String Password = "";
    String MaritalStatus = ""; //not used for login
    Integer Age = 0; //not used for login
    UserPermissions perms = null; //user perms stored here
    void changePassword(String oldPassword, String newPassword);
}
