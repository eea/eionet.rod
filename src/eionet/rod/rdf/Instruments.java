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

package eionet.rod.rdf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import java.util.ResourceBundle;
import java.util.MissingResourceException;

//import com.tee.util.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import eionet.rod.services.WebRODService;

import java.util.Hashtable;
import java.util.Vector;
import eionet.rod.services.ServiceException;
import javax.servlet.ServletConfig;
import com.tee.xmlserver.BaseServletAC;
import eionet.rod.services.RODServices;
import com.tee.util.Util;

/**
 * <P>Servlet URL: <CODE>rdf</CODE></P>
 *
 * <P>Database tables involved: T_ACTIVITY</P>
 *
 * <P>XSL file used: <CODE>index.xsl</CODE><BR>
 * Query file used: <CODE>index.xrs</CODE></P>
 *
 * @author  Kaido Laine
 * @version 1.0
 */
public class Instruments extends RDFServletAC {

  private static final String  actPropName = "activity";

  private static String allNameSpaces =  rdfNameSpace +  rdfSNameSpace +
    dcNs +
    "xmlns:dcterms='http://purl.org/dc/terms/'";


  protected  String generateRDF(HttpServletRequest req) throws ServiceException {

    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    s.append("<rdf:RDF ")
    //.append(" xmlns=\"").append(activitiesNamespace).append("#\"")
    .append(" xmlns:rod=\"").append(rodSchemaNamespace).append("#\"")
    .append(" ")
    .append(allNameSpaces)
    .append(">");


    //WebRODService wSrv = new WebRODService();
    Vector instruments = RODServices.getDbService().getInstruments();
    
    for (int i= 0; i< instruments.size(); i++){
      Hashtable li=(Hashtable)instruments.elementAt(i);
      String pk=(String)li.get("PK_SOURCE_ID");
      String title=(String)li.get("ALIAS");
      String legalName=(String)li.get("TITLE");
      String lastUpdate=(String)li.get("LAST_UPDATE");
      String url=(String)li.get("URL");      
      String abstr=(String)li.get("ABSTRACT");      
      String issuedBy=(String)li.get("ISSUED_BY");      

      //show legal name if short name is empty
      title=(Util.nullString(title) ? legalName : title);

      s.append("<rod:Instrument rdf:about=\"" + instrumentsNamespace + pk + "\">")
        .append("<rdf:value>").append(title).append("</rdf:value>")
        .append("<rdfs:label>").append(title).append("</rdfs:label>")        
        .append("<dc:title>").append(legalName).append("</dc:title>")        
        .append("<dcterms:modified>").append(lastUpdate).append("</dcterms:modified>");

        if (!Util.nullString(abstr))
          s.append("<dcterms:abstract>").append(abstr).append("</dcterms:abstract>");
        
        if (!Util.nullString(url))
          s.append("<dc:identifier rdf:resource=\"" + url + "\"/>");

        if (!Util.nullString(issuedBy))
          s.append("<dc:creator>").append(issuedBy).append("</dc:creator>");
          
          
        s.append("</rod:Instrument>");        
/*
      s.append("<rod:Obligation rdf:about=\"").append(obligationsNamespace).append("/").append(pk).append("\">")
        .append("<rdf:value>").append(title).append("</rdf:value>")
        .append("<rdfs:label>").append(title).append("</rdfs:label>")
        .append("<dcterms:modified>").append(lastUpdate).append("</dcterms:modified>")
        .append("</rod:Obligation>");
*/
    }
    
    s.append("</rdf:RDF>");

    return s.toString();

  }  

 
}
