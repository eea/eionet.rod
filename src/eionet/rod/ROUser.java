/**
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 *
 * The Original Code is "EINRC-4 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Andre Karpistsenko (TietoEnator)
 */

package eionet.rod;

import com.tee.xmlserver.*;
import java.sql.*;
import java.util.Vector;

import eionet.directory.DirectoryService;

/**
 * <P>WebROD specific implementation of the <CODE>com.tee.xmlserver.AppUserIF</CODE> interface. 
 * Uses database to authenticate users.</P>
 *
 * @author  Rando Valt
 * @version 1.0
 */

public class ROUser implements AppUserIF {
   private boolean authented = false;
   private String user = null;
   private String password = null;
   private DBPoolIF dbPool = null;
   private String fullName = null;
   
   private String[] _roles = null;
   
   public ROUser() {
      dbPool = XDBApplication.getDBPool();
   }
/**
 *
 */
   public boolean authenticate(String userName, String userPws) {
      invalidate();

      // LOG
      if (Logger.enable(5))
         Logger.log("Authenticating user '" + userName + "'");
      //
      //Connection conn = null;
      //if (conn == null)
      //  conn = dbPool.getConnection();

      try {
         DirectoryService.sessionLogin(userName, userPws);
         fullName = DirectoryService.getFullName(userName);
         //KL021029
         //conn = dbPool.getConnection(userName, userPws);
         //conn = dbPool.getConnection();

         /*if (conn == null)
            return false; */
               
         // LOG
         if (Logger.enable(5))
            Logger.log("Authenticated!");
         //
         authented = true;
         user = userName;
         password = userPws;
               
      } catch (Exception e) {
      
         Logger.log("User '" + userName + "' not authenticated", e);
      }/* finally {
         try {
            if (conn != null) conn.close();
         } catch (SQLException e1) {} 
      } */
      return authented;
   }
/**
 *
 */
   public boolean isAuthentic() {
      return authented;
   }
/**
 *
 */
   public boolean isUserInRole(String role) {
      boolean b = false;

      if (_roles == null)
        getUserRoles();
        
      for (int i =0; i< _roles.length; i++)
        if ( _roles[i].equals(role))
          b = true;
          
      return b;
   }

/**
* FullName
*/
   public String getFullName() {
      return fullName;
   }

/**
 *
 */
   public String getUserName() {
      return user;
   }
/**
 *
 */
   public Connection getConnection() {
      //return dbPool.getConnection(user, password);
      return dbPool.getConnection();
   }
/**
 * Returns a string array of roles the user is linked to.
 * Note that the method returns newly constructed array, leaving internal role list unrevealed.
 */
   public String[] getUserRoles() {
      //String[] roles;
      if (_roles == null) {
        try {
          
          Vector v = DirectoryService.getRoles(user);
          String[] roles = new String[v.size()];
          for ( int i=0; i< v.size(); i++)
              _roles[i] = (String)v.elementAt(i);
          
          } catch ( Exception e ) {
            //return empty String, no need for roles
            _roles = new String[]{};
          }
       }
     
      //return new String[]{};
      return _roles;
   }
/**
 *
 */
   public void invalidate() {
      authented = false;
      user = null;
      password = null;
   }
/** 
 *
 */
   public String toString() {
      return (user == null ? "" : user );
      //return user;
   }

}