package org.User;

public class UserPermissions {
    Integer[] app_perms; //array of what methods they can run from each action class, in order of class diag
    Integer[] enq_perms;
    Integer[] proj_perms;

    public UserPermissions(Integer usr_type) {
        switch (usr_type) {
            case 0: //applicant
                app_perms = new Integer[]{1, 0, 0, 0, 0}; //applicants can only withdraw their application
                enq_perms = new Integer[]{1, 1, 0}; //applicants can create and edit their enquiries
                proj_perms = new Integer[]{1, 0, 0, 1, 1}; //applicants can reply, filter and join projects
                break;
            case 1: //manager
                break;
            case 2: //officer
                break;
        }
    }

}