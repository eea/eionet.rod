package eionet.rod.rdf;

import eionet.rod.services.ServiceException;
import eionet.rod.services.RODServices;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import eionet.rod.Constants;

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
      
    //String eventsNs = props.getString(Constants.ROD_URL_EVENTS);
    String actsUrl = props.getString(Constants.ROD_URL_ACTIVITIES);
    addChannelTag(s, actsUrl);

    String[][] acts = RODServices.getDbService().getIssueActivities(ids);

    s.append("<items><rdf:Seq>");
    for (int i= 0; i< acts.length; i++){
      String pk = acts[i][0];

      s.append("<rdf:li rdf:resource=\"").append(activitiesNamespace).append("/ra-")
        .append(pk).append("\"/>");
  
    } 
    s.append("</rdf:Seq></items>");  
    addChannelEnd(s);
    for (int i= 0; i< acts.length; i++){
      String pk = acts[i][0];
      String title = acts[i][1];
      String date = acts[i][2];
      String link = getActivityUrl(pk, acts[i][3] );
      
      s.append( "<item rdf:about=\"").append(activitiesNamespace).append("/ra-")
        .append(pk).append("\">")
        .append("<title>").append(title).append("</title>")
        .append("<link>").append(link).append("</link>");
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