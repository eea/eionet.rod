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

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;

import com.tee.util.*;
import com.tee.xmlserver.*;

public class SpatialHistory extends ROServletAC {
   protected String setXSLT(HttpServletRequest req) {
      return PREFIX + "spatialhistory.xsl";
   }
    
   
   protected DataSourceIF prepareDataSource(Parameters parameters){

      String querySource = PREFIX + "spatialhistory.xrs";
      String idParam = parameters.getParameter("ID");
      String spatialIdParam = parameters.getParameter("spatialID");
      String spatialHistoryIdParam = parameters.getParameter("spatialHistoryID");
      
      String queryPars[][] = {{"ID",idParam}};
      
      DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, parameters.getRequest());
      dataSrc.setParameters(queryPars);
      
      java.util.Enumeration e = dataSrc.getQueries();
      if (e != null) {
         QueryStatementIF qry = (QueryStatementIF)e.nextElement();
         if(idParam != null && !idParam.equalsIgnoreCase(""))
             qry.addAttribute("ID",idParam);
         
         if(spatialIdParam != null && !spatialIdParam.equalsIgnoreCase(""))
             qry.addAttribute("spatialID",spatialIdParam);
         else
             qry.addAttribute("spatialID",null);

         if(spatialHistoryIdParam != null && !spatialHistoryIdParam.equalsIgnoreCase(""))
             qry.addAttribute("spatialHistoryID",spatialHistoryIdParam);
             
      }

      return userInfo( parameters.getRequest() , dataSrc);    
   }
   
   protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

       String id = req.getParameter("ID");
       try {
          appDoGet(req, res);
          res.sendRedirect("show.jsv?ID="+id+"&mode=A&tab=participation");

       } catch (Exception e) {
             throw new GeneralException(e, e.getMessage());
       }
    }
   
   protected SaveHandler setDataHandler() {
      return null;
   }


}