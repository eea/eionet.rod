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
 * The Original Code is "NaMod project".
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

package eionet.rod.countrysrv.servlets;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Calendar;
import java.util.Enumeration;
import com.tee.xmlserver.DataSource;
import com.tee.xmlserver.QueryStatementIF;
import eionet.rod.countrysrv.CSSearchStatement;
import com.tee.util.Util;
import eionet.rod.ROServletAC;

public class Main extends ROServletAC { // CSServletAC {

  protected String setXSLT(HttpServletRequest req) {
        return PREFIX + "csmain.xsl";
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
    HttpServletRequest req = params.getRequest();
    DataSource dataSrc = new DataSource();

    //lookup values from rorabrowse.xrs
    DataSourceIF XMLDataSrc = XMLSource.getXMLSource(PREFIX + "csmain.xrs", req);
          Enumeration e = XMLDataSrc.getQueries();
          while (e.hasMoreElements()) 
            dataSrc.setQuery((QueryStatementIF)e.nextElement());
            
     String[][] queryPars= new String[3][2];

     String countryId = params.getParameter("COUNTRY_ID");
     queryPars[0][0]="COUNTRY_ID";
     queryPars[0][1]=(Util.nullString(countryId) ? "0": Util.strLiteral(countryId));

     String issueId = params.getParameter("ISSUE_ID");
     queryPars[1][0]="ISSUE_ID";
     queryPars[1][1]=(Util.nullString(issueId) ? "0": Util.strLiteral(issueId));

     String clientId = params.getParameter("CLIENT_ID");
     queryPars[2][0]="CLIENT_ID";
     queryPars[2][1]=(Util.nullString(clientId) ? "0": Util.strLiteral(clientId));
          
    dataSrc.setParameters(queryPars);
    dataSrc.setQuery(new CSSearchStatement(params, false));



    //dataSrc.setParameters(queryPars);
    addMetaInfo(dataSrc);
    
    return userInfo(req,dataSrc);    
  }

    protected int setMode() {
        return FORM_HANDLER;
    }

  /*private void _log(String s ) {
    System.out.println("================= " + s);
  } */
}