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
 * Original Code: Ander Tenno (TietoEnator)
 */


package eionet.rod;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.XSQLException;
import com.tee.xmlserver.XMLSource;

/**
* Servlet for showing bigger texts: disclaimer, general help etc
*/
public class TxtServlet extends ROServletAC {
  protected String setXSLT(HttpServletRequest req) {

    String mode=req.getParameter(MODE_PARAM);
    String xslName="";
    if (mode.equals("D"))
      xslName="disclaimer.xsl";
    else if (mode.equals("H"))
      xslName="generalhelp.xsl";
    else if (mode.equals("R"))
      xslName="rdfhelp.xsl";


    return  PREFIX + xslName;
    
   }

  protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {

    String querySource = PREFIX + "dummy.xrs";
    DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, params.getRequest());
    addMetaInfo(dataSrc);
    return userInfo(params.getRequest(),dataSrc);
  }
}