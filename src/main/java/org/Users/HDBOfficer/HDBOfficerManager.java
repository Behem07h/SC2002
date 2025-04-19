package org.Users.HDBOfficer;


import org.Users.Applicant.Applicant;
import org.Users.GenericManager;
import org.Users.user;


public class HDBOfficerManager extends GenericManager<user> {
   @Override
   protected HDBOfficer parseUser(String line) {
       String[] details = line.split(",");
       if (details.length != 6) return null;


       int age = Integer.parseInt(details[4]);
       user.PermissionLevel perms = user.PermissionLevel.valueOf(details[5]);


       // CSV: userID, username, password, maritalStatus, age
       return new HDBOfficer(details[0], details[1], details[2], details[3], age, perms);
   }


   @Override
   public void addUser(String userID, String username, int age, String maritalStatus, String unused2) {
       user.PermissionLevel perms = user.PermissionLevel.OFFICER;
       userDB.add(new HDBOfficer(userID, username, "password", maritalStatus, age, perms));
       System.out.println("HDB Officer added: " + userID);
   }
}
