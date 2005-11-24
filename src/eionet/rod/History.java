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
 * The Original Code is "EINRC-5 / WebROD Project".
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2002 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Kaido Laine (TietoEnator)
 */

package eionet.rod;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.GeneralException;
import com.tee.xmlserver.XMLSource;



import com.tee.util.Util;
import com.tee.xmlserver.AppUserIF;
import com.tee.uit.security.SignOnException;


public class History extends ROServletAC {

/**
 *
 */
   protected String setXSLT(HttpServletRequest req) {
      String id = req.getParameter(ID_PARAM);
      String xslName = null;
      if ( !Util.nullString(id) ) 
        xslName = PREFIX + HISTORY_XSL; //history of 1 item
      else
        xslName = PREFIX + ACTION_HIST_XSL; //history of all items

    return xslName;
 }
/**
 *
 */
   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {

      if  (!isAuthorised( params.getRequest() ))
         throw new XSQLException(null, "Not authorized user. Please verify that you are logged in (for security reasons, the system will log you out after a period of inactivity). If the problem persists, please contact the server administrator.");
        
      String id = params.getParameter(ID_PARAM);
      String item_type = params.getParameter(ENTITY_PARAM);
      String mode = params.getParameter(MODE_PARAM);

      String secondParam = "";

      if ( Util.nullString(item_type) ) 
         throw new GeneralException(null, "Missing parameter '" + ENTITY_PARAM + "'");
      if ( Util.nullString(id) && Util.nullString(mode) ) 
               throw new GeneralException(null, "One of parameters '" + MODE_PARAM + "' or '" + ID_PARAM + "' must not be empty");
               
      if (!Util.nullString(id))
         secondParam = " ITEM_ID = " + id;
      else        
         secondParam = " ACTION_TYPE = '" + mode + "'";
         
         
      // prepare data source
      String[][] queryPars = {{"TYPE", "'" + item_type + "'" }, {"OTHER", secondParam}};   

      DataSourceIF dataSrc = XMLSource.getXMLSource(PREFIX + HISTORY_QUERY, params.getRequest());
      dataSrc.setParameters(queryPars);
      
      return userInfo(params.getRequest(), dataSrc);
   }

 private boolean isAuthorised(HttpServletRequest req) throws XSQLException {
    boolean b = false;
     AppUserIF u = getUser(req);
      try {
      if (u != null) 
          b = getAcl(Constants.ACL_ADMIN_NAME).checkPermission(u.getUserName(), Constants.ACL_VIEW_PERMISSION);
      } catch ( SignOnException soe  )  {
        throw new XSQLException(soe, "Error getting permissions " + soe.toString());
      }
    return b;
 }

}