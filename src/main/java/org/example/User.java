package org.example;

public interface User {
    String UserID = "";
    String Password = "";
    Integer Age = 0; //not used for login
    Integer MaritalStatus = 0; //not used for login
    UserPermissions perms = null; //user perms stored here
    void changePassword();
}
