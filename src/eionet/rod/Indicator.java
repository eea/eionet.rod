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
 * The Original Code is "EINRC-7 / OPS Project".
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

import java.io.*;
import javax.servlet.http.*;

import com.tee.util.*;
import com.tee.xmlserver.*;

public class Indicator extends ROEditServletAC {
    
    protected int setMode() {
        return FORM_HANDLER;
    }
   
   protected DataSourceIF prepareDataSource(Parameters params){
      String querySource;
      querySource = PREFIX + E_INDICATOR_XRS;

      String id = params.getParameter(ID_PARAM);
      if ( Util.nullString(id) ) 
         throw new GeneralException(null, "Missing parameter '" + ID_PARAM + "'");

      String rid = params.getParameter(AID_PARAM);
      if ( Util.nullString(rid) ) 
         throw new GeneralException(null, "Missing parameter '" + AID_PARAM + "'");

      // prepare data source
      String[][] queryPars = {{"ID", id}, {"RID", rid}};   
            
      DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, params.getRequest());
      dataSrc.setParameters(queryPars);
      
      return userInfo(params.getRequest(), dataSrc);
   }
   
   protected void appDoPost(HttpServletRequest req, HttpServletResponse res) throws XSQLException {

    //close the window when update or insert
    //if delete then it is an invisible action and no need to close, redirect back to obligation


/*
    String actionType=req.getParameter("dom-update-mode");

    if (actionType != null && actionType.equals("D"))
      try {

        res.sendRedirect("activity.jsv?id=" + req.getParameter(ID_PARAM) + "&aid=" + req.getParameter(AID_PARAM) );
    } catch (IOException ioe ) {
        throw new XSQLException(ioe, "Error when redirecting to obligation page ");      
    } 
*/
//    else
      printPage(res, "<html><script>window.close()</script></html>");
   }
   
   protected SaveHandler setDataHandler() {
      return new IndicatorHandler(this);
   }

  protected String setXSLT(HttpServletRequest req) {
      return PREFIX + E_INDICATOR_XSL;
   }   
}