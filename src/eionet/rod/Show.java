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

import javax.servlet.http.*;

import com.tee.util.*;
import com.tee.xmlserver.*;

/**
 * <P>Generic servlet to retrieve WebROD read-only pages.</P>
 *
 * <P>Servlet URL: <CODE>show.jsv</CODE></P>
 *
 * <P>Depending on the HTTP request parameter 'mode', the servlet acts as follows:
 * <UL TYPE="SQUARE">
 * <LI>mode=S - displays legislative act information;
 *    <UL TYPE="DISC">
 *       <LI>HTTP request parameters: id=T_SOURCE.PK_SOURCE_ID
 *       <LI>Database tables involved: T_SOURCE, T_SOURCE_LNK, T_SOURCE_CLASS, T_SOURCE_TYPE, T_REPORTING
 *       <LI>XSL file used: <CODE>source.xsl</CODE>
 *       <LI>Query file used: <CODE>source.xrs</CODE>
 *    </UL>
 * <LI>mode=R - displays reporting obligation information;
 *    <UL TYPE="DISC">
 *       <LI>HTTP request parameters: id=T_REPORTING.PK_RO_ID, aid=T_REPORTING.FK_SOURCE_ID, page
 *       <LI>Database tables involved: T_REPORTING, 	T_SOURCE, T_ACTIVITY, T_INFORMATION, T_ISSUE_LNK, T_SPATIAL_LNK
 *       <LI>XSL file used: <CODE>reporting.xsl</CODE>
 *       <LI>Query file used: <CODE>reporting.xrs</CODE>
 *    </UL>
 * <LI>mode=A - displays activity information;
 *    <UL TYPE="DISC">
 *       <LI>HTTP request parameters: id=T_ACTIVITY.PK_RA_ID, aid=T_REPORTING.PK_RO_ID
 *       <LI>Database tables involved: T_ACTIVITY, T_PARAMETER_LNK, T_PARAMETER, T_REPORTING, T_SOURCE
 *       <LI>XSL file used: <CODE>activity.xsl</CODE>
 *       <LI>Query file used: <CODE>activity.xrs</CODE>
 *    </UL>
 * <LI>mode=C - displays legislation hierarchy;
 *    <UL TYPE="DISC">
 *       <LI>HTTP request parameters: id=T_SOURCE_CLASS.PK_CLASS_ID
 *       <LI>Database tables involved: T_SOURCE_LNK, T_SOURCE_CLASS, T_SOURCE
 *       <LI>XSL file used: <CODE>hierarchy.xsl</CODE>
 *       <LI>Query file used: <CODE>hierarchy.xrs</CODE>
 *    </UL>
 * <LI>mode=X - displays legislation hierarchy for adding obligations;
 *    <UL TYPE="DISC">
 *       <LI>HTTP request parameters: id=T_SOURCE_CLASS.PK_CLASS_ID
 *       <LI>Database tables involved: T_SOURCE_LNK, T_SOURCE_CLASS, T_SOURCE
 *       <LI>XSL file used: <CODE>hierarchyx.xsl</CODE>
 *       <LI>Query file used: <CODE>hierarchyx.xrs</CODE>
 *    </UL>
 *
 * @author  Andre Karpistsenko, Rando Valt
 * @version 1.1
 */

public class Show extends ROServletAC {
   private String mode = "";
   
   private DataSourceIF _prepare(String SQL) {
      DataSourceIF dataSrc = new DataSource();
      QueryStatementIF qry = new QueryStatement(SQL);
      dataSrc.setQuery(qry);
      
      return dataSrc;
   }
/**
 *
 */
   protected int setMode() {
      if ( mode.equals(ACTIVITY_MODE))
         return URL_TRANSFER;
      else
         return SIMPLE;
   }

/**
 *
 */
   protected String setXSLT(HttpServletRequest req) {
      mode = req.getParameter(MODE_PARAM);
      if ( Util.nullString(mode) )
         throw new GeneralException(null, "Missing parameter '" + MODE_PARAM + "'");

//System.out.println("==================== mode " + mode);      

      if (mode.equals(SOURCE_MODE)) // || mode.equals(SOURCE_PRINT_MODE) )
         return PREFIX + SOURCE_XSL;
      else if (mode.equals(HIERARCHY_MODE)) // || mode.equals(HIERARCHY_PRINT_MODE) )
         return PREFIX + HIERARCHY_XSL;
      else if (mode.equals(HIERARCHYX_MODE))
         return PREFIX + HIERARCHYX_XSL;
      else if (mode.equals(ACTIVITY_MODE))
         return PREFIX + ACTIVITY_XSL;
      else if (mode.equals(INDICATORS_MODE))
         return PREFIX + INDICATORS_XSL;
      else if (mode.equals(PARAMETERS_MODE))
         return PREFIX + PARAMETERS_XSL;

      else
         throw new GeneralException(null, "Unknown value for parameter '" + MODE_PARAM + "': " + mode);
   }
/**
 *
 */
   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
      mode = params.getParameter(MODE_PARAM);
      if ( Util.nullString(mode) )
         throw new GeneralException(null, "Missing parameter '" + MODE_PARAM + "'");
      
      DataSourceIF dataSrc = null;
      HttpServletRequest req = params.getRequest();

      String id = params.getParameter(ID_PARAM);
      if ( Util.nullString(id) )
        throw new GeneralException(null, "Missing parameter '" + ID_PARAM + "'");

        String[][] queryPars = {{"ID", id}};

         String qrySrc;
         if (mode.equals(SOURCE_MODE))
            qrySrc = PREFIX + SOURCE_QUERY;
         else if (mode.equals(HIERARCHY_MODE) || mode.equals(HIERARCHYX_MODE)) 
            qrySrc = PREFIX + HIERARCHY_QUERY;
         else if (mode.equals(INDICATORS_MODE))
            qrySrc = PREFIX + INDICATORS_QUERY;
         else if (mode.equals(ACTIVITY_MODE))
            qrySrc = PREFIX + ACTIVITY_QUERY;
         else if (mode.equals(PARAMETERS_MODE)) 
            qrySrc = PREFIX + PARAMETERS_QUERY;
         else
            throw new GeneralException(null, "Unknown value for parameter 'mode': " + mode);


         dataSrc = XMLSource.getXMLSource(qrySrc, req);
         dataSrc.setParameters(queryPars);

      addMetaInfo(dataSrc);
      return userInfo(req, dataSrc);
   }
}