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
import eionet.rod.services.DbServiceIF;
import eionet.rod.services.RODServices;


/**
 * <P>Servlet URL: <CODE>rdf</CODE></P>
 *
 * <P>Database tables involved: T_ACTIVITY</P>
 *
 * <P>XSL file used: <CODE>index.xsl</CODE><BR>
 * Query file used: <CODE>index.xrs</CODE></P>
 *
 * @author  Kaido Laine
 * @version 1.1
 */
public class Activities extends RDFServletAC {

  private static final String  actPropName = "activity";

  private static String allNameSpaces =  rdfNameSpace +  rdfSNameSpace + dcNs +
    "xmlns:eor=\"http://dublincore.org/2000/03/13/eor#\" " +
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


    DbServiceIF wSrv = RODServices.getDbService();
    //WebRODService wSrv = new WebRODService();
    Vector acts = wSrv.getObligations();
    
    for (int i= 0; i< acts.size(); i++){
      Hashtable act = (Hashtable)acts.elementAt(i);
      String pk = (String)act.get("PK_RA_ID");
      String title = (String)act.get("TITLE");
      String lastUpdate = (String)act.get("LAST_UPDATE");
      String liId = (String)act.get("PK_SOURCE_ID");
      
      String validSince = (String)act.get("VALID_SINCE");
      String terminated = (String)act.get("terminated");
      String comment = (String)act.get("COMMENT");

      String respRole = (String)act.get("RESPONSIBLE_ROLE");
      String nextDeadline = (String)act.get("NEXT_DEADLINE");
      String nextDeadline2 = (String)act.get("NEXT_DEADLINE2");

      String repFormat=(String)act.get("REPORTING_FORMAT");
      String formatName=(String)act.get("FORMAT_NAME");
      String repFormatUrl=(String)act.get("REPORT_FORMAT_URL");      

      String description=(String)act.get("DESCRIPTION");  

      if (formatName.equals("") && !repFormatUrl.equals(""))
        formatName = repFormatUrl;

      String detailsUrl = (String)act.get("details_url");


      //s.append("<rod:Obligation rdf:ID=\"ra-").append(pk).append("\">")
      s.append("<rod:Obligation rdf:about=\"").append(obligationsNamespace).append("/").append(pk).append("\">")
        .append("<dc:title>").append(title).append("</dc:title>")
        .append("<rdfs:label>").append(title).append("</rdfs:label>")
        .append("<dcterms:abstract>").append(description).append("</dcterms:abstract>")
        .append("<dcterms:modified>").append(lastUpdate).append("</dcterms:modified>")
        .append("<dcterms:valid>").append(validSince).append("</dcterms:valid>")        
        .append("<rod:terminated>").append(terminated).append("</rod:terminated>")                
        .append("<rod:comment>").append(comment).append("</rod:comment>")                
        .append("<rod:responsiblerole>").append(respRole).append("</rod:responsiblerole>")                
        .append("<rod:nextdeadline>").append(nextDeadline).append("</rod:nextdeadline>")                        
        .append("<rod:nextdeadline2>").append(nextDeadline2).append("</rod:nextdeadline2>")                
        .append("<rod:guidelines>").append(repFormat).append("</rod:guidelines>")                
        
        .append("<rod:instrument rdf:resource=\"" + instrumentsNamespace + liId + "\"/>");

        s.append(composeResource("rod:details_url", "Information page", detailsUrl));

        if (!repFormatUrl.equals(""))
          s.append(composeResource("rod:guidelines_url", formatName, repFormatUrl));

        
        s.append("</rod:Obligation>");

    }
    //loop for the countries
    String[][] raIds=wSrv.getObligationIds();
    for (int i= 0; i< raIds.length; i++) {
      String pk = raIds[i][0];

      String[][] spIds = wSrv.getCountries(pk);
      
      s.append("<rdf:Description rdf:about=\"").append(obligationsNamespace).append("/").append(pk).append("\">");
        for (int j=0; j<spIds.length; j++) 
          s.append(countryTag(spIds[j][0]));
          
      s.append("</rdf:Description>");      
    }

    //issues list
    String [][] issues = wSrv.getIssueIdPairs();
    for (int i= 0; i< issues.length; i++) {
      String pk = issues[i][0];
      String name = issues[i][1];
      s.append("<rod:Issue rdf:about=\"").append(issuesNamespace).append(pk).append("\">")
        .append("<rdfs:label>").append(name).append("</rdfs:label>")
        .append("</rod:Issue>");
      
    }

   for (int i= 0; i< raIds.length; i++) {
   
    s.append("<rdf:Description rdf:about=\"").append(obligationsNamespace).append("/").append(raIds[i][0]).append("\">");
    String[][] iIds = wSrv.getIssues(raIds[i][0]);
    for (int j=0; j<iIds.length; j++) 
      s.append("<rod:issue rdf:resource=\"" + issuesNamespace + iIds[j][0] + "\"/>");
          
    s.append("</rdf:Description>");         
   }
    
    s.append("</rdf:RDF>");

    return s.toString();

  }  

 

  private String countryTag(String spatialId) {
    return "<rod:locality rdf:resource=\"" + spatialNamespace + spatialId + "\"/>";
  } 
}
