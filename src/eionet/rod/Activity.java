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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.util.SQLGenerator;
import com.tee.util.Util;
import com.tee.xmlserver.AppUserIF;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.GeneralException;
import com.tee.xmlserver.Logger;
import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.QueryStatementIF;
import com.tee.xmlserver.SaveHandler;
import com.tee.xmlserver.XMLSource;
import com.tee.xmlserver.XSQLException;

import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * <P>Activity editor servlet class.</P>
 * <P>Servlet URL: <CODE>activity.jsv</CODE></P>
 *
 * HTTP request parameters:
 *    <LI>id - T_ACTIVITY.PK_RA_ID
 *    <LI>aid - T_REPORTING.PK_RO_ID
 *
 * <P>Database tables involved: T_ACTIVITY, T_REPORTING, T_SOURCE, T_PARAMETER, T_PARAMETER_LNK,
 * T_LOOKUP</P>
 *
 * <P>XSL file used: <CODE>eactivity.xsl</CODE><BR>
 * Query file used: <CODE>eactivity.xrs</CODE></P>
 *
 * @author  Andre Karpistsenko
 * @version 1.1
 */

public class Activity extends ROEditServletAC { 
    
    /** */
    private ActivityHandler activityHandler = null;
    
    
/**
 *
 */
   protected String setXSLT(HttpServletRequest req) {
      return PREFIX + E_ACTIVITY_XSL;
   }
/**
 *
 */
   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
      String id = params.getParameter(ID_PARAM);
      if ( Util.nullString(id) ) 
         throw new GeneralException(null, "Missing parameter '" + ID_PARAM + "'");
      String rid = params.getParameter(AID_PARAM);
      if ( Util.nullString(rid) ) 
         throw new GeneralException(null, "Missing parameter '" + AID_PARAM + "'"); 



      // prepare data source
      String[][] queryPars = {{"ID", Util.strLiteral(id)}, {"RID", Util.strLiteral(rid)}};   

      HttpServletRequest req = params.getRequest();
      DataSourceIF dataSrc = XMLSource.getXMLSource(PREFIX + E_ACTIVITY_QUERY, req);
      /*
      Enumeration e = dataSrc.getQueries();
      if (e != null) {
          QueryStatementIF qry = (QueryStatementIF)e.nextElement();
          qry.addAttribute("tab", params.getParameter("tab"));
      }*/
      
      dataSrc.setParameters(queryPars);
      //addMetaInfo(dataSrc);      
      
      return userInfo(req, dataSrc);
   }
/**
 *
 */
   protected void doGet(HttpServletRequest req, HttpServletResponse res) 
         throws javax.servlet.ServletException, java.io.IOException {
       
      Connection conn = null;
      Statement stmt  = null;

      String id = req.getParameter(ID_PARAM);
      if ( Util.nullString(id) ) 
         throw new GeneralException(null, "Missing parameter '" + ID_PARAM + "'");
      
      String tmpName = Thread.currentThread().getName() + System.currentTimeMillis();
      tmpName = tmpName.replace('-', '_').toUpperCase();

      String tmpIssueTbl = "I" + tmpName;
      String tmpParTbl = "P" + tmpName;

      String tmpSpatialTbl = "S" + tmpName;
      String tmpIndicatorTbl = "C" + tmpName;

      QueryStatementIF qrySpatial = null;

//--
      DataSourceIF dataSrc = null;
      QueryStatementIF qryIssue = null;
      QueryStatementIF qryPars = null;

      QueryStatementIF qryIndicators = null;

      try {
         AppUserIF user = getUser(req);


         conn = (user != null) ? user.getConnection() : null;
         if (conn == null)
            throw new XSQLException(null, "Not authenticated user. Please verify that you are logged in (for security reasons, the system will log you out after a period of inactivity). If the problem persists, please contact the server administrator.");

         //checkPermissions(req);      
         
         try {
            stmt = conn.createStatement();

            if (Logger.enable(5))
               Logger.log("Create temp table " + tmpIssueTbl);
            stmt.execute(CREATE1 + tmpIssueTbl + CREATE2 + ISSUES + "-1");
		/*
			if (Logger.enable(5))
               Logger.log("Create temp table " + tmpParTbl);
            stmt.execute(CREATE1 + tmpParTbl + CREATE2 + PARAMETERS + "-1");
		*/

            if (Logger.enable(5))
               Logger.log("Create temp table " + tmpSpatialTbl);
            stmt.execute(CREATE1 + tmpSpatialTbl + CREATE2 + SPATIALS + "-1");

            /*
            if (Logger.enable(5))
               Logger.log("Create temp table " + tmpIndicatorTbl);
            stmt.execute(CREATE1 + tmpIndicatorTbl + CREATE2 + INDICATORS + "-1");
            */

            // prepare data source
            dataSrc = prepareDataSource(new Parameters(req));

            // issues
            qryIssue = new SubSelectStatement("ISSUE", tmpIssueTbl);
            dataSrc.setQuery(qryIssue);
            
            //qryPars = new SubSelectStatement("PARAMETER", "FK_GROUP_ID", tmpParTbl,"NEW=1");
            //dataSrc.setQuery(qryPars);

            // spatials
            qrySpatial = new SubSelectStatement("SPATIAL", "SPATIAL_TYPE", tmpSpatialTbl, "", "");
            dataSrc.setQuery(qrySpatial);

            //String tableName, String pkField, String fkField, String name1, String name2, String tmpTable
            /*
            qryIndicators = new SubSelectStatement("T_INDICATOR", "PK_INDICATOR_ID", "FK_RA_ID", "URL", "NAME", tmpIndicatorTbl); 
            dataSrc.setQuery(qryIndicators);
            */

            // call superclass to generate the page
            super.doGet(req, res);

         } catch (SQLException sqle) {
            String msg = "Creating a temporary table failed";
            Logger.log(msg, sqle);
            throw new XSQLException(sqle, msg);
         } finally {
            try {
               if (stmt != null) {
                  if (Logger.enable(5))
                     Logger.log("Drop temp table " + tmpIssueTbl);
                  stmt.execute(DROP + tmpIssueTbl);
                  if (Logger.enable(5))
                     Logger.log("Drop temp table " + tmpParTbl);
                  stmt.execute(DROP + tmpParTbl);

                  if (Logger.enable(5))
                     Logger.log("Drop temp table " + tmpSpatialTbl);
                  stmt.execute(DROP + tmpSpatialTbl);

                  if (Logger.enable(5))
                     Logger.log("Drop temp table " + tmpIndicatorTbl);
                  stmt.execute(DROP + tmpIndicatorTbl);


                  stmt.close();
               }
            } catch (SQLException e1) {
            } finally {
               try { if (conn != null) conn.close(); } catch (SQLException e2) {}
            }
         }
      } catch (XSQLException xe) {
         printError(xe, req, res);
      } finally {
         if (qryIssue != null) dataSrc.unsetQuery(qryIssue);
         if (qryPars != null) dataSrc.unsetQuery(qryPars);
        if (qrySpatial != null) dataSrc.unsetQuery(qrySpatial);         
      }
   }
/**
 *
 */

   protected void appDoPost(HttpServletRequest req, HttpServletResponse res)
         throws XSQLException {
      try {
          
          //
          if (activityHandler.wasObligationUpdate() || activityHandler.wasObligationInsert()){
              AppUserIF user = getUser(req);
              String userName = user.getUserName();
              
              try{
                  SQLGenerator gen = activityHandler.getSQLGen();
                  if (gen != null) {
                  
                      Vector lists = new Vector();
                      Vector list = new Vector();
                      long timestamp = System.currentTimeMillis();
                      String events = "http://rod.eionet.europa.eu/events/" + timestamp;
                      String obligationID = null;
                      
                      if (curRecord != null)
                          obligationID = curRecord;
                      int obligation_id = Integer.valueOf(obligationID).intValue();
                      
                      if (activityHandler.wasObligationUpdate()) {
                         
                          list.add(events);
                          list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                          list.add(Attrs.SCHEMA_RDF + "ObligationChange");
                          lists.add(list);
                          
                          list = new Vector();
                          list.add(events);
                          list.add(Attrs.SCHEMA_RDF + "event_type");
                          list.add("Obligation change");
                          lists.add(list);
                          
                          list = new Vector();
                          list.add(events);
                          list.add("http://purl.org/dc/elements/1.1/title");
                          //list.add(Attrs.SCHEMA_RDF + "label");
                          list.add("Obligation change");
                          lists.add(list);
                          
                      } else if (activityHandler.wasObligationInsert()) {
                          
                          list.add(events);
                          list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                          list.add(Attrs.SCHEMA_RDF + "NewObligation");
                          lists.add(list);
                          
                          list = new Vector();
                          list.add(events);
                          list.add(Attrs.SCHEMA_RDF + "event_type");
                          list.add("New Obligation");
                          lists.add(list);
                          
                          list = new Vector();
                          list.add(events);
                          list.add("http://purl.org/dc/elements/1.1/title");
                          //list.add(Attrs.SCHEMA_RDF + "label");
                          list.add("New Obligation");
                          lists.add(list);
                          
                      }
                      
                      list = new Vector();
                      list.add(events);
                      list.add(Attrs.SCHEMA_RDF + "obligation");
                      list.add(gen.getFieldValue("TITLE"));
                      lists.add(list);
                      
                      
                      Vector countries = RODServices.getDbService().getSpatialDao().getObligationCountries(obligation_id);
                      
                      for (Enumeration en = countries.elements(); en.hasMoreElements(); ){
                          Hashtable hash = (Hashtable) en.nextElement();
                          list = new Vector();
                          list.add(events);
                          list.add(Attrs.SCHEMA_RDF + "locality");
                          list.add(hash.get("name"));                  
                          lists.add(list);
                      }
                      
                      list = new Vector();
                      list.add(events);
                      list.add(Attrs.SCHEMA_RDF + "responsiblerole");
                      list.add(gen.getFieldValue("RESPONSIBLE_ROLE"));
                      lists.add(list);
                      
                      list = new Vector();
                      list.add(events);
                      list.add(Attrs.SCHEMA_RDF + "actor");
                      list.add(userName);
                      lists.add(list);
                      
                      if (activityHandler.wasObligationUpdate()) {                      
                          Vector changes = getChanges(obligationID);
                          for(Enumeration en = changes.elements(); en.hasMoreElements(); ){
                              String label = (String) en.nextElement();
                              list = new Vector();
                              list.add(events);
                              list.add(Attrs.SCHEMA_RDF + "change");
                              list.add(label);
                              lists.add(list);
                          }
                      }
                      
                      list = new Vector();
                      list.add(events);
                      list.add("http://purl.org/dc/elements/1.1/identifier");
                      String ra_id = gen.getFieldValue("PK_RA_ID");
                      String src_id = gen.getFieldValue("FK_SOURCE_ID");
                      String url = "http://rod.eionet.europa.eu/show.jsv?id="+obligationID+"&mode=A";
                      list.add(url);
                      
                      lists.add(list);
                      
                      if (lists.size() > 0) UNSEventSender.makeCall(lists);
                  }
              }
              catch (Exception e){
              }
          }

    		String reDirect=req.getParameter("silent");
        reDirect=(reDirect==null ? "0" : reDirect);


         String location = "" +
            ((curRecord != null) ?
               "obligations/" + curRecord :
               "show.jsv?id=" + req.getParameter("/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID") +
               //req.getParameter("/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID") +
                "&mode=S") ; 
               /*+ "&aid=" + req.getParameter("/XmlData/RowSet[@Name='Activity']/Row/T_ACTIVITY/FK_SOURCE_ID") +
               "&mode=R&page=0") ; */
         // DBG         
         if (Logger.enable(5))
            Logger.log("Redirecting to " + location);
         //
         //res.sendRedirect(location);

         //if adding a new client we do a "silent" save, no redirecting
    		 if (reDirect.equals("0"))
	         res.sendRedirect(location);
         else
          res.sendRedirect("activity.jsv?id=" + curRecord + "&aid=" + req.getParameter("/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID") );

         
      } catch(java.io.IOException e) {
         throw new XSQLException(e, "Error in redirection");
      }
   }
   
   private Vector getChanges(String obligationID) throws ServiceException {
       
       long ts = activityHandler.tsValue();
       Vector res_vec = new Vector();
       Vector undo_vec = RODServices.getDbService().getUndoDao().getUndoInformation(ts,"U","T_OBLIGATION",obligationID);
       for (int i=0; i<undo_vec.size(); i++){
           Hashtable hash = (Hashtable) undo_vec.elementAt(i);
           
           String label = "";
           String ut = (String) hash.get("undo_time");
           String tabel = (String) hash.get("tab");
           String col = (String) hash.get("col");
           String value = (String) hash.get("value");
           String currentValue = RODServices.getDbService().getDifferencesDao().getDifferences(Long.valueOf(ut).longValue(),tabel,col);
           if ((value != null && value.trim().equals("")) || (value != null && value.trim().equals("null"))) value = null;
           if ((currentValue != null && currentValue.trim().equals("")) || (currentValue != null && currentValue.trim().equals("null"))) currentValue = null;
           boolean diff = (value != null && currentValue != null && value.equals(currentValue)) || (value == null && currentValue == null)  ;
           
           if(!diff){
               label = getLabel(col, value, currentValue);
               res_vec.add(label);
           }
       }
       
       Hashtable countries_formally = RODServices.getDbService().getDifferencesDao().getDifferencesInCountries(ts,new Integer(obligationID).intValue(),"N","U");
       if(countries_formally.size() > 0){
           String added = (String) countries_formally.get("added");
           String removed = (String) countries_formally.get("removed");
           if(added.length() > 0){
               res_vec.add("'Countries reporting formally' added: "+added);
           }
           if(removed.length() > 0){
               res_vec.add("'Countries reporting formally' removed: "+removed);
           }
       }
       
       Hashtable countries_voluntarily = RODServices.getDbService().getDifferencesDao().getDifferencesInCountries(ts,new Integer(obligationID).intValue(),"Y","U");
       if(countries_voluntarily.size() > 0){
           String added = (String) countries_voluntarily.get("added");
           String removed = (String) countries_voluntarily.get("removed");
           if(added.length() > 0){
               res_vec.add("'Countries reporting voluntarily' added: "+added);
           }
           if(removed.length() > 0){
               res_vec.add("'Countries reporting voluntarily' removed: "+removed);
           }
       }
       
       Hashtable issues = RODServices.getDbService().getDifferencesDao().getDifferencesInIssues(ts,new Integer(obligationID).intValue(),"U"); 
       if(issues.size() > 0){
           String added = (String) issues.get("added");
           String removed = (String) issues.get("removed");
           if(added.length() > 0){
               res_vec.add("'Environmental issues' added: "+added);
           }
           if(removed.length() > 0){
               res_vec.add("'Environmental issues' removed: "+removed);
           }
       }
       
       Hashtable clients = RODServices.getDbService().getDifferencesDao().getDifferencesInClients(ts,new Integer(obligationID).intValue(),"C","U","A");
       if(clients.size() > 0){
           String added = (String) clients.get("added");
           String removed = (String) clients.get("removed");
           if(added.length() > 0){
               res_vec.add("'Other clients using this reporting' added: "+added);
           }
           if(removed.length() > 0){
               res_vec.add("'Other clients using this reporting' removed: "+removed);
           }
       }
       
       Hashtable info = RODServices.getDbService().getDifferencesDao().getDifferencesInInfo(ts,new Integer(obligationID).intValue(),"U","I");        
       if(info.size() > 0){
           String added = (String) info.get("added");
           String removed = (String) info.get("removed");
           if(added.length() > 0){
               res_vec.add("'Type of info reported' added: "+added);
           }
           if(removed.length() > 0){
               res_vec.add("'Type of info reported' removed: "+removed);
           }
       }
       
       return res_vec;
   }
   
   private String getLabel(String col, String value, String currentValue) throws ServiceException {
       
       String label = "";
       
       if(col != null && col.equalsIgnoreCase("TITLE")){
           label = "'Title' changed ";
       } else if (col != null && col.equalsIgnoreCase("DESCRIPTION")){
           label = "'Description' changed ";
       } else if (col != null && col.equalsIgnoreCase("COORDINATOR_ROLE")){
           label = "'National reporting coordinators role' changed ";
       } else if (col != null && col.equalsIgnoreCase("COORDINATOR_ROLE_SUF")){
           label = "'National reporting coordinators suffix' changed ";
           value = getSuffixValue(value);
           currentValue = getSuffixValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("COORDINATOR")){
           label = "'National reporting coordinators name' changed ";
       } else if (col != null && col.equalsIgnoreCase("COORDINATOR_URL")){
           label = "'National reporting coordinators URL' changed ";
       } else if (col != null && col.equalsIgnoreCase("RESPONSIBLE_ROLE")){
           label = "'National reporting contacts role' changed ";
       } else if (col != null && col.equalsIgnoreCase("RESPONSIBLE_ROLE_SUF")){
           label = "'National reporting contacts suffix' changed ";
           value = getSuffixValue(value);
           currentValue = getSuffixValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("NATIONAL_CONTACT")){
           label = "'National reporting contacts name' changed ";
       } else if (col != null && col.equalsIgnoreCase("REPORT_FREQ_MONTHS")){
           label = "'Reporting frequency in months' changed ";
       } else if (col != null && col.equalsIgnoreCase("FIRST_REPORTING")){
           label = "'Baseline reporting date' changed ";
       } else if (col != null && col.equalsIgnoreCase("VALID_TO")){
           label = "'Valid to' changed ";
       } else if (col != null && col.equalsIgnoreCase("NEXT_DEADLINE")){
           label = "'Next due date' changed ";
       } else if (col != null && col.equalsIgnoreCase("NEXT_REPORTING")){
           label = "'Reporting date' changed ";
       } else if (col != null && col.equalsIgnoreCase("DATE_COMMENTS")){
           label = "'Date comments' changed ";
       } else if (col != null && col.equalsIgnoreCase("FORMAT_NAME")){
           label = "'Name of reporting guidelines' changed ";
       } else if (col != null && col.equalsIgnoreCase("REPORT_FORMAT_URL")){
           label = "'URL to reporting guidelines' changed ";
       } else if (col != null && col.equalsIgnoreCase("VALID_SINCE")){
           label = "'Format valid since' changed ";
       } else if (col != null && col.equalsIgnoreCase("REPORTING_FORMAT")){
           label = "'Reporting guidelines -Extra info' changed ";
       } else if (col != null && col.equalsIgnoreCase("LOCATION_INFO")){
           label = "'Name of repository' changed ";
       } else if (col != null && col.equalsIgnoreCase("LOCATION_PTR")){
           label = "'URL to repository' changed ";
       } else if (col != null && col.equalsIgnoreCase("DATA_USED_FOR")){
           label = "'Data used for' changed ";
       } else if (col != null && col.equalsIgnoreCase("LEGAL_MORAL")){
           label = "'Obligation type' changed ";
       } else if (col != null && col.equalsIgnoreCase("PARAMETERS")){
           label = "'Parameters' changed ";
       } else if (col != null && col.equalsIgnoreCase("EEA_PRIMARY")){
           label = "'This obligation is EIONET Priority Data flow' changed ";
           value = getChkValue(value);
           currentValue = getChkValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("EEA_CORE")){
           label = "'This obligation is used for EEA Core set of indicators' changed ";
           value = getChkValue(value);
           currentValue = getChkValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("FLAGGED")){
           label = "'This obligation is flagged' changed ";
           value = getChkValue(value);
           currentValue = getChkValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("DPSIR_D")){
           label = "'DPSIR D' changed ";
           value = getDpsirValue(value);
           currentValue = getDpsirValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("DPSIR_P")){
           label = "'DPSIR P' changed ";
           value = getDpsirValue(value);
           currentValue = getDpsirValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("DPSIR_S")){
           label = "'DPSIR S' changed ";
           value = getDpsirValue(value);
           currentValue = getDpsirValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("DPSIR_I")){
           label = "'DPSIR I' changed ";
           value = getDpsirValue(value);
           currentValue = getDpsirValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("DPSIR_R")){
           label = "'DPSIR R' changed ";
           value = getDpsirValue(value);
           currentValue = getDpsirValue(currentValue);
       } else if (col != null && col.equalsIgnoreCase("OVERLAP_URL")){
           label = "'URL of overlapping obligation' changed ";
       } else if (col != null && col.equalsIgnoreCase("OVERLAP_URL")){
           //Indicators
       } else if (col != null && col.equalsIgnoreCase("COMMENT")){
           label = "'General comments' changed ";
       } else if (col != null && col.equalsIgnoreCase("AUTHORITY")){
           label = "'Authority giving rise to the obligation' changed ";
       } else if (col != null && col.equalsIgnoreCase("RM_VERIFIED")){
           label = "'Verified' changed ";
       } else if (col != null && col.equalsIgnoreCase("RM_VERIFIED_BY")){
           label = "'Verified by' changed ";
       } else if (col != null && col.equalsIgnoreCase("RM_NEXT_UPDATE")){
           label = "'Next update due' changed ";
       } else if (col != null && col.equalsIgnoreCase("VALIDATED_BY")){
           label = "'Validated by' changed ";
       } else if (col != null && col.equalsIgnoreCase("FK_CLIENT_ID")){
           label = "'Report to' changed ";
           value = RODServices.getDbService().getClientDao().getOrganisationNameByID(value);
           currentValue = RODServices.getDbService().getClientDao().getOrganisationNameByID(currentValue);
       } else if (col != null && col.equalsIgnoreCase("LAST_UPDATE")){
           label = "'Last update' changed ";
       }
       
       label = label + " from '" + value + "' to '" + currentValue + "'";
       
       return label;
   }
   
   private String getDpsirValue(String value) throws ServiceException {
    
        String ret = null;
        if(value.equalsIgnoreCase("null") || value.equalsIgnoreCase("no")){
            ret="unchecked";
        }
        if(value.equalsIgnoreCase("yes")){
            ret="checked";
        }
        return ret;
   }
   
   private String getChkValue(String value) throws ServiceException {
       
       String ret = null;
       int b = new Integer(value).intValue();
       if(b == 0){
           ret="unchecked";
       }
       if(b == 1){
           ret="checked";
       }
       return ret;
  }
   
   private String getSuffixValue(String value) throws ServiceException {
       
       String ret = null;
       int b = new Integer(value).intValue();
       if(b == 0){
           ret="checked";
       }
       if(b == 1){
           ret="unchecked";
       }
       return ret;
   }

/**  
 *
 */
   protected SaveHandler setDataHandler() {
       
       this.activityHandler = new ActivityHandler(this);
       return this.activityHandler;
   }
   
   private static final String ISSUES =
      "T_RAISSUE_LNK.FK_ISSUE_ID FROM T_RAISSUE_LNK WHERE T_RAISSUE_LNK.FK_RA_ID=";
/*
   private static final String PARAMETERS =
      "T_PARAMETER_LNK.FK_PARAMETER_ID FROM T_PARAMETER_LNK WHERE T_PARAMETER_LNK.FK_RA_ID=";
*/
   private static final String SPATIALS =
      "T_RASPATIAL_LNK.FK_SPATIAL_ID FROM T_RASPATIAL_LNK WHERE T_RASPATIAL_LNK.FK_RA_ID=";

   private static final String INDICATORS =
      "FK_INDICATOR_ID FROM T_INDICATOR_LNK WHERE FK_RA_ID=";

      
}
