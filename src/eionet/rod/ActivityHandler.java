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

import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tee.uit.security.AccessControlListIF;
import com.tee.uit.security.AccessController;
import com.tee.uit.security.SignOnException;
import com.tee.util.SQLGenerator;
import com.tee.xmlserver.DBPoolIF;
import com.tee.xmlserver.DBVendorIF;

import eionet.rod.countrysrv.Extractor;
import eionet.rod.services.RODServices;
import eionet.rod.services.LogServiceIF;
import eionet.rod.services.ServiceException;
import eionet.rod.services.modules.db.dao.IClientDao;
import eionet.rod.services.modules.db.dao.IHistoricDeadlineDao;
import eionet.rod.services.modules.db.dao.IObligationDao;
import eionet.rod.services.modules.db.dao.ISpatialHistoryDao;
import eionet.rod.services.modules.db.dao.IUndoDao;

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
  private Vector clientCont = new Vector();
  private String obligation_id = "";

  private Extractor ext; //used for role handling
  private IUndoDao undoDao = null;
  private IObligationDao obligationDao = null;
  private IHistoricDeadlineDao historicDeadlineDao = null;
  private IClientDao clientDao = null;
  private ISpatialHistoryDao spatialHistoryDao = null;
  protected static LogServiceIF logger = RODServices.getLogService();
  
  private boolean _wasObligationUpdate = false;
  private boolean _wasObligationInsert = false;
  private long _ts = 0;
  private String _id = null;
  private SQLGenerator _getSQLGen = null;
    
  //private Vector spatialHistCont = new Vector();

/**
 * Deletes activity related data and if delSelf is true, also the activity itself.
 */
   protected final void DELETE_ACTIVITY(String raID, boolean delSelf, String userName, long ts, String show,int state) {
      
      String op = null;
      String acl_id = null;
      if(state == MODIFY_RECORD)
          op = "U";
      else if(state == DELETE_RECORD)
          op = "D";
      
      // store in T_UNDO 
      try{
          if(state == MODIFY_RECORD || state == DELETE_RECORD){
				undoDao.insertTransactionInfo(raID, "A", "T_RAISSUE_LNK", "FK_RA_ID", ts, "");
				undoDao.insertTransactionInfo(raID, "A", "T_RASPATIAL_LNK", "FK_RA_ID", ts, "");
				undoDao.insertTransactionInfo(raID, "A", "T_INFO_LNK", "FK_RA_ID", ts, "");
				// !!!!!!!!!! in old version single quotes was escaped in where clause
				undoDao.insertTransactionInfo(raID, "A", "T_CLIENT_LNK", "FK_OBJECT_ID", ts, "AND TYPE='A'");
				undoDao.insertTransactionInfo(raID, "A", "T_OBLIGATION", "PK_RA_ID", ts, "");
				undoDao.insertTransactionInfo(raID, "A", "T_HISTORIC_DEADLINES", "FK_RA_ID", ts, "");
			}
			Integer obligationID = Integer.valueOf(raID); 
			undoDao.insertIntoUndo(null, raID, op, "T_RAISSUE_LNK", "FK_RA_ID", ts, "", show, null);
          // delete linked environmental issues & parameters
			obligationDao.deleteIssueLink(obligationID);
			undoDao.insertIntoUndo(null, raID, op, "T_RASPATIAL_LNK", "FK_RA_ID", ts, "", show, null);
			obligationDao.deleteSpatialLink(obligationID);
			// updateDB("DELETE FROM T_PARAMETER_LNK WHERE FK_RA_ID=" + raID);
			undoDao.insertIntoUndo(null, raID, op, "T_INFO_LNK", "FK_RA_ID", ts, "", show, null);
			obligationDao.deleteInfoLink(obligationID);
			// delete linked historical deadlines
			undoDao.insertIntoUndo(null, raID, op, "T_HISTORIC_DEADLINES", "FK_RA_ID", ts, "", show, null);
			historicDeadlineDao.deleteByObligationId(obligationID);

          if(state == DELETE_RECORD){
              undoDao.insertIntoUndo(null,acl_id, op, "ACLS", "ACL_ID",ts,"","n",null);
              undoDao.insertIntoUndo(null,acl_id, op, "ACL_ROWS", "ACL_ID",ts,"","n",null);
              try {
                  String aclPath = "/obligations/"+raID;
                  AccessController.removeAcl(aclPath);
              } catch (SignOnException e){
                  e.printStackTrace();
              }
          }
    
          // delete linked related information (eea reports, national submissions)
          //at the moment T_INFORMATION is not used
          //updateDB("DELETE FROM T_INFORMATION WHERE FK_RO_ID=" + raID);
    
          //client_lnk
			undoDao.insertIntoUndo(null, raID, op, "T_CLIENT_LNK", "FK_OBJECT_ID", ts, "AND TYPE='A'", show, null);
			clientDao.deleteObligationLink(obligationID);

			spatialHistoryDao.updateEndDateForObligation(obligationID);

          if (delSelf) {
				undoDao.insertIntoUndo(null, raID, "D", "T_OBLIGATION", "PK_RA_ID", ts, "", show, null);
				obligationDao.deleteObligation(obligationID);
				HistoryLogger.logActivityHistory(raID, this.user.getUserName(), DELETE_RECORD, "");
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
    try{
      if (getError())
         return false;

      String tblName = gen.getTableName();
      int state = gen.getState();
      
      long ts = System.currentTimeMillis();
      
      if (tblName.equals("T_OBLIGATION")) {
          if (state != INSERT_RECORD) {
              id = gen.getFieldValue("PK_RA_ID");
          }
          obligation_id = id;
          _ts = ts;
          _id = obligation_id;
      }
      
      String userName = this.user.getUserName();
      boolean ins = false, upd =false, del=false;
      
      try {
        String acl_p = (obligation_id == null || obligation_id == "")? Constants.ACL_RA_NAME : (Constants.ACL_RA_NAME + "/" +obligation_id);
		AccessControlListIF acl = AccessController.getAcl(acl_p);
        
        upd = acl.checkPermission(userName, Constants.ACL_UPDATE_PERMISSION);
        
        //special case for delete, because user, having permission to delete RO or 
        //LI must be able to delete RA's as well
        del = acl.checkPermission(userName, Constants.ACL_DELETE_PERMISSION) || servlet.getAcl(Constants.ACL_RO_NAME).checkPermission(userName, Constants.ACL_DELETE_PERMISSION) || servlet.getAcl(Constants.ACL_LI_NAME).checkPermission(userName, Constants.ACL_DELETE_PERMISSION) ;
        ins = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);
      } catch (Exception e ) {
        return false;
      }


      //Logger.log("state " + state);
      if (tblName.equals("T_OBLIGATION")) {
          
         if (state != INSERT_RECORD) {

            if (!upd)
              return false;
            
            gen.setPKField("PK_RA_ID");
            //id = gen.getFieldValue("PK_RA_ID");
            try{
                if(state == MODIFY_RECORD){
						undoDao.insertIntoUndo(null, id, "U", tblName, "PK_RA_ID", ts, "", "y", null);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            
            String aid = gen.getFieldValue("FK_SOURCE_ID");
            
            String url = "show.jsv?id="+id+"&aid="+aid+"&mode=A";
           
			undoDao.insertIntoUndo(ts,tblName,"REDIRECT_URL","L","y","n",url,0,"n");
			undoDao.insertIntoUndo(ts,tblName,"A_USER","K","y","n",userName,0,"n");
			undoDao.insertIntoUndo(ts,tblName,"TYPE","T","y","n","A",0,"n");
            
            if(state == DELETE_RECORD){
                String acl_path = "/obligations/"+id;
                undoDao.insertIntoUndo(ts,tblName,"ACL","ACL","y","n",acl_path,0,"n");
            }
            
            if (ext==null)
              ext = new Extractor();
              
            //Get role info from Directory and save in T_ROLE
            String roleId = gen.getFieldValue("RESPONSIBLE_ROLE");
            try {
              ext.saveRole(roleId);
              roleId = gen.getFieldValue("COORDINATOR_ROLE");              
              ext.saveRole(roleId);              
            } catch (Exception e ) {
              //don't worry about the role saving if something wrong
            }

            // delete all linked parameter records and in delete mode also the self record
            boolean delSelf = (state == DELETE_RECORD);

            if (delSelf && !del)
                return false;
            else if (!upd)
              return false;

            DELETE_ACTIVITY(id, delSelf, userName, ts,"y",state);

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
         
         if(state == INSERT_RECORD){
             try {
                 String aclPath = "/obligations/"+id;
                 HashMap acls = AccessController.getAcls();
                 if (!acls.containsKey(aclPath)){
                     AccessController.addAcl(aclPath, userName, "");
                 }
             } catch (SignOnException e){
                 e.printStackTrace();
             }
         }
         
         String mainClientId=gen.getFieldValue("FK_CLIENT_ID");

         //main client to T_CLIENT_LNK
      	 if (state != DELETE_RECORD && !gen.getFieldValue("FK_CLIENT_ID").equals("0"))
      		 	clientDao.insertClientLink(Integer.valueOf(gen.getFieldValue("FK_CLIENT_ID")),Integer.valueOf(id),"M","A");

         HistoryLogger.logActivityHistory(id,this.user.getUserName(), state, gen.getFieldValue("TITLE"));

         if (servlet != null)
            servlet.setCurrentID(id);
      }
      else if (tblName.equals("T_RAISSUE_LNK")) {
       if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd))  {
           issueCont.add(gen.clone());
       }
        else
          return false;
      }
      else if (tblName.equals("T_RASPATIAL_LNK")) {
       if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd)){
           spatialCont.add(gen.clone());
       }
       else
        return false;
      }


      else if (tblName.equals("T_CLIENT_LNK")) {
         if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd)){
             clientCont.add(gen.clone());
         }
         else
          return false;
      }

      /*else if (tblName.equals("T_PARAMETER_LNK")) {

        //check 
        if (( state == INSERT_RECORD && ins) || ( state == MODIFY_RECORD && upd))
          paramCont.add(gen.clone());
        else 
          return false;
          
      } */
      else if (tblName.equals("T_LOOKUP"))
         return false; // no need for further processing

      else if (tblName.equals("T_LOOKUP"))  
         return false; // no need for further processing


      else if (tblName.equals("T_INFO_LNK")) {
          processInfoType( id, gen.getFieldValue("FK_INFO_IDS") );
      }
         


    if ( (id != null) && (!spatialCont.isEmpty()) ) {
         for (int i=0; i < spatialCont.size(); i++) {
           SQLGenerator spatialGen = (SQLGenerator)spatialCont.get(i);
           String value = spatialGen.getFieldValue("FK_SPATIAL_ID");

           spatialGen.setField("FK_RA_ID", id);

           String spatialId, voluntary; // spatialType;

           if(value.indexOf(":") == -1) {
			 obligationDao.deleteSpatialLink(Integer.valueOf(id), new Integer(getID(value)));
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

      if ( (id != null) && (!clientCont.isEmpty()) ) {
         for (int i=0; i < clientCont.size(); i++) {
           SQLGenerator clientGen = (SQLGenerator)clientCont.get(i);
           String value = clientGen.getFieldValue("FK_CLIENT_ID");

           clientGen.setField("FK_CLIENT_ID", value.substring( value.indexOf(":") +1));
           clientGen.setField("FK_OBJECT_ID", id);
           clientGen.setField("STATUS", "C");
           clientGen.setField("TYPE", "A");           

           updateDB(clientGen.insertStatement());
         }
         clientCont.clear();
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
      
      //
      if (tblName.equals("T_OBLIGATION") && (state == MODIFY_RECORD || state == INSERT_RECORD)) {
              _getSQLGen = (SQLGenerator) gen.clone();
              if (state == MODIFY_RECORD) {
                  _wasObligationUpdate = true;
                  
              } else if (state == INSERT_RECORD) {
                  _wasObligationInsert = true;
              }
      }
    }catch (Exception e){logger.error(e);} 
      return true;
   }
   
   /*
    * 
    */
   public boolean wasObligationUpdate(){
       
       return _wasObligationUpdate;
   }
   
   /*
    * 
    */
   public boolean wasObligationInsert(){
       
       return _wasObligationInsert;
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
   public String idValue(){
       
       return _id;
   }
   
   
   /*
    * 
    */
   public SQLGenerator getSQLGen(){
       
       return _getSQLGen;
   }

   public ActivityHandler(ROEditServletAC servlet) {
      super(servlet);
      initDao();
   }
   // constructor for testing
   ActivityHandler(DBPoolIF dbPool, DBVendorIF dbVendor) {
      super(dbPool, dbVendor);
      initDao();
   }

  private void processInfoType(String raId, String infoIds) throws ServiceException{
    StringTokenizer st = new StringTokenizer(infoIds, "|");
    while (st.hasMoreTokens()) {
      String infoId=st.nextToken();
      if (infoId.length()>0) {
				obligationDao.insertInfoLink(Integer.valueOf(raId),infoId);
      }
        
    }
  }

   private void initDao(){
		try {
			undoDao = RODServices.getDbService().getUndoDao();
			obligationDao = RODServices.getDbService().getObligationDao();
			historicDeadlineDao = RODServices.getDbService().getHistoricDeadlineDao();
			clientDao = RODServices.getDbService().getClientDao();
			spatialHistoryDao = RODServices.getDbService().getSpatialHistoryDao();			
		} catch (ServiceException e) {
			logger.fatal(e);
		}		
	}
	
  /**
  * takes role info from DIR and saves if found
  */
/*   private void processRole(String roleId) {
      if (!Util.nullString(roleId)) 
        try {
         //quick fix - improve the handling later: not sensible to back up ALL roles before saving 1
          //RODServices.getDbService().backUpRoles();        
          Hashtable role = DirectoryService.getRole(roleId);
          RODServices.getDbService().saveRole(role,"","");
          //RODServices.getDbService().commitRoles();
        } catch (DirServiceException de ) {
          Logger.log("Error getting role infor for: " + roleId);
        } catch (ServiceException se ) {
          Logger.log("Error saving role info for: " + roleId);
        }

   } */
}
