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
public class InstrumentsRSS extends RSSServletAC {

  protected String generateRDF(HttpServletRequest req) throws ServiceException {


    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    
    s.append("<rdf:RDF ").append(rdfNameSpace)
      .append(rssNs)
      .append(eventsNs)
      .append(">");
      
    String lisUrl = "http://rod.eionet.eu.int/instruments.rss";
    try {
      lisUrl=props.getString(Constants.ROD_URL_INSTRUMENTS);
    } catch (Exception e ) {
      //use default
    }
    addChannelTag(s, lisUrl);

    String[][] lis = RODServices.getDbService().getInstrumentsRSS();

    s.append("<items><rdf:Seq>");
    for (int i= 0; i< lis.length; i++){
      String pk = lis[i][0];

      s.append("<rdf:li rdf:resource=\"").append(instrumentsNamespace)
        .append(pk).append("\"/>");
  
    } 
    s.append("</rdf:Seq></items>");  
    addChannelEnd(s);

    for (int i= 0; i< lis.length; i++){
      String pk = lis[i][0];
      String title = lis[i][1];
      String link = lis[i][2];
      String description = lis[i][3];
      
      s.append( "<item rdf:about=\"").append(instrumentsNamespace)
        .append(pk).append("\">")
        .append("<title>").append(title).append("</title>")
        .append("<link>").append(link).append("</link>")
        .append("<description>").append(description).append("</description>");

      s.append("</item>");
    } 
    
    s.append("</rdf:RDF>");

//System.out.println(s.toString());

    return s.toString();
  }

}