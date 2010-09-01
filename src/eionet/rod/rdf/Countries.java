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

package eionet.rod.rdf;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;

import eionet.rod.RODUtil;
import eionet.rod.services.ServiceException;
import eionet.rod.services.WebRODService;


import java.util.Vector;
import java.util.Hashtable;

public class Countries extends RDFServletAC {

  protected  String generateRDF(HttpServletRequest req) throws ServiceException {

    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    s.append("<rdf:RDF ")
    .append(" xmlns:rod=\"").append(rodSchemaNamespace).append("#\" ")
    .append(rdfNameSpace)
    .append(rdfSNameSpace)
    .append(">");

    WebRODService wSrv = new WebRODService();
    Vector countries = wSrv.getCountries();
    
    for (int i= 0; i< countries.size(); i++){
      Hashtable act = (Hashtable)countries.elementAt(i);
      String uri = (String)act.get("uri");
      String name = (String)act.get("name");
      String iso = (String)act.get("iso");

      s.append("<rod:Locality rdf:about='").append(RODUtil.replaceTags(uri, true, true)).append("'>")
        .append("<rdfs:label>").append(RODUtil.replaceTags(name, true, true)).append("</rdfs:label>")
        .append("<rod:loccode>").append(iso).append("</rod:loccode>")
        .append("</rod:Locality>");

    }
    
    s.append("</rdf:RDF>");

    return s.toString();

  }  



}