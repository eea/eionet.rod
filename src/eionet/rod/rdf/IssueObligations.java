package eionet.rod.rdf;

import eionet.rod.services.ServiceException;
import eionet.rod.services.RODServices;

import javax.servlet.http.HttpServletRequest;

import eionet.rod.Constants;
import java.util.StringTokenizer;

public class IssueObligations extends RSSServletAC {

  //private final String obligationsNs = "ooo";

  protected String generateRDF(HttpServletRequest req) throws ServiceException {
    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    
    s.append("<rdf:RDF ").append(rdfNameSpace)
      .append(rssNs)
      .append(">");
      
    String obligationsUrl = props.getString(Constants.ROD_URL_OBLIGATIONS);
    addChannelTag(s, obligationsUrl);

    String idsParam = req.getParameter("issues");
    StringTokenizer ids = null;

    if (idsParam!=null)
      ids = new StringTokenizer(idsParam, ",");
    
    String[][] obligations = RODServices.getDbService().getIssueObligations(ids);

    s.append("<items><rdf:Seq>");
    for (int i= 0; i< obligations.length; i++){
      String pk = obligations[i][0];

      s.append("<rdf:li rdf:resource=\"").append(obligationsNamespace).append("/ro-")
        .append(pk).append("\"/>");
  
    } 
    s.append("</rdf:Seq></items>");  
    
    addChannelEnd(s);

    for (int i= 0; i< obligations.length; i++){

      String pk = obligations[i][0];
      String title = obligations[i][1];      
      String description =obligations[i][2] ;      
      String link = getObligationUrl(pk, obligations[i][3] );      
/*      
      String title = "Deadline for Reporting Activity: " + events[i][1];
      String date = events[i][2];
      String link = getActivityUrl(pk, events[i][3] );
*/      
      s.append( "<item rdf:about=\"").append(obligationsNamespace).append("/ro-")
        .append(pk).append("\">")
        .append("<title>").append(title).append("</title>")
        .append("<link>").append(link).append("</link>")
        .append("<description>").append(description).append("</description>");
        //.append("<link>").append(link).append("</link>")
        //.append("<ev:startdate>").append(date).append("</ev:startdate>");

      s.append("</item>");
    } 
    
    s.append("</rdf:RDF>");

    return s.toString();
  }

}