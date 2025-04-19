package org.Users.HDBOfficer;


import org.Users.GenericManager;


public class HDBOfficerManager extends GenericManager<HDBOfficer> {
   @Override
   protected HDBOfficer parseUser(String line) {
       String[] details = line.split(",");
       if (details.length != 3) return null;


       // CSV: userID, username, password
       return new HDBOfficer(details[0], details[1], details[2], line, 0, null);
   }


   @Override
   public void addUser(String userID, String username, int age, String unused1, String unused2) {
       userDB.add(new HDBOfficer(userID, username, "defaultPassword", unused2, age, null));
       System.out.println("HDB Officer added: " + userID);
   }
}
