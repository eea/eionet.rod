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
import java.util.HashMap;

import com.tee.util.*;
import com.tee.xmlserver.*;
import com.tee.uit.security.AccessControlListIF;
import com.tee.uit.security.AccessController;
import com.tee.uit.security.SignOnException;

import eionet.rod.services.RODServices;

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
    
   private String source_id = "";

   private void DELETE_SOURCE(String srcID, boolean delSelf, boolean updateMode, String userName, long ts, int state) {
       
       String op = null;
       String acl_id = null;
       if(state == MODIFY_RECORD)
           op = "U";
       else if(state == DELETE_RECORD)
           op = "D";
       
       try{
          
           if(state == MODIFY_RECORD || state == DELETE_RECORD){
               RODServices.getDbService().insertTransactionInfo(srcID,"A","T_CLIENT_LNK","FK_OBJECT_ID",ts,"AND TYPE=''S''");
               RODServices.getDbService().insertTransactionInfo(srcID,"A","T_SOURCE_LNK","FK_SOURCE_CHILD_ID",ts,"AND CHILD_TYPE=''S''");
               RODServices.getDbService().insertTransactionInfo(srcID,"A","T_SOURCE_LNK","FK_SOURCE_PARENT_ID",ts,"AND PARENT_TYPE=''S''");
               RODServices.getDbService().insertTransactionInfo(srcID,"A","T_SOURCE","PK_SOURCE_ID",ts,"");
               /*if(state == DELETE_RECORD){
                   acl_id = RODServices.getDbService().getAclId(srcID,"/instruments");
                   RODServices.getDbService().insertTransactionInfo(acl_id,"A","ACLS","ACL_ID",ts,"");
                   RODServices.getDbService().insertTransactionInfo(acl_id,"A","ACL_ROWS","ACL_ID",ts,"");
               }*/
           }
          
          if ( delSelf){
            RODServices.getDbService().insertIntoUndo(srcID, op, "T_CLIENT_LNK", "FK_OBJECT_ID",ts,"AND TYPE='S'","y",null);
            updateDB("DELETE FROM T_CLIENT_LNK WHERE TYPE='S' AND FK_OBJECT_ID=" + srcID);
          }
          RODServices.getDbService().insertIntoUndo(srcID, op, "T_SOURCE_LNK", "FK_SOURCE_CHILD_ID",ts,"AND CHILD_TYPE='S'","y",null);
          updateDB("DELETE FROM T_SOURCE_LNK WHERE CHILD_TYPE='S' AND FK_SOURCE_CHILD_ID=" + srcID);
          if(!updateMode){
              RODServices.getDbService().insertIntoUndo(srcID, op, "T_SOURCE_LNK", "FK_SOURCE_PARENT_ID",ts,"AND PARENT_TYPE='S'","y",null);
              updateDB("DELETE FROM T_SOURCE_LNK WHERE PARENT_TYPE='S' AND FK_SOURCE_PARENT_ID=" + srcID);
          }
          if(state == DELETE_RECORD){
              RODServices.getDbService().insertIntoUndo(acl_id, op, "ACLS", "ACL_ID",ts,"","n",null);
              RODServices.getDbService().insertIntoUndo(acl_id, op, "ACL_ROWS", "ACL_ID",ts,"","n",null);
              try {
                  String aclPath = "/instruments/"+srcID;
                  AccessController.removeAcl(aclPath);
              } catch (SignOnException e){
                  e.printStackTrace();
              }
          }
          if (delSelf) {
             RODServices.getDbService().addObligationIdsIntoUndo(srcID,ts,"T_SOURCE");
              
             // cascade delete related reporting obligations
             Statement stmt = null;
             ResultSet rs = null;
             String sqlStmt = "SELECT PK_RA_ID FROM T_OBLIGATION WHERE FK_SOURCE_ID=" + srcID;
             try {
                stmt = conn.createStatement();
                rs = stmt.executeQuery(sqlStmt);
                int cnt = 1;
                while (rs.next()) {
                   DELETE_ACTIVITY(rs.getString(1), true, userName, ts+cnt,"n",DELETE_RECORD);
                   cnt++;
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
             RODServices.getDbService().insertIntoUndo(srcID, "D", "T_SOURCE", "PK_SOURCE_ID",ts,"","y",null);
             // delete legislative act itself
             updateDB("DELETE FROM T_SOURCE WHERE PK_SOURCE_ID=" + srcID);
             HistoryLogger.logLegisgationHistory(srcID,this.user.getUserName(), DELETE_RECORD, "");          
          }
       }catch (Exception e){
           e.printStackTrace();
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
      if (tblName.equals("T_SOURCE")) {
          if (state != INSERT_RECORD) {
              id = gen.getFieldValue("PK_SOURCE_ID");
          }
          source_id = id;
      }
      long ts = System.currentTimeMillis();

      String userName = this.user.getUserName();
      boolean ins = false, upd =false, del=false;
      try {
        String acl_p = Constants.ACL_LI_NAME + "/" +source_id;  
        AccessControlListIF acl = servlet.getAcl(acl_p);
        upd = acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION);
        del = acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION);
        ins = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);      
      } catch (Exception e ) {
        return false;
      }

      if (tblName.equals("T_SOURCE")) {
          
         if (state != INSERT_RECORD) {
             
            gen.setPKField("PK_SOURCE_ID");
            try{
                if(state == MODIFY_RECORD){
                    RODServices.getDbService().insertIntoUndo(id, "U", tblName, "PK_SOURCE_ID",ts,"","y",null);
                    RODServices.getDbService().insertIntoUndo(id, "U", "T_CLIENT_LNK", "FK_OBJECT_ID",ts,"AND TYPE='S'","y",null);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            
            String url = "show.jsv?id="+id+"&mode=S";
            
            updateDB("INSERT INTO T_UNDO VALUES ("+
                    ts + ",'"+ tblName +"','REDIRECT_URL','L','y','n','"+url+"',0,'n')");
            updateDB("INSERT INTO T_UNDO VALUES ("+
                    ts + ",'"+ tblName +"','A_USER','K','y','n','"+userName+"',0,'n')");
            updateDB("INSERT INTO T_UNDO VALUES ("+
                    ts + ",'"+ tblName +"','TYPE','T','y','n','L',0,'n')");
            if(state == DELETE_RECORD){
                String acl_path = "/instruments/"+id;
                updateDB("INSERT INTO T_UNDO VALUES ("+
                        ts + ",'"+ tblName +"','ACL','ACL','y','n','"+acl_path+"',0,'n')");
            }
            
            // delete all linked parameter and medium records and in delete mode also the self record
		    updateDB("DELETE FROM T_CLIENT_LNK WHERE TYPE='S' AND STATUS = 'M' AND FK_OBJECT_ID=" + id);

            boolean delSelf = (state == DELETE_RECORD);

            if (delSelf && !del)
              return false;

            //if (!upd)              
            DELETE_SOURCE(id, delSelf, state == MODIFY_RECORD, userName, ts,state);

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
         
         if(state == INSERT_RECORD){
             try {
                 String aclPath = "/instruments/"+source_id;
                 HashMap acls = AccessController.getAcls();
                 if (!acls.containsKey(aclPath)){
                     AccessController.addAcl(aclPath, userName, "");
                 }
             } catch (SignOnException e){
                 e.printStackTrace();
             }
         }
         
         /*if(state == INSERT_RECORD){
             try{
                 RODServices.getDbService().insertIntoUndo(id, "I", userName, tblName, "PK_SOURCE_ID",ts,"","y");
             }catch (Exception e){
                 e.printStackTrace();
             }
         }*/

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
