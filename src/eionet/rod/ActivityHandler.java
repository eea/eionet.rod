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
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.services.DbServiceIF;
import com.tee.uit.security.AccessControlListIF;
import eionet.directory.DirectoryService;
import eionet.directory.DirServiceException;

/**
 * <P>Handler to store WebROD activity data.</P>
 *
 * <P>Database tables updated: T_ACTIVITY, T_PARAMETER_LNK</P>
 *
 * @author  Andre Karpistsenko, Rando Valt
 * @version 1.1
 */

public class ActivityHandler extends ROHandler {
  private Vector issueCont = new Vector();
  private Vector paramCont = new Vector();
  private Vector spatialCont = new Vector();  
  //private Vector spatialHistCont = new Vector();

/**
 * Deletes activity related data and if delSelf is true, also the activity itself.
 */
   protected final void DELETE_ACTIVITY(String raID, boolean delSelf) {
      // delete linked environmental issues & parameters
      updateDB("DELETE FROM T_RAISSUE_LNK WHERE FK_RA_ID=" + raID);
//??
      updateDB("DELETE FROM T_RASPATIAL_LNK WHERE FK_RA_ID=" + raID);      
      
      updateDB("DELETE FROM T_PARAMETER_LNK WHERE FK_RA_ID=" + raID);
      updateDB("UPDATE T_SPATIAL_HISTORY SET END_DATE=NOW() WHERE " +
        " END_DATE IS NULL AND FK_RA_ID=" + raID);

      /*try {
        spatialHistCont=RODServices.getDbService().backupSpatialHistory(raID);
      } catch (ServiceException se ) {
        Logger.log("Error " + se.toString());
      } */


      
      if (delSelf) {

         //KL031014
         //updateDB("DELETE FROM T_SPATIAL_HISTORY WHERE FK_RA_ID=" + raID);
      
         updateDB("DELETE FROM T_ACTIVITY WHERE PK_RA_ID=" + raID);
         HistoryLogger.logActivityHistory(raID,this.user.getUserName(), DELETE_RECORD, "");                       
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
        AccessControlListIF acl = servlet.getAcl(Constants.ACL_RA_NAME);

        upd = acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION);
        
        //special case for delete, because user, having permission to delete RO or 
        //LI must be able to delete RA's as well
        del = acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION) || servlet.getAcl(Constants.ACL_RO_NAME).checkPermission(userName, Constants.ACL_DELETE_PERMISSION) || servlet.getAcl(Constants.ACL_LI_NAME).checkPermission(userName, Constants.ACL_DELETE_PERMISSION) ;
        ins = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);
      } catch (Exception e ) {
        return false;
      }


      //Logger.log("state " + state);
      if (tblName.equals("T_ACTIVITY")) {
         if (state != INSERT_RECORD) {

            if (!upd)
              return false;
              
            gen.setPKField("PK_RA_ID");
            id = gen.getFieldValue("PK_RA_ID");

            //Get role info from Directory and save in T_ROLE
            //KL 030206
            String roleId = gen.getFieldValue("RESPONSIBLE_ROLE");
            if (!Util.nullString(roleId)) 
              try {
                Hashtable role = DirectoryService.getRole(roleId);
                RODServices.getDbService().saveRole(role);
              } catch (DirServiceException de ) {
                Logger.log("Error getting role infor for: " + roleId);
              } catch (ServiceException se ) {
                Logger.log("Error saving role info for: " + roleId);
              }

              
            // delete all linked parameter records and in delete mode also the self record
            boolean delSelf = (state == DELETE_RECORD);

            if (delSelf && !del)
                return false;
            else if (!upd)
              return false;

            DELETE_ACTIVITY(id, delSelf);

            if (delSelf == true) 
               return false; // everything is done, log + stop
           
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
         setDateValue(gen, "VALID_TO");
         setDateValue(gen, "NEXT_DEADLINE");
         setDateValue(gen, "FIRST_REPORTING");
         setDateValue(gen, "NEXT_DEADLINE2");
         setDateValue(gen, "RM_NEXT_UPDATE");
         setDateValue(gen, "RM_VERIFIED");
         gen.setFieldExpr("LAST_UPDATE", "CURDATE()");

         defaultProcessing(gen, null);
         id = recordID;

         HistoryLogger.logActivityHistory(id,this.user.getUserName(), state, gen.getFieldValue("TITLE"));

         if (servlet != null)
            servlet.setCurrentID(id);
      }
      else if (tblName.equals("T_RAISSUE_LNK")) {
       if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd))      
          issueCont.add(gen.clone());
        else
          return false;
      }
      else if (tblName.equals("T_RASPATIAL_LNK")) {
       if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd))      
          spatialCont.add(gen.clone());
       else
        return false;
      }

      //KL 031015
      /*else if (tblName.equals("T_SPATIAL_HISTORY")) {
       if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd))      
          spatialHistCont.add(gen.getFieldValue("FK_SPATIAL_ID"));
       else
        return false;
      } */


      else if (tblName.equals("T_PARAMETER_LNK")) {

        //check 
        if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd))
          paramCont.add(gen.clone());
        else 
          return false;
          
      }
      else if (tblName.equals("T_LOOKUP"))
         return false; // no need for further processing


    if ( (id != null) && (!spatialCont.isEmpty()) ) {
         for (int i=0; i < spatialCont.size(); i++) {
           SQLGenerator spatialGen = (SQLGenerator)spatialCont.get(i);
           String value = spatialGen.getFieldValue("FK_SPATIAL_ID");

           spatialGen.setField("FK_RA_ID", id);

           String spatialId, voluntary; // spatialType;

           if(value.indexOf(":") == -1) {
             updateDB("DELETE FROM T_RASPATIAL_LNK WHERE FK_RA_ID=" + id + " AND FK_SPATIAL_ID=" + getID(value));
             spatialId=getID(value);
             //spatialType=getUnitID(value);
             voluntary="N";

           }
           else {
            voluntary="Y";
            spatialId=value.substring(value.indexOf(":")+1);
            //spatialType="C";
           }
           spatialGen.setField("FK_SPATIAL_ID", spatialId);
           spatialGen.setField("VOLUNTARY", voluntary);
  
          //System.out.println("SQL=" + spatialGen.insertStatement());

           updateDB(spatialGen.insertStatement());

            //handle history
            //if (spatialType.equals("C"))
            HistoryLogger.logSpatialHistory(id, spatialId, voluntary);

           
         }

         spatialCont.clear();

      }

      if ( (id != null) && (!issueCont.isEmpty()) ) {
         for (int i=0; i < issueCont.size(); i++) {
           SQLGenerator issueGen = (SQLGenerator)issueCont.get(i);
           String value = issueGen.getFieldValue("FK_ISSUE_ID");

           issueGen.setField("FK_ISSUE_ID", getID(value));
           issueGen.setField("FK_RA_ID", id);

           updateDB(issueGen.insertStatement());
         }
         issueCont.clear();
      }
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

//System.out.println("============= FINAL SPATIALHISTCONT SIZE= " + spatialHistCont.size());         
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
