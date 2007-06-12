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
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.servlet.http.*;

import com.tee.util.*;
import com.tee.xmlserver.*;

import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * <P>Legal instrument editor servlet class.</P>
 *
 * <P>Servlet URL: <CODE>source.jsv</CODE></P>
 *
 * HTTP request parameters:
 *    <LI>id - T_SOURCE.PK_SOURCE_ID
 *
 * <P>Database tables involved: T_SOURCE, T_SOURCE_LNK, T_SOURCE_CLASS, T_SOURCE_TYPE, T_LOOKUP</P>
 *
 * <P>XSL file used: <CODE>esource.xsl</CODE><BR>
 * Query file used: <CODE>esource.xrs</CODE></P>
 *
 * @author  Rando Valt, Andre Karpistsenko
 * @version 1.1
 */

public class Source extends ROEditServletAC {
    
    private SourceHandler sourceHandler = null;
/**
 *
 */
   protected String setXSLT(HttpServletRequest req) {
      return PREFIX + E_SOURCE_XSL;
   }
/**
 *
 */
   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
      String id = params.getParameter(ID_PARAM);
      if ( Util.nullString(id) ) 
         throw new GeneralException(null, "Missing parameter '" + ID_PARAM + "'");

      // prepare data source
      String[][] queryPars = {{"ID", id}};   

      HttpServletRequest req = params.getRequest();
      DataSourceIF dataSrc = XMLSource.getXMLSource(PREFIX + E_SOURCE_QUERY, params.getRequest());
      dataSrc.setParameters(queryPars);
 
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
      
      String tmpParTbl = "C" + tmpName;

      DataSourceIF dataSrc = null;

      QueryStatementIF qryParents = null;

      try {


        
         AppUserIF user = getUser(req);
         conn = (user != null) ? user.getConnection() : null;
         if (conn == null)
            throw new XSQLException(null, "Not authenticated user. Please verify that you are logged in (for security reasons, the system will log you out after a period of inactivity). If the problem persists, please contact the server administrator.");

        //checkPermissions(req);
        
         try {
            stmt = conn.createStatement();

            if (Logger.enable(5))
               Logger.log("Create temp table " + tmpParTbl);
            stmt.execute(CREATE1 + tmpParTbl + CREATE2 + PARENTS + id);

            // prepare data source
            dataSrc = prepareDataSource(new Parameters(req));
         
            // parents
            qryParents = new SubSelectStatement("T_SOURCE_CLASS", "PK_CLASS_ID", "FK_SOURCE_PARENT_ID", 
                                                "CLASSIFICATOR", "CLASS_NAME", tmpParTbl);
            dataSrc.setQuery(qryParents);

            // call superclass to generate the page
            super.doGet(req, res);

         } catch (SQLException sqle) {
            String msg = "Creating temp table failed";
            Logger.log(msg, sqle);
            throw new XSQLException(sqle, msg);
         } finally {
            try {
               if (stmt != null) {
                  if (Logger.enable(5))
                     Logger.log("Drop temp table " + tmpParTbl);
                  stmt.execute(DROP + tmpParTbl);

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
         if (qryParents != null) dataSrc.unsetQuery(qryParents);
      }
   }
/**  
 *
 */
   protected SaveHandler setDataHandler() {
       this.sourceHandler = new SourceHandler(this);
       return this.sourceHandler;
   }
/**
 *
 */
   protected void appDoPost(HttpServletRequest req, HttpServletResponse res)
         throws XSQLException {
      try {
          
          if (sourceHandler.wasInstrumentUpdate() || sourceHandler.wasInstrumentInsert()){
              AppUserIF user = getUser(req);
              String userName = user.getUserName();
              
              try{
                  SQLGenerator gen = sourceHandler.getSQLGen();
                  if (gen != null) {
                  
                      Vector lists = new Vector();
                      Vector list = new Vector();
                      long timestamp = System.currentTimeMillis();
                      String events = "http://rod.eionet.europa.eu/events/" + timestamp;
                      String instrumentID = null;
                      
                      if (curRecord != null)
                          instrumentID = curRecord;
                      
                      if (sourceHandler.wasInstrumentUpdate()) {
                         
                          list.add(events);
                          list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                          list.add(Attrs.SCHEMA_RDF + "InstrumentChange");
                          lists.add(list);
                          
                          list = new Vector();
                          list.add(events);
                          list.add(Attrs.SCHEMA_RDF + "event_type");
                          list.add("Instrument change");
                          lists.add(list);
                          
                          list = new Vector();
                          list.add(events);
                          list.add("http://purl.org/dc/elements/1.1/title");
                          //list.add(Attrs.SCHEMA_RDF + "label");
                          list.add("Instrument change");
                          lists.add(list);
                          
                      } else if (sourceHandler.wasInstrumentInsert()) {
                          
                          list.add(events);
                          list.add("http://www.w3.org/1999/02/22-rdf-syntax-ns#type");
                          list.add(Attrs.SCHEMA_RDF + "NewInstrument");
                          lists.add(list);
                          
                          list = new Vector();
                          list.add(events);
                          list.add(Attrs.SCHEMA_RDF + "event_type");
                          list.add("New instrument");
                          lists.add(list);
                          
                          list = new Vector();
                          list.add(events);
                          list.add("http://purl.org/dc/elements/1.1/title");
                          //list.add(Attrs.SCHEMA_RDF + "label");
                          list.add("New instrument");
                          lists.add(list);
                          
                      }
                      
                      list = new Vector();
                      list.add(events);
                      list.add(Attrs.SCHEMA_RDF + "instrument");
                      list.add(gen.getFieldValue("TITLE"));
                      lists.add(list);
                      
                      list = new Vector();
                      list.add(events);
                      list.add(Attrs.SCHEMA_RDF + "actor");
                      list.add(userName);
                      lists.add(list);
                      
                      if (sourceHandler.wasInstrumentUpdate()) {                      
                          Vector changes = getChanges(instrumentID);
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
                      String url = "http://rod.eionet.europa.eu/show.jsv?id=" + curRecord + "&mode=S";
                      list.add(url);
                      
                      lists.add(list);
                      
                      if (lists.size() > 0) UNSEventSender.makeCall(lists);
                  }
              }
              catch (Exception e){
              }
          } 
          
    	 String reDirect = req.getParameter("silent");
         reDirect = (reDirect == null) ? "0" : reDirect;

         String location = null;
         //checkPermissions(req);         
         if (curRecord != null) {
            location = "show.jsv?id=" + curRecord + "&mode=S";
         }
         else {
            // try to show the parent legislation hierarchy page
            String parent = req.getParameter("/XmlData/RowSet[@Name='Source']/Row/SubSet[@Name='LnkParent']/Row/T_SOURCE_LNK/FK_SOURCE_PARENT_ID");
            if ( !Util.nullString(parent) ) {
               location = "show.jsv?id=" + parent + "&mode=C";
            }
            else {
               // try to show parent topic page
               String topic = req.getParameter("/XmlData/RowSet[@Name='Source']/Row/SubSet[@Name='LnkTopic']/Row/T_TOPIC_LNK/FK_TOPIC_ID");
               if ( !Util.nullString(topic) ) {
                  location = "show.jsv?id=" + topic + "&mode=T";
               }
               // redirect to the main page
               else
                  location = "index.html";
            }
         }
         

         // DBG         
         if (Logger.enable(5))
            Logger.log("Redirecting to " + location);

         // if adding a new client we do a "silent" save, no redirecting
    		if (reDirect.equals("0"))
	         res.sendRedirect(location);
         else
            res.sendRedirect("source.jsv?id=" + curRecord);

      } catch(java.io.IOException e) {
         throw new XSQLException(e, "Error in redirection");
      }
   }
   
   private Vector getChanges(String instrumentID) throws ServiceException {
       
       long ts = sourceHandler.tsValue();
       Vector res_vec = new Vector();
       Vector undo_vec = RODServices.getDbService().getUndoDao().getUndoInformation(ts,"U","T_SOURCE",instrumentID);
       for (int i=0; i<undo_vec.size(); i++){
           Hashtable hash = (Hashtable) undo_vec.elementAt(i);
           
           String label = "";
           String ut = (String) hash.get("undo_time");
           String tabel = (String) hash.get("tab");
           String col = (String) hash.get("col");
           String value = (String) hash.get("value");
           if(tabel != null & !tabel.equals("") && tabel.equals("T_SOURCE")){
               String currentValue = RODServices.getDbService().getDifferencesDao().getDifferences(Long.valueOf(ut).longValue(),tabel,col);
               if ((value != null && value.trim().equals("")) || (value != null && value.trim().equals("null"))) value = null;
               if ((currentValue != null && currentValue.trim().equals("")) || (currentValue != null && currentValue.trim().equals("null"))) currentValue = null;
               boolean diff = (value != null && currentValue != null && value.equals(currentValue)) || (value == null && currentValue == null)  ;
               
               if(!diff){
                   label = getLabel(col, value, currentValue);
                   res_vec.add(label);
               }
           }
       }
       
       Hashtable eurlex = RODServices.getDbService().getDifferencesDao().getDifferencesInEurlexCategories(ts,new Integer(instrumentID).intValue(),"U");
       if(eurlex.size() > 0){
           String added = (String) eurlex.get("added");
           String removed = (String) eurlex.get("removed");
           if(added.length() > 0){
               res_vec.add("'Eur-lex categories' added: "+added);
           }
           if(removed.length() > 0){
               res_vec.add("'Eur-lex categories' removed: "+removed);
           }
       }
       
       return res_vec;
   }

    private String getLabel(String col, String value, String currentValue) throws ServiceException {
        
        String label = "";
        
        if(col != null && col.equalsIgnoreCase("TITLE")){
            label = "'Title' changed ";
        } else if (col != null && col.equalsIgnoreCase("ALIAS")){
            label = "'Short name' changed ";
        } else if (col != null && col.equalsIgnoreCase("SOURCE_CODE")){
            label = "'Identification number' changed ";
        } else if (col != null && col.equalsIgnoreCase("DRAFT")){
            label = "'Draft' changed ";
        } else if (col != null && col.equalsIgnoreCase("URL")){
            label = "'URL to official text' changed ";
        } else if (col != null && col.equalsIgnoreCase("CELEX_REF")){
            label = "'CELEX reference' changed ";
        } else if (col != null && col.equalsIgnoreCase("FK_CLIENT_ID")){
            label = "'Issued by' changed ";
            value = RODServices.getDbService().getClientDao().getOrganisationNameByID(value);
            currentValue = RODServices.getDbService().getClientDao().getOrganisationNameByID(currentValue);
        } else if (col != null && col.equalsIgnoreCase("ISSUED_BY_URL")){
            label = "'URL to issuer' changed ";
        } else if (col != null && col.equalsIgnoreCase("DGENV_REVIEW")){
            label = "'DG Env review of reporting theme' changed ";
            value = RODServices.getDbService().getSourceDao().getDGEnvName(value);
            currentValue = RODServices.getDbService().getSourceDao().getDGEnvName(currentValue);
        } else if (col != null && col.equalsIgnoreCase("VALID_FROM")){
            label = "'Valid from' changed ";
        } else if (col != null && col.equalsIgnoreCase("GEOGRAPHIC_SCOPE")){
            label = "'Geographic scope' changed ";
        } else if (col != null && col.equalsIgnoreCase("ABSTRACT")){
            label = "'Abstract' changed ";
        } else if (col != null && col.equalsIgnoreCase("COMMENT")){
            label = "'Comments' changed ";
        } else if (col != null && col.equalsIgnoreCase("EC_ENTRY_INTO_FORCE")){
            label = "'EC entry into force' changed ";
        } else if (col != null && col.equalsIgnoreCase("EC_ACCESSION")){
            label = "'EC accession' changed ";
        } else if (col != null && col.equalsIgnoreCase("SECRETARIAT")){
            label = "'Secretariat' changed ";
        } else if (col != null && col.equalsIgnoreCase("SECRETARIAT_URL")){
            label = "'URL to Secretariat homepage' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_VERIFIED")){
            label = "'Verified' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_VERIFIED_BY")){
            label = "'Verified by' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_NEXT_UPDATE")){
            label = "'Next update due' changed ";
        } else if (col != null && col.equalsIgnoreCase("RM_VALIDATED_BY")){
            label = "'Validated by' changed ";
        } else if (col != null && col.equalsIgnoreCase("LAST_UPDATE")){
            label = "'Last update' changed ";
        }
        
        label = label + " from '" + value + "' to '" + currentValue + "'";
        
        return label;
    }
   
   private static final String PARENTS =
      "T_SOURCE_LNK.FK_SOURCE_PARENT_ID FROM T_SOURCE_LNK WHERE T_SOURCE_LNK.CHILD_TYPE='S' AND T_SOURCE_LNK.FK_SOURCE_CHILD_ID=";
}