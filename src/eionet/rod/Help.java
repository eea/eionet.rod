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

import java.io.*;
import javax.servlet.http.*;

import com.tee.util.*;
import com.tee.xmlserver.*;

public class Help extends ROEditServletAC {
   protected String setXSLT(HttpServletRequest req) {
      return "../app/help.xsl";
   }
    
   protected int setMode() {
      return FORM_HANDLER;
   }
   
   protected DataSourceIF prepareDataSource(Parameters parameters){
      String querySource = "../app/help.xrs";
      String queryPars[][] = {{"ID", parameters.getParameter("helpID")}};

      DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, parameters.getRequest());
      dataSrc.setParameters(queryPars);

      return dataSrc;
   }
   
   protected SaveHandler setDataHandler() {
      return new HelpHandler(this);
   }

  protected void appDoPost(HttpServletRequest req, HttpServletResponse res) throws XSQLException {
    printPage(res, "<html><script>window.close()</script></html>");
   }
}