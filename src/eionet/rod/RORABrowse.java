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

import java.util.*;
import com.tee.util.*;
import com.tee.xmlserver.*;

/**
 * <P>Reporting Obligations/Activities browse and filter page servlet class.</P>
 *
 * <P>Servlet URL: <CODE>rorabrowse.jsv</CODE></P>
 *
 * <P>Database tables involved: see SearchStatement</P>
 *
 * <P>XSL file used: <CODE>rorabrowse.xsl</CODE><BR>
 * Query file used: <CODE>rorabrowse.xrs</CODE></P>
 *
 * @see SearchStatement
 *
 * @author  Andre Karpistsenko
 * @version 1.0
 */

public class RORABrowse extends ROServletAC {
/**
 *
 */
   protected int setMode() {
     return URL_TRANSFER;
   }

   protected String setXSLT(HttpServletRequest req) {
      return PREFIX + RORABROWSE_XSL;
   }
/**
 *
 */
   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
      HttpServletRequest req = params.getRequest();

      DataSourceIF dataSrc = new DataSource();
      String showfilter = params.getParameter(SHOWFILTER);
      if (showfilter != null)
        if (showfilter.equals("1")) {
        DataSourceIF XMLDataSrc = XMLSource.getXMLSource(PREFIX + RORABROWSE_QUERY, req);
        Enumeration e = XMLDataSrc.getQueries();
        while (e.hasMoreElements())
          dataSrc.setQuery((QueryStatementIF)e.nextElement());
      }
      dataSrc.setQuery(new SearchStatement(params));

      addMetaInfo(dataSrc);
      //dataSrc.setQuery(new MetaData("RAMetaData", "T_ACTIVITY"));

      return userInfo(req, dataSrc);
   }
}

