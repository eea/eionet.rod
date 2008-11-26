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
import java.util.Iterator;
import java.util.List;

import com.tee.util.*;
import com.tee.xmlserver.*;
import com.tee.uit.security.AccessControlListIF;
import com.tee.uit.security.AccessController;
import com.tee.uit.security.SignOnException;

import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IClientDao;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.services.modules.db.dao.ISourceDao;
import eionet.rod.services.modules.db.dao.IUndoDao;

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
   private IClientDao clientDao = null;
   private IUndoDao undoDao = null;
   private ISourceDao sourceDao = null;
   private IObligationDao obligationDao = null;
   
   private boolean _wasInstrumentUpdate = false;
   private boolean _wasInstrumentInsert = false;
   private long _ts = 0;
   private SQLGenerator _getSQLGen = null;
	
   private void DELETE_SOURCE(String srcID, boolean delSelf, boolean updateMode, String userName, long ts, int state) {
       
       String op = null;
       String acl_id = null;
       if(state == MODIFY_RECORD)
           op = "U";
       else if(state == DELETE_RECORD)
           op = "D";
       
       try{
          
           if(state == MODIFY_RECORD || state == DELETE_RECORD){
//          	 !!!!!!!!!! in old version single quotes was escaped in where clause
        	   undoDao.insertTransactionInfo(srcID,"A","T_CLIENT_LNK","FK_OBJECT_ID",ts,"AND TYPE='S'");
        	   undoDao.insertTransactionInfo(srcID,"A","T_SOURCE_LNK","FK_SOURCE_CHILD_ID",ts,"AND CHILD_TYPE='S'");
        	   undoDao.insertTransactionInfo(srcID,"A","T_SOURCE_LNK","FK_SOURCE_PARENT_ID",ts,"AND PARENT_TYPE='S'");
        	   undoDao.insertTransactionInfo(srcID,"A","T_SOURCE","PK_SOURCE_ID",ts,"");
               /*if(state == DELETE_RECORD){
                   acl_id = RODServices.getDbService().getAclId(srcID,"/instruments");
                   RODServices.getDbService().insertTransactionInfo(acl_id,"A","ACLS","ACL_ID",ts,"");
                   RODServices.getDbService().insertTransactionInfo(acl_id,"A","ACL_ROWS","ACL_ID",ts,"");
               }*/
           }
          
          if ( delSelf){
            undoDao.insertIntoUndo(null,srcID, op, "T_CLIENT_LNK", "FK_OBJECT_ID",ts,"AND TYPE='S'","y",null);
            clientDao.deleteSourceLink(Integer.valueOf(srcID));
          }
          undoDao.insertIntoUndo(null,srcID, op, "T_SOURCE_LNK", "FK_SOURCE_CHILD_ID",ts,"AND CHILD_TYPE='S'","y",null);
          sourceDao.deleteChildLink(Integer.valueOf(srcID));
          if(!updateMode){
              undoDao.insertIntoUndo(null,srcID, op, "T_SOURCE_LNK", "FK_SOURCE_PARENT_ID",ts,"AND PARENT_TYPE='S'","y",null);
              sourceDao.deleteParentLink(Integer.valueOf(srcID));
          }
          if(state == DELETE_RECORD){
              undoDao.insertIntoUndo(null,acl_id, op, "ACLS", "ACL_ID",ts,"","n",null);
              undoDao.insertIntoUndo(null,acl_id, op, "ACL_ROWS", "ACL_ID",ts,"","n",null);
              try {
                  String aclPath = "/instruments/"+srcID;
                  AccessController.removeAcl(aclPath);
              } catch (SignOnException e){
                  e.printStackTrace();
              }
          }
          if (delSelf) {
             undoDao.addObligationIdsIntoUndo(Integer.valueOf(srcID),ts,"T_SOURCE");
              
             // cascade delete related reporting obligations
             
             List sourceObligations = obligationDao.getObligationsBySource(Integer.valueOf(srcID));
             int cnt = 1;
             Iterator soIterator = sourceObligations.iterator();
             while (soIterator.hasNext()) {
				String obligationId = (String) soIterator.next();
			    DELETE_ACTIVITY(obligationId, true, userName, ts+cnt,"n",DELETE_RECORD);
			    cnt++;				
			}             
            undoDao.insertIntoUndo(null,srcID, "D", "T_SOURCE", "PK_SOURCE_ID",ts,"","y",null);
            // delete legislative act itself
            sourceDao.deleteSource(Integer.valueOf(srcID));
          }
       }catch (Exception e){
           Logger.log("SourceHandler.DELETE_SOURCE", e);
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

     try {
      String tblName = gen.getTableName();
      int state = gen.getState();
      
      long ts = System.currentTimeMillis();
      
      if (tblName.equals("T_SOURCE")) {
          if (state != INSERT_RECORD) {
              id = gen.getFieldValue("PK_SOURCE_ID");
          }
          source_id = id;
          _ts = ts;
      }
      

      String userName = this.user.getUserName();
      boolean ins = false, upd =false, del=false;
      try {
        String acl_p = (source_id == null || source_id == "")? Constants.ACL_LI_NAME : (Constants.ACL_LI_NAME + "/" +source_id);
        AccessControlListIF acl = AccessController.getAcl(acl_p);
        upd = acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION);
        del = acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION);
        ins = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);      
      } catch (Exception e ) {
        return false;
      }

      if (tblName.equals("T_SOURCE")) {
          
         if (state != INSERT_RECORD) {
             
            gen.setPKField("PK_SOURCE_ID");
                if(state == MODIFY_RECORD){
		            undoDao.insertIntoUndo(null,id, "U", tblName, "PK_SOURCE_ID",ts,"","y",null);
		            undoDao.insertIntoUndo(null,id, "U", "T_CLIENT_LNK", "FK_OBJECT_ID",ts,"AND TYPE='S'","y",null);
                }
            
            String url = "instruments/"+id;
            
			undoDao.insertIntoUndo(ts,tblName,"REDIRECT_URL","L","y","n",url,0,"n");
			undoDao.insertIntoUndo(ts,tblName,"A_USER","K","y","n",userName,0,"n");
			undoDao.insertIntoUndo(ts,tblName,"TYPE","T","y","n","L",0,"n");
            if(state == DELETE_RECORD){
                String acl_path = "/instruments/"+id;
                undoDao.insertIntoUndo(ts,tblName,"ACL","ACL","y","n",acl_path,0,"n");
            }
            
            // delete all linked parameter and medium records and in delete mode also the self record
			clientDao.deleteParameterLink(Integer.valueOf(id));

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
                 String aclPath = "/instruments/"+id;
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
		    	  clientDao.insertClientLink(Integer.valueOf(clientId), Integer.valueOf(id), "M","S");
          
         if (servlet != null)
            servlet.setCurrentID(id);
         
         if (state == MODIFY_RECORD) {
             _wasInstrumentUpdate = true;
             _getSQLGen = (SQLGenerator) gen.clone();
             
         } else if (state == INSERT_RECORD) {
             _wasInstrumentInsert = true;
             _getSQLGen = (SQLGenerator) gen.clone();
         }
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
       }catch(Exception e){logger.error(e);}
      return true;
   }
   
   /*
    * 
    */
   public boolean wasInstrumentUpdate(){
       
       return _wasInstrumentUpdate;
   }
   
   /*
    * 
    */
   public boolean wasInstrumentInsert(){
       
       return _wasInstrumentInsert;
   }
   
   /*
    * 
    */
   public long tsValue(){
       
       return _ts;
   }
   
   /*
    * 
    */
   public SQLGenerator getSQLGen(){
       
       return _getSQLGen;
   }

   public SourceHandler(ROEditServletAC servlet) {
      super(servlet);
      initDao();
   }
   // constructor for testing
   SourceHandler(DBPoolIF dbPool, DBVendorIF dbVendor) {
      super(dbPool, dbVendor);
      initDao();
   }

   private void initDao(){
		try {
			undoDao = RODServices.getDbService().getUndoDao();
			obligationDao = RODServices.getDbService().getObligationDao();
			clientDao = RODServices.getDbService().getClientDao();
			sourceDao = RODServices.getDbService().getSourceDao();			
		} catch (ServiceException e) {
			logger.fatal(e);
		}
		
	}   
   
}
