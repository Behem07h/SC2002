package org.Users.HDBOfficer;

public class HDBOfficerPermissions {
    Integer[] app_perms;
    Integer[] enq_perms;
    Integer[] proj_perms;

    public HDBOfficerPermissions(Integer usr_type) {
        switch (usr_type) {
            case 0: // officer
                app_perms = new Integer[]{1, 1, 1}; // Officers can access all application-related methods
                enq_perms = new Integer[]{1, 1, 1}; // Officers can create, edit, and delete enquiries
                proj_perms = new Integer[]{1, 1, 1, 1, 1}; // Officers can handle all project actions
                break;
            case 1: // manager
                break;
            case 2: // admin
                break;
        }
    }
}
