package org.Users.Applicant;


import org.Users.GenericManager;
import org.Users.user;


public class ApplicantManager extends GenericManager<user> {
   @Override
   protected Applicant parseUser(String line) {
       String[] details = line.split(",");
       if (details.length != 6) return null;


       int age = Integer.parseInt(details[4]);
       user.PermissionLevel perms = user.PermissionLevel.valueOf(details[5]);


       // CSV: userID, username, password, maritalStatus, age
       return new Applicant(details[0], details[1], details[2], details[3], age, perms);
   }


   @Override
   public void addUser(String userID, String username, int age, String maritalStatus, String unused) {
       if (age < 0) {
           System.out.println("Invalid age.");
           return;
       }
       user.PermissionLevel perms = user.PermissionLevel.APPLICANT;
       userDB.add(new Applicant(userID, username, "defaultPassword", maritalStatus, age, perms));
       System.out.println("Applicant added: " + userID);
   }
}
