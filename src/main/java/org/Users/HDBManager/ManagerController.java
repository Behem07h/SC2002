package org.Users.HDBManager;


import org.Users.GenericManager;
import org.Users.user;


public class ManagerController extends GenericManager<user> {
   @Override
   protected HDBManager parseUser(String line) {
       String[] details = line.split(",");
       if (details.length != 7) return null;

       int age = Integer.parseInt(details[4]);
       user.PermissionLevel perms = user.PermissionLevel.valueOf(details[5]);

       // CSV: userID, username, password
       return new HDBManager(details[0], details[1], details[2], details[3], age, perms, details[6]);
   }


   @Override
   public void addUser(String userID, String username, int age, String maritalStatus, String unused2) {
       user.PermissionLevel perms = user.PermissionLevel.MANAGER;
       userDB.add(new HDBManager(userID, username, "password", maritalStatus, age, perms, ""));
       System.out.println("Manager added: " + userID);
   }
}
