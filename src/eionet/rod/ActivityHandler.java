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
 * <P>Handler to store WebROD activity data.</P>
 *
 * <P>Database tables updated: T_ACTIVITY, T_PARAMETER_LNK</P>
 *
 * @author  Andre Karpistsenko, Rando Valt
 * @version 1.1
 */

public class ActivityHandler extends ROHandler {
  private Vector paramCont = new Vector();

/**
 * Deletes activity related data and if delSelf is true, also the activity itself.
 */
   protected final void DELETE_ACTIVITY(String raID, boolean delSelf) {
      updateDB("DELETE FROM T_PARAMETER_LNK WHERE FK_RA_ID=" + raID);

      if (delSelf)
         updateDB("DELETE FROM T_ACTIVITY WHERE PK_RA_ID=" + raID);
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
        upd = servlet.getAcl().checkPermission(userName, "a");
        del = servlet.getAcl().checkPermission(userName, "X");
        ins = servlet.getAcl().checkPermission(userName, "A");      
      } catch (Exception e ) {
        return false;
      }



      if (tblName.equals("T_ACTIVITY")) {
         if (state != INSERT_RECORD) {

            if (!ins)
              return false;
              
            gen.setPKField("PK_RA_ID");
            id = gen.getFieldValue("PK_RA_ID");
            // delete all linked parameter records and in delete mode also the self record
            boolean delSelf = (state == DELETE_RECORD);

            if (delSelf && !del)
                return false;
            else if (!upd)
              return false;
              
            DELETE_ACTIVITY(id, delSelf);

            if (delSelf == true)
               return false; // everything is done, stop
         }
         else {

            if (!ins)
              return false;
              
            gen.removeField("PK_RA_ID");
         }

         String months = gen.getFieldValue("REPORT_FREQ_MONTHS");
         if(months.trim().length() == 0)
            gen.setFieldExpr("REPORT_FREQ_MONTHS", "NULL");
         setDateValue(gen, "VALID_SINCE");
         setDateValue(gen, "NEXT_DEADLINE");
         setDateValue(gen, "NEXT_DEADLINE_PLUS");
         setDateValue(gen, "FIRST_REPORTING");

         defaultProcessing(gen, null);
         id = recordID;

         if (servlet != null)
            servlet.setCurrentID(id);
      }
      else if (tblName.equals("T_PARAMETER_LNK")) {

        //check 
        if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd))
          paramCont.add(gen.clone());
        else 
          return false;
          
      }
      else if (tblName.equals("T_LOOKUP"))
         return false; // no need for further processing

      if ( (id != null) && (!paramCont.isEmpty()) ) {
         for (int i=0; i < paramCont.size(); i++) {
           SQLGenerator paramGen = (SQLGenerator)paramCont.get(i);
           String value = paramGen.getFieldValue("FK_PARAMETER_ID");

           paramGen.setField("FK_PARAMETER_ID", getID(value));
           paramGen.setField("FK_RA_ID", id);
           paramGen.setField("FK_UNIT_ID",getText(value));
           //paramGen.setField("PARAMETER_UNIT", getText(value));

           updateDB(paramGen.insertStatement());
         }
         paramCont.clear();
      }
      return true;
   }

   public ActivityHandler(ROEditServletAC servlet) {
      super(servlet);
   }
   // constructor for testing
   ActivityHandler(DBPoolIF dbPool, DBVendorIF dbVendor) {
      super(dbPool, dbVendor);
   }
}
