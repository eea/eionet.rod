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
import javax.servlet.http.*;

import com.tee.xmlserver.*;
import com.tee.util.*;
import javax.servlet.ServletException;
import eionet.rod.services.RODServices;

/**
 * <P>Reporting obligations editor servlet class.</P>
 *
 * <P>Servlet URL: <CODE>reporting.jsv</CODE></P>
 *
 * HTTP request parameters:
 *    <LI>id - T_REPORTING.PK_RO_ID
 *    <LI>aid - T_SOURCE.PK_SOURCE_ID
 *
 * <P>Database tables involved: T_REPORTING, T_SOURCE, T_LOOKUP</P>
 *
 * <P>XSL file used: <CODE>ereporting.xsl</CODE><BR>
 * Query file used: <CODE>ereporting.xrs</CODE></P>
 *
 * @author  Rando Valt, Andre Karpistsenko
 * @version 1.1
 */

public class Reporting extends ROEditServletAC {
/**
 *
 */
   protected String setXSLT(HttpServletRequest req) {
      return PREFIX + E_REPORTING_XSL;
   }
/**
 *
 */
   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
      String id = params.getParameter(ID_PARAM);
      if ( Util.nullString(id) )
         throw new GeneralException(null, "Missing parameter '" + ID_PARAM + "'");
      String sid = params.getParameter(AID_PARAM);
      if ( Util.nullString(sid) )
         throw new GeneralException(null, "Missing parameter '" + AID_PARAM + "'");


      //quick fix !! 
      /*try {
      if (id.equals("MAX"))
        id=RODServices.getDbService().getMaxROId();
      } catch (Exception e ) {
        throw new XSQLException (e,"Error getting id=" + e.toString());
      } */
        
      String[][] queryPars = {{"ID", id}, {"SID", sid}};   

	  HttpServletRequest req = params.getRequest();

      DataSourceIF dataSrc = XMLSource.getXMLSource(PREFIX + E_REPORTING_QUERY, params.getRequest());
      dataSrc.setParameters(queryPars);
       
      return userInfo(req, dataSrc);
   }
/**
 *
 */
   protected void xdoGet(HttpServletRequest req, HttpServletResponse res) 
         throws javax.servlet.ServletException, java.io.IOException {
      Connection conn = null;
      Statement stmt  = null;

      String id = req.getParameter(ID_PARAM);

      
      if ( Util.nullString(id) )
         throw new GeneralException(null, "Missing parameter '" + ID_PARAM + "'");

      String tmpName = Thread.currentThread().getName() + System.currentTimeMillis();
      tmpName = tmpName.replace('-', '_').toUpperCase();
      //String tmpSpatialTbl = "S" + tmpName;

      DataSourceIF dataSrc = null;
      //QueryStatementIF qrySpatial = null;

      try {


         AppUserIF user = getUser(req);
         conn = (user != null) ? user.getConnection() : null;
         if (conn == null)
            throw new XSQLException(null, "Not authenticated user");
			/*
			System.out.println("==================================================");
			System.out.println("= DOGET ");
			System.out.println("==================================================");
			*/

         try {
            stmt = conn.createStatement();

            /*if (Logger.enable(5))
               Logger.log("Create temp table " + tmpSpatialTbl);
            stmt.execute(CREATE1 + tmpSpatialTbl + CREATE2 + SPATIALS + "-1"); */

            // prepare data source
            dataSrc = prepareDataSource(new Parameters(req));

            // parameters
            /*
            qrySpatial = new SubSelectStatement("SPATIAL", "SPATIAL_TYPE", tmpSpatialTbl, "", "");
            dataSrc.setQuery(qrySpatial);
            */

            // call superclass to generate the page
            super.doGet(req, res);

         } catch (SQLException sqle) {
            String msg = "Creating a temporary table failed";
            Logger.log(msg, sqle);
            throw new XSQLException(sqle, msg);
         } 
         /*finally {
            try {
               if (stmt != null) {
                  if (Logger.enable(5))
                     Logger.log("Drop temp table " + tmpSpatialTbl);
                  stmt.execute(DROP + tmpSpatialTbl);

                  stmt.close();
               }
            } catch (SQLException e1) {
            } finally {
               try { if (conn != null) conn.close(); } catch (SQLException e2) {}
            }
         } */
      } catch (XSQLException xe) {
         printError(xe, req, res);
      } /*finally {
         if (qrySpatial != null) dataSrc.unsetQuery(qrySpatial);
      } */
   }
/**
 *
 */
   protected void appDoPost(HttpServletRequest req, HttpServletResponse res)
         throws XSQLException {


      
      try {

/*log(" ************** appDoPOST");      
java.util.Enumeration e = req.getParameterNames()      ;

while (e.hasMoreElements())
  log( (String)e.nextElement());
*/
/*
System.out.println("==================================================");
System.out.println("= AppdOPOST ");
System.out.println("==================================================");
*/
        //checkPermissions(req);  

		String reDirect=req.getParameter("silent");
		reDirect=(reDirect==null ? "0" : reDirect);

      
         String location = "show.jsv?" + 
            ((curRecord != null) ?
               "id=" + curRecord + 
               "&aid=" + req.getParameter("/XmlData/RowSet[@Name='Reporting']/Row/T_REPORTING/FK_SOURCE_ID") + 
               "&mode=R&page=0" :
               "id=" + req.getParameter("/XmlData/RowSet[@Name='Reporting']/Row/T_REPORTING/FK_SOURCE_ID") + 
               "&mode=S");
         // DBG         
		//Logger.log("========== Redirecting to " + reDirect);
         if (Logger.enable(5))
            Logger.log("Redirecting to " + location);
         //
		 if (reDirect.equals("0"))
	         res.sendRedirect(location);
		 else
		    res.sendRedirect("reporting.jsv?id=" + curRecord + "&aid=" + req.getParameter("/XmlData/RowSet[@Name='Reporting']/Row/T_REPORTING/FK_SOURCE_ID") );

      } catch(java.io.IOException e) {
         throw new XSQLException(e, "Error in redirection");
     /* } catch(ServletException se) {
         throw new XSQLException(se, "No permission to delete record"); */
      }
   }
/**  
 *
 */
    protected SaveHandler setDataHandler() {
        return new ReportingHandler(this);
    }

/*   private static final String SPATIALS =
      "T_SPATIAL_LNK.FK_SPATIAL_ID FROM T_SPATIAL_LNK WHERE T_SPATIAL_LNK.FK_RO_ID=";
  */

}