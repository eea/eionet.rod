package eionet.rod.rdf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.ServletException;

import java.io.IOException;

import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.services.Config;


public abstract class RSSServletAC extends RDFServletAC  {

  protected static final String eventsNs = " xmlns:ev=\"http://purl.org/rss/1.0/modules/event/\" ";
  protected static final String rssNs = " xmlns=\"http://purl.org/rss/1.0/\" "  ;
  
 protected void addChannelTag(StringBuffer s, String ns ) {
    s.append("<channel rdf:about=\"").append(ns).append("\">");
 }

 protected void addChannelEnd(StringBuffer s) {
    s.append("</channel>");
 }

 //protected abstract String generateRSS( ) throws ServiceException;
/* 
 private String generateRSS( ) throws ServiceException {
    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    
    s.append("<rdf:RDF ").append(rdfNameSpace)
      .append(eventsNs).append(rssNs)
      .append(">");
      
    String eventsNs = props.getString(Constants.ROD_URL_EVENTS);
    addChannelTag(s, eventsNs);

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

  }  */

 protected String getObligationUrl(String id, String aid){
    String url = props.getString( Config.ROD_URL_DOMAIN) + "/" + Config.URL_SERVLET + "?" + 
      Config.URL_ACTIVITY_ID + "=" + id + "&amp;" + Config.URL_ACTIVITY_AID + "=" + aid + "&amp;" +
      Config.URL_ACTIVITY_RMODE;
    return url;
}
 protected String getActivityUrl(String id, String aid){
    String url = props.getString( Config.ROD_URL_DOMAIN) + "/" + Config.URL_SERVLET + "?" + 
      Config.URL_ACTIVITY_ID + "=" + id + "&amp;" + Config.URL_ACTIVITY_AID + "=" + aid + "&amp;" +
      Config.URL_ACTIVITY_AMODE;
    return url;
//http://rod.eionet.eu.int/show.jsv?id=15&amp;aid=170&amp;mode=A    
 }


 public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    res.setContentType("text/xml");  
    try {

      String rss = generateRDF(req);

      res.getWriter().write( rss) ;      

    } catch (ServiceException se ) {
      throw new ServletException( "Error getting values for events " + se.toString(), se);
    }
  
  }
}