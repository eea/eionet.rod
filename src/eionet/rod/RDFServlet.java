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

package eionet.rod;

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
import javax.servlet.ServletContext;
import java.net.MalformedURLException;
//import com.tee.xmlserver.*;

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
public class RDFServlet extends BaseServletAC {

  private String ns;
  private static ResourceBundle props; 
  
  private static final String rdfHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
  private static final String  actsSchema = "<eor:Schema rdf:about=\"\"><rdf:value>Activities schema</rdf:value></eor:Schema>";
  private static final String  actPropName = "activity";
  
  private static String rdfNameSpaces = "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"  xmlns:eor=\"http://dublincore.org/2000/03/13/eor#\"" ;


  public void init(  ServletConfig config ) throws ServletException {

    try {
        props = ResourceBundle.getBundle(eionet.rod.services.Config.PROP_FILE);
     } catch (MissingResourceException mre) {
       throw new ServletException("Properties file " + eionet.rod.services.Config.PROP_FILE + ".properties not found");
     }

    if (ns == null)
      ns = props.getString(eionet.rod.services.Config.ROD_URL_NS);

    
    //_log("host = " + domain);    
    super.init( config );
  }
  private String generateRDF( ) throws ServiceException {
    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    s.append("<rdf:RDF ").append(rdfNameSpaces)
    .append(" xmlns=\"").append(ns).append("#\"")
    .append(">");

    s.append(actsSchema);

    s.append("<rdf:Property rdf:ID=\"").append( actPropName).append("\">");
    s.append("<rdf:value>Activity name</rdf:value>");
    s.append("<rdfs:label>Activity name</rdfs:label>");
    s.append("<rdfs:range rdf:resource=\"#Activity\"/>");
    s.append("</rdf:Property>");
    
    
    Vector acts = WebRODService.getActivities();
    
    for (int i= 0; i< acts.size(); i++){
      Hashtable act = (Hashtable)acts.elementAt(i);
      String pk = (String)act.get("PK_RA_ID");
      String title = (String)act.get("TITLE");

      s.append("<Activity rdf:ID=\"ra-").append(pk).append("\">")
        .append("<rdf:value>").append(title).append("</rdf:value>")
        .append("<rdfs:label>").append(title).append("</rdfs:label>")      
        .append("</Activity>");

    }
    
    s.append("</rdf:RDF>");

    return s.toString();

  }  
  public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
    res.setContentType("text/xml");  
    try {

      String servletName = req.getServletPath();
      //_log("********* " + servletName);      
      String rdf = generateRDF();
  //_log(rdf);
      res.getWriter().write( rdf) ;      
    } catch (ServiceException se ) {
      throw new ServletException( "Error getting values for activities " + se.toString(), se);
    }
  
  }


    private static void _log(String s) { System.out.println("****** " + s);}
}