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

import com.tee.util.*;
import com.tee.xmlserver.*;
import com.tee.uit.security.AccessControlListIF;

/**
 * <P>Handler to store WebROD legislative act data.</P>
 *
 * <P>Database tables updated: T_SOURCE, T_SOURCE_LNK. In case of
 * delete also related reporting obligations (with related activities) will be deleted.</P>
 *
 * @see ReportingHandler
 * @see ActivityHandler
 *
 * @author  Rando Valt, Andre Karpistsenko
 * @version 1.1
 */

public class SourceHandler extends ActivityHandler {

   private void DELETE_SOURCE(String srcID, boolean delSelf, boolean updateMode) {
      if ( delSelf)
        updateDB("DELETE FROM T_CLIENT_LNK WHERE TYPE='S' AND FK_OBJECT_ID=" + srcID);   
        
      updateDB("DELETE FROM T_SOURCE_LNK WHERE CHILD_TYPE='S' AND FK_SOURCE_CHILD_ID=" + srcID);
      if(!updateMode)
         updateDB("DELETE FROM T_SOURCE_LNK WHERE PARENT_TYPE='S' AND FK_SOURCE_PARENT_ID=" + srcID);

      if (delSelf) {
         // cascade delete related reporting obligations
         Statement stmt = null;
         ResultSet rs = null;
         String sqlStmt = "SELECT PK_RA_ID FROM T_OBLIGATION WHERE FK_SOURCE_ID=" + srcID;
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
               Logger.log("SourceHandler.DELETE_SOURCE", e1);
            }
         }
         // delete legislative act itself
         updateDB("DELETE FROM T_SOURCE WHERE PK_SOURCE_ID=" + srcID);
         HistoryLogger.logLegisgationHistory(srcID,this.user.getUserName(), DELETE_RECORD, "");          
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
        AccessControlListIF acl = servlet.getAcl(Constants.ACL_LI_NAME);
        upd = acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION);
        del = acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION);
        ins = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);      
      } catch (Exception e ) {
        return false;
      }

      if (tblName.equals("T_SOURCE")) {

          
         if (state != INSERT_RECORD) {
             
            gen.setPKField("PK_SOURCE_ID");
            id = gen.getFieldValue("PK_SOURCE_ID");
            // delete all linked parameter and medium records and in delete mode also the self record
		    updateDB("DELETE FROM T_CLIENT_LNK WHERE TYPE='S' AND STATUS = 'M' AND FK_OBJECT_ID=" + id);

            boolean delSelf = (state == DELETE_RECORD);

            if (delSelf && !del)
              return false;

            //if (!upd)              
            DELETE_SOURCE(id, delSelf, state == MODIFY_RECORD);

            if (delSelf == true) {
               //logHistory(gen);
               return false; // everything is done, stop
            }
         }
         else {
              if (!ins)
                return false;
              gen.removeField("PK_SOURCE_ID");
         }

         setDateValue(gen, "VALID_FROM");
         setDateValue(gen, "EC_ACCESSION");
         setDateValue(gen, "EC_ENTRY_INTO_FORCE");
         setDateValue(gen, "RM_NEXT_UPDATE");
         setDateValue(gen, "RM_VERIFIED");
         defaultProcessing(gen, null);
         id = recordID;

         String clientId=gen.getFieldValue("FK_CLIENT_ID");

          if ( clientId != null && !clientId.trim().equals(""))
            updateDB("INSERT INTO T_CLIENT_LNK (FK_CLIENT_ID, FK_OBJECT_ID, TYPE, STATUS) VALUES " +
               " ( " + clientId + "," + id + ", 'S', 'M')");


        logHistory(gen);
          
         if (servlet != null)
            servlet.setCurrentID(id);
      } 
      else if (tblName.equals("T_SOURCE_LNK")) {

         if ((state == INSERT_RECORD && !ins ) ||  (state == MODIFY_RECORD && !upd )      )
          return false;
          
         if ( Util.nullString(gen.getFieldValue("FK_SOURCE_PARENT_ID")) ) // no link information
            return true;
            
         gen.setField("CHILD_TYPE", "S");
         gen.setField("FK_SOURCE_CHILD_ID", id);
         if (context.indexOf("LnkParent") >= 0)
            gen.setField("PARENT_TYPE", "C");
         else
            gen.setField("PARENT_TYPE", "S");
            
         updateDB(gen.insertStatement());
      }
      else if (tblName.equals("T_LOOKUP")) 
         return false; // no need for further processing
      
      return true;
   }

   public SourceHandler(ROEditServletAC servlet) {
      super(servlet);
   }
   // constructor for testing
   SourceHandler(DBPoolIF dbPool, DBVendorIF dbVendor) {
      super(dbPool, dbVendor);
   }

   private void logHistory(SQLGenerator gen ) {
      HistoryLogger.logLegisgationHistory(id, this.user.getUserName(), gen.getState(), gen.getFieldValue("TITLE"));   
   }
   
}
