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

import com.tee.util.*;
import com.tee.xmlserver.*;

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
            throw new XSQLException(null, "Not authenticated user");

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
      return new SourceHandler(this);
   }
/**
 *
 */
   protected void appDoPost(HttpServletRequest req, HttpServletResponse res)
         throws XSQLException {
      try {
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
         //
         res.sendRedirect(location);
      } catch(java.io.IOException e) {
         throw new XSQLException(e, "Error in redirection");
      }
   }
/*
   private void checkPermissions ( HttpServletRequest req  ) throws XSQLException {
    String mode = null;
    
    String userName = getUser(req).getUserName();
//    String id = req.getParameter( ID_PARAM );
    String upd = req.getParameter( FormHandlerIF.MODE_PARAM );

    upd = (upd==null ? "" : upd);
//    id = (id==null ? "" : id); //not needed?
    if ( upd.equals("A"))
      mode = Constants.ACL_INSERT_PERMISSION;
    else if ( upd.equals("D"))
      mode = Constants.ACL_DELETE_PERMISSION;
    else if ( upd.equals("U"))
      mode = Constants.ACL_UPDATE_PERMISSION;
        
    boolean b = false;
    try {
      b = getAcl(Constants.ACL_LI_NAME).checkPermission( userName, mode );
    } catch ( Exception e ) {
      throw new XSQLException (e, "Error getting user rights ");
    }

    if (!b)
      throw new XSQLException (null, "No permission to perform the action");

    
  } */
   private static final String PARENTS =
      "T_SOURCE_LNK.FK_SOURCE_PARENT_ID FROM T_SOURCE_LNK WHERE T_SOURCE_LNK.CHILD_TYPE='S' AND T_SOURCE_LNK.FK_SOURCE_CHILD_ID=";
}