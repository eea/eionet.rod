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

/**
 * <P>Handler to store WebROD obligation data.</P>
 *
 * <P>Database tables updated: T_REPORTING, T_ISSUE_LNK, T_SPATIAL_LNK. In case of delete also activities
 * (and the related) data will be deleted.</P>
 *
 * @see ActivityHandler
 *
 * @author  Andre Karpistsenko, Rando Valt
 * @version 1.1
 */

public class ReportingHandler extends ActivityHandler {
   private Vector issueCont = new Vector();
   private Vector spatialCont = new Vector();

   protected final void DELETE_RO(String roID, boolean delSelf) {
      // cascade deleted related activitiess
      // delete linked spatial attributes
      updateDB("DELETE FROM T_SPATIAL_LNK WHERE FK_RO_ID=" + roID);
      // delete linked environmental issues
      updateDB("DELETE FROM T_ISSUE_LNK WHERE FK_RO_ID=" + roID);

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

      if (tblName.equals("T_REPORTING")) {
         if (state != INSERT_RECORD) {
            gen.setPKField("PK_RO_ID");
            id = gen.getFieldValue("PK_RO_ID");
            // delete all linked data and in delete mode also the self record
            boolean delSelf = (state == DELETE_RECORD);
            DELETE_RO(id, delSelf);

            if (delSelf == true)
               return false; // everything is done, stop
         }
         else {
            gen.removeField("PK_RO_ID");
         }
         
         setDateValue(gen, "VALID_FROM");
         defaultProcessing(gen, null);
         id = recordID;
         
         if (servlet != null)
            servlet.setCurrentID(id);
      }
      else if (tblName.equals("T_ISSUE_LNK")) {
        issueCont.add(gen.clone());
      }
      else if (tblName.equals("T_SPATIAL_LNK")) {
        spatialCont.add(gen.clone());
      }
      else if (tblName.equals("T_LOOKUP"))
         return false; // no need for further processing

      if ( (id != null) && (!issueCont.isEmpty()) ) {
         for (int i=0; i < issueCont.size(); i++) {
           SQLGenerator issueGen = (SQLGenerator)issueCont.get(i);
           String value = issueGen.getFieldValue("FK_ISSUE_ID");

           issueGen.setField("FK_ISSUE_ID", getID(value));
           issueGen.setField("FK_RO_ID", id);

           updateDB(issueGen.insertStatement());
         }
         issueCont.clear();
      }
      if ( (id != null) && (!spatialCont.isEmpty()) ) {
         for (int i=0; i < spatialCont.size(); i++) {
           SQLGenerator spatialGen = (SQLGenerator)spatialCont.get(i);
           String value = spatialGen.getFieldValue("FK_SPATIAL_ID");

           spatialGen.setField("FK_SPATIAL_ID", getID(value));
           spatialGen.setField("FK_RO_ID", id);

           updateDB(spatialGen.insertStatement());
         }
         spatialCont.clear();
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
}
