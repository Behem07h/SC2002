package org.Users.HDBManager;


import org.Users.GenericManager;


public class ManagerController extends GenericManager<HDBManager> {
   @Override
   protected HDBManager parseUser(String line) {
       String[] details = line.split(",");
       if (details.length != 3) return null;


       // CSV: userID, username, password
       return new HDBManager(details[0], details[1], details[2], line, null, line, 0);
   }


   @Override
   public void addUser(String userID, String username, int age, String unused1, String unused2) {
       userDB.add(new HDBManager(userID, username, "defaultPassword", unused2, null, unused2, age));
       System.out.println("Manager added: " + userID);
   }
}
