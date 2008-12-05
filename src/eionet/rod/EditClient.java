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
 * Original Code: Risto Alt (TietoEnator)
 */

package eionet.rod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tee.xmlserver.DataSourceIF;
import com.tee.xmlserver.Parameters;
import com.tee.xmlserver.SaveHandler;
import com.tee.xmlserver.XMLSource;
import com.tee.xmlserver.XSQLException;

public class EditClient extends ROEditServletAC {
    
    protected String setXSLT(HttpServletRequest req) {
        return PREFIX + "client.xsl";
    }
    
    protected int setMode() {
        return FORM_HANDLER;
    }
   
    protected DataSourceIF prepareDataSource(Parameters params) throws XSQLException {
        HttpServletRequest req = params.getRequest();
        
        String querySource = PREFIX + "client.xrs";

        String param = params.getParameter(Constants.ID_PARAM);

        String queryPars[][] = {{"ID", param}};

        DataSourceIF dataSrc = XMLSource.getXMLSource(querySource, req);

        dataSrc.setParameters(queryPars);
        
        return userInfo(req, dataSrc);
      }
   
   protected void appDoPost(HttpServletRequest req, HttpServletResponse res) throws XSQLException {
       try {
        String location = "clients/" + req.getParameter("/XmlData/RowSet/Row/T_CLIENT/PK_CLIENT_ID");
        res.sendRedirect(location);
       } catch(java.io.IOException e) {
           throw new XSQLException(e, "Error in redirection");
       }
   }
   
   protected SaveHandler setDataHandler() {
      return new EditClientHandler(this);
   }
}