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
 * <P>Main page servlet class.</P>
 *
 * <P>Servlet URL: <CODE>index.html</CODE></P>
 *
 * <P>Database tables involved: T_REPORTING, 	T_ACTIVITY, T_ISSUE, T_SPATIAL, T_PARAM_GROUP, T_LOOKUP</P>
 *
 * <P>XSL file used: <CODE>index.xsl</CODE><BR>
 * Query file used: <CODE>index.xrs</CODE></P>
 *
 * @author  Rando Valt, Andre Karpistsenko
 * @version 1.1
 */

public class Index extends ROServletAC {
/**
 *
 */
   protected String setXSLT(HttpServletRequest req) {
      return PREFIX + INDEX_XSL;        
   }
/**
 *
 */
   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
      HttpServletRequest req = params.getRequest();
      DataSourceIF dataSrc = XMLSource.getXMLSource(PREFIX + INDEX_QUERY, req);
      addMetaInfo(dataSrc);
      return userInfo(req, dataSrc);
   }
}