package eionet.rod.rdf;

import eionet.rod.Constants;
import eionet.rod.services.ServiceException;
import eionet.rod.services.RODServices;

import javax.servlet.http.HttpServletRequest;

public class Events extends RSSServletAC {

  protected String generateRDF(HttpServletRequest req) throws ServiceException {
    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    
    s.append("<rdf:RDF ").append(rdfNameSpace)
      .append(eventsNs).append(rssNs)
      .append(">");
      
    String eventsUrl = props.getString(Constants.ROD_URL_EVENTS);
    addChannelTag(s, eventsUrl);

    String[][] events = RODServices.getDbService().getActivityDeadlines();

    s.append("<items><rdf:Seq>");
    for (int i= 0; i< events.length; i++){
      String pk = events[i][0];

      s.append("<rdf:li rdf:resource=\"").append(activitiesNamespace).append("/ra-")
        .append(pk).append("\"/>");
  
    } 
    s.append("</rdf:Seq></items>");  
    addChannelEnd(s);
    for (int i= 0; i< events.length; i++){
      String pk = events[i][0];
      String title = "Deadline for Reporting Activity: " + events[i][1];
      String date = events[i][2];
      String link = getActivityUrl(pk, events[i][3] );
      
      s.append( "<item rdf:about=\"").append(activitiesNamespace).append("/ra-")
        .append(pk).append("\">")
        .append("<title>").append(title).append("</title>")
        .append("<link>").append(link).append("</link>")
        .append("<ev:startdate>").append(date).append("</ev:startdate>");

      s.append("</item>");
    }
    
    s.append("</rdf:RDF>");

    return s.toString();
  }

}