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

import java.sql.*;
import java.util.*;

import com.tee.util.*;
import com.tee.xmlserver.*;
import com.tee.uit.security.AccessControlListIF;

/**
 * <P>Handler to store WebROD obligation data.</P>
 *
 * <P>Database tables updated: T_REPORTING, T_SPATIAL_LNK, T_CLIENT_LNK. In case of delete also activities
 * (and the related) data will be deleted.</P>
 *
 * @see ActivityHandler
 *
 * @author  Andre Karpistsenko, Rando Valt
 * @version 1.1
 */

public class ReportingHandler extends ActivityHandler {
   //private Vector spatialCont = new Vector();
   private Vector clientCont = new Vector();

   protected final void DELETE_RO(String roID, boolean delSelf) {
      // cascade deleted related activitiess
      // delete linked spatial attributes
      //updateDB("DELETE FROM T_SPATIAL_LNK WHERE FK_RO_ID=" + roID);

      if (delSelf) {
      Statement stmt = null;
      ResultSet rs = null;
      String sqlStmt = "SELECT PK_RA_ID FROM T_ACTIVITY WHERE FK_RO_ID=" + roID;
      try {
         stmt = conn.createStatement();
         rs = stmt.executeQuery(sqlStmt);
         while (rs.next()) {
            DELETE_ACTIVITY(rs.getString(1), true);
         }
      } catch (SQLException e) {
         addErrorInfo(e, sqlStmt);
      } finally {
         try {
            if (rs != null)   rs.close();
            if (stmt != null) stmt.close();
         } catch (SQLException e1) {
            Logger.log("ReportingHandler.DELETE_RO", e1);
         }
      }
      // delete linked related information (eea reports, national submissions)
      updateDB("DELETE FROM T_INFORMATION WHERE FK_RO_ID=" + roID);
      // delete reporting obligation itself
      updateDB("DELETE FROM T_REPORTING WHERE PK_RO_ID=" + roID);

      //client_lnk
      updateDB("DELETE FROM T_CLIENT_LNK WHERE TYPE='R' AND FK_OBJECT_ID=" + roID);
      HistoryLogger.logObligationHistory(roID,this.user.getUserName(), DELETE_RECORD, ""); 
      }
   }

/**
 *
 */
   protected boolean sqlReady(SQLGenerator gen, String context) {
      // if error has occured in previous call, stop further processing
    

      if (getError())
         return false;

      String tblName = gen.getTableName();
      int state = gen.getState();

      String userName = this.user.getUserName();


      boolean ins = false, upd =false, del=false;
      try {
        AccessControlListIF acl = servlet.getAcl(Constants.ACL_RO_NAME);
        ins = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);              
        upd = acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION);


        //permission to delete RO || LI
        del = acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION) || servlet.getAcl(Constants.ACL_LI_NAME).checkPermission(userName, Constants.ACL_DELETE_PERMISSION);

      } catch (Exception e ) {
        System.out.println("============================= Error" + e.toString());  
        return false;
      }


      if (tblName.equals("T_REPORTING")) {

  		if (state != INSERT_RECORD) 
	        updateDB("DELETE FROM T_CLIENT_LNK WHERE FK_OBJECT_ID= " + gen.getFieldValue("PK_RO_ID")  + " AND " +
            " TYPE='R'");


      if (state != INSERT_RECORD) {
            gen.setPKField("PK_RO_ID");
            id = gen.getFieldValue("PK_RO_ID");
            // delete all linked data and in delete mode also the self record
            boolean delSelf = (state == DELETE_RECORD);

            if (delSelf && !del)
              return false;

              
            DELETE_RO(id, delSelf);

            if (delSelf == true) {
               //logHistory(gen);
               return false; // everything is done, stop
            }

            if(!upd)
              return false;
         
         }
         else {

            if (!ins)
              return false;
              
            gen.removeField("PK_RO_ID");
         }
         
         setDateValue(gen, "RM_NEXT_UPDATE");
         setDateValue(gen, "RM_VERIFIED");

         String mainClientId=gen.getFieldValue("FK_CLIENT_ID");
        //System.out.println("========= u " + gen.updateStatement());
         defaultProcessing(gen, null);
          
         id = recordID;
  	 if (state != DELETE_RECORD && !gen.getFieldValue("FK_CLIENT_ID").equals("0"))
    		updateDB("INSERT INTO T_CLIENT_LNK (FK_CLIENT_ID, FK_OBJECT_ID, STATUS, TYPE) " +
		        " VALUES ( " + gen.getFieldValue("FK_CLIENT_ID") + ", " + id +
			    ", 'M', 'R')");     

        //log history
        logHistory(gen);
         
         if (servlet != null)
            servlet.setCurrentID(id);

            
      }
      else if (tblName.equals("T_CLIENT_LNK")) {
        if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd))      
          clientCont.add(gen.clone());
        else
          return false;

      }
      else if (tblName.equals("T_LOOKUP"))
         return false; // no need for further processing

      //client lnk
      if ( (id != null) && (!clientCont.isEmpty()) ) {
      
         for (int i=0; i < clientCont.size(); i++) {
           SQLGenerator clientGen = (SQLGenerator)clientCont.get(i);
           String value = clientGen.getFieldValue("FK_CLIENT_ID");
           //String id = getID(value);
           
           clientGen.setField("FK_CLIENT_ID", value.substring( value.indexOf(":") +1));
           clientGen.setField("FK_OBJECT_ID", id);
           clientGen.setField("STATUS", "C");
           clientGen.setField("TYPE", "R");           

           updateDB(clientGen.insertStatement());
         }
         clientCont.clear();
      }
      
      return true;
   }

   public ReportingHandler(ROEditServletAC servlet) {
      super(servlet);
   }
   // constructor for testing
   ReportingHandler(DBPoolIF dbPool, DBVendorIF dbVendor) {
      super(dbPool, dbVendor);
   }

   private void logHistory(SQLGenerator gen ) {
    HistoryLogger.logObligationHistory(id, this.user.getUserName(), gen.getState(), gen.getFieldValue("ALIAS"));   
   }
}
