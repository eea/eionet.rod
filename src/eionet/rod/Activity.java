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
      DataSourceIF dataSrc = XMLSource.getXMLSource(PREFIX + E_ACTIVITY_QUERY, params.getRequest());
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
              
              try{
                  SQLGenerator gen = activityHandler.getSQLGen();
                  if (gen != null) {
                  
                      Vector lists = new Vector();
                      Vector list = new Vector();
                      long timestamp = System.currentTimeMillis();
                      String events = "http://rod.eionet.europa.eu/events/" + timestamp;
                      String obligationID = null;
                      
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
                          list.add(Attrs.SCHEMA_RDF + "label");
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
                          list.add(Attrs.SCHEMA_RDF + "label");
                          list.add("New Obligation");
                          lists.add(list);
                          
                      }
                      
                      list = new Vector();
                      list.add(events);
                      list.add(Attrs.SCHEMA_RDF + "obligation");
                      list.add(gen.getFieldValue("TITLE"));
                      lists.add(list);
                      
                      if (curRecord != null)
                          obligationID = curRecord;
                      Vector countries = RODServices.getDbService().getObligationCountries(obligationID);
                      
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
                      list.add("http://purl.org/dc/elements/1.1/identifier");
                      String ra_id = gen.getFieldValue("PK_RA_ID");
                      String src_id = gen.getFieldValue("FK_SOURCE_ID");
                      String url = "http://rod.eionet.europa.eu/show.jsv?id="+ra_id+"&aid="+src_id+"&mode=A";
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


         String location = "show.jsv?" +
            ((curRecord != null) ?
               "id=" + curRecord + 
               "&aid=" + req.getParameter("/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID") +
               "&mode=A" :
               "id=" + req.getParameter("/XmlData/RowSet[@Name='Activity']/Row/T_OBLIGATION/FK_SOURCE_ID") +
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
