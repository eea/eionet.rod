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

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.AppUserIF;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import com.tee.xmlserver.Logger;
import java.io.IOException;

import eionet.rod.countrysrv.Extractor;

public class Harvester extends ROEditServletAC {

  private Extractor ext;

  public Harvester()  {
  }

/*   protected String setXSLT(HttpServletRequest req) {
      return PREFIX + "harvester.xsl";
   } */

   protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    return null;
   }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) 
         throws javax.servlet.ServletException, java.io.IOException {
    try {
      AppUserIF user = getUser(req);
      java.sql.Connection conn = (user != null) ? user.getConnection() : null;

      if (conn == null)
        throw new XSQLException(null, "Not authenticated user");

    if  ( ! getAcl(Constants.ACL_HARVEST_NAME).checkPermission( user.getUserName(), Constants.ACL_VIEW_PERMISSION ))
        throw new XSQLException(null, "Not authorized user");

  StringBuffer s = new StringBuffer();
  s.append("<html>")
    .append("<script language = 'javascript' src='script/util.js'></script>")
    .append("<script language = 'javascript'> function harvest( mode ) {")
      .append("var ff = document.f;")
      .append(" ff.action = ff.action + '?MODE=' + mode;")
      .append("alert('It takes some to harvest the data. Please wait');")
      .append("document.body.style.cursor='wait';")
      .append("ff.submit();")
      .append("} </script>")
      .append("<body bgcolor=\"#f0f0f0\">")
      .append("<a href=\"javascript:openPopup('history.jsv', 'id=0&amp;entity=H')\"><img src='images/showhistory.png' alt='Show harvesting history' border='0'/></a><br/>")
      .append("<form align='center' name=\"f\" method=\"POST\" action=\"harvester.jsv\">")
      .append("<b>Select data, you want to be harvested:</b><br/>")
      .append("<input width='200'  type='button' onClick='javascript:harvest(0)' value='All' style=\"width: 200; background-image: url(\'images/bgr_form_buttons_wide.jpg\'); font-family: Verdana; font-size: 10pt; font-weight: bold\"></input>")
      .append("<br>").append("<input width='200'style=\"width:200\" type='button' onClick='javascript:harvest(" + Extractor.DELIVERIES + ")' value='Deliveries' style=\"width: 200; background-image: url(\'images/bgr_form_buttons_wide.jpg\'); font-family: Verdana; font-size: 10pt; font-weight: bold\"></input>")            
      .append("<br>").append("<input width='200'style=\"width:200\" type='button' onClick='javascript:harvest(" + Extractor.ROLES + ")' value='Roles' style=\"width: 200; background-image: url(\'images/bgr_form_buttons_wide.jpg\'); font-family: Verdana; font-size: 10pt; font-weight: bold\"></input>")                  
      .append("<br>").append("<input width='200'style=\"width:200\" type='button' onClick='javascript:harvest(" + Extractor.PARAMS + ")' value='Parameters' style=\"width: 200; background-image: url(\'images/bgr_form_buttons_wide.jpg\'); font-family: Verdana; font-size: 10pt; font-weight: bold\"></input>")                  
      .append("</form>")
      .append("</body></html>");


    //buid html
    res.setContentType("text/html");
    res.getWriter().write( s.toString() );
      
     } catch (Exception xe) {
         printError(xe, res);
     }
  }

protected void doPost(HttpServletRequest req, HttpServletResponse res) 
         throws javax.servlet.ServletException, java.io.IOException {

      res.setContentType("text/html");

    res.getWriter().write("<html><body bgcolor=\"#f0f0f0\">");

      try {
        AppUserIF user = getUser(req);
        java.sql.Connection conn = (user != null) ? user.getConnection() : null;

        if (conn == null)
          throw new XSQLException(null, "Not authenticated user");
        else {
 
          //harvest
          //res.getWriter().write("Harvester starts. It may take several minutes to harvest");
          try {

            if  ( ! getAcl(Constants.ACL_HARVEST_NAME).checkPermission( user.getUserName(), Constants.ACL_UPDATE_PERMISSION ))
              throw new XSQLException(null, "Not authenticated user");
          
            int mode = Integer.parseInt( req.getParameter("MODE") ); 
            if (ext==null)
              ext = new Extractor();
            
            ext.harvest(mode, user.getUserName() );
            res.getWriter().write("<b>Harvested!</b> See log for details");
          } catch (Exception e ) {
             printError(e, res);
          }
        }
      } catch (XSQLException xs ) {
        printError(xs, res);
      }

    res.getWriter().write("</body></html>");      


  }

  private void printError( Exception e, HttpServletResponse res  ) throws IOException {
    res.setContentType("text/html");  
    res.getWriter().write("<html><body bgcolor=\"#f0f0f0\">");
    res.getWriter().write("<b>Error:</b><br>");
    res.getWriter().write(e.toString());
    res.getWriter().write("</body></html>");
  } 
}