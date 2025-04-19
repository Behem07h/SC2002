package org.Users.Applicant;


import org.Users.GenericManager;
import org.Users.user;


public class ApplicantManager extends GenericManager<user> {
   @Override
   protected Applicant parseUser(String line) {
       System.out.println("Parse users");
       String[] details = line.split(",");
       if (details.length != 6) return null;


       int age = Integer.parseInt(details[4]);
       Applicant.PermissionLevel perms = Applicant.PermissionLevel.valueOf(details[5]);


       // CSV: userID, username, password, maritalStatus, age
       return new Applicant(details[0], details[1], details[2], details[3], age, perms);
   }


   @Override
   public void addUser(String userID, String username, int age, String maritalStatus, String unused) {
       if (age < 0) {
           System.out.println("Invalid age.");
           return;
       }
       Applicant.PermissionLevel perms = Applicant.PermissionLevel.NONE;
       userDB.add(new Applicant(userID, username, "defaultPassword", maritalStatus, age, perms));
       System.out.println("Applicant added: " + userID);
   }
}
