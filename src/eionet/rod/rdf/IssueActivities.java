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

package eionet.rod.rdf;

import eionet.rod.services.ServiceException;
import eionet.rod.services.RODServices;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import eionet.rod.Constants;

/**
* Activities RSS
*/
public class IssueActivities extends RSSServletAC {

  protected String generateRDF(HttpServletRequest req) throws ServiceException {

    String idsParam = req.getParameter("issues");
    StringTokenizer ids = null;

    if (idsParam!=null)
      ids = new StringTokenizer(idsParam, ",");


    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    
    s.append("<rdf:RDF ").append(rdfNameSpace)
      .append(rssNs)
      .append(eventsNs)
      .append(">");
      
    String actsUrl = props.getString(Constants.ROD_URL_ACTIVITIES);
    addChannelTag(s, actsUrl);

    String[][] acts = RODServices.getDbService().getIssueActivities(ids);

    s.append("<items><rdf:Seq>");
    for (int i= 0; i< acts.length; i++){
      String pk = acts[i][0];

      s.append("<rdf:li rdf:resource=\"").append(obligationsNamespace).append("/")
        .append(pk).append("\"/>");
  
    } 
    s.append("</rdf:Seq></items>");  
    addChannelEnd(s);
    for (int i= 0; i< acts.length; i++){
      String pk = acts[i][0];
      String title = acts[i][1];
      String date = acts[i][2];
      String link = getActivityUrl(pk, acts[i][3] );
      String description = acts[i][4];
      
      s.append( "<item rdf:about=\"").append(obligationsNamespace).append("/")
        .append(pk).append("\">")
        .append("<title>").append(title).append("</title>")
        .append("<link>").append(link).append("</link>")
        .append("<description>").append(description).append("</description>");

        if (date != null)
          s.append("<ev:startdate>").append(date).append("</ev:startdate>");
        else
          s.append("<ev:startdate/>");

      s.append("</item>");
    }
    
    s.append("</rdf:RDF>");

    return s.toString();
  }

}