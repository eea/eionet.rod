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

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import eionet.rod.services.WebRODService;

import java.util.Hashtable;
import java.util.Vector;
import eionet.rod.services.ServiceException;
import javax.servlet.ServletConfig;

import eionet.rod.Constants;

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

public abstract class RDFServletAC extends HttpServlet implements Constants {

  protected String activitiesNamespace;
  protected String obligationsNamespace;
  protected String rodSchemaNamespace;

  protected static ResourceBundle props; 
  
  protected static final String rdfHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>";
  protected static final String rdfNameSpace = "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" ";
  protected static final String rdfSNameSpace = "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" ";

  public void init(  ServletConfig config ) throws ServletException {

    try {
        props = ResourceBundle.getBundle(PROP_FILE);
     } catch (MissingResourceException mre) {
       throw new ServletException("Properties file " + PROP_FILE + ".properties not found");
     }

    if (activitiesNamespace == null)
      activitiesNamespace = props.getString(ROD_URL_NS);

    if (obligationsNamespace == null)
      obligationsNamespace = props.getString(ROD_URL_RO_NS);

    if (rodSchemaNamespace == null)
      try {
        rodSchemaNamespace=props.getString("schame.namespace");
        //quite likely it will not change
      } catch (MissingResourceException mre ) {
        rodSchemaNamespace="http://rod.eionet.eu.int/schema.rdf";
      }

  }
  protected abstract String generateRDF(HttpServletRequest req) throws ServiceException;
  
  public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {

    res.setContentType("text/xml");  

    try {

      String rdf = generateRDF(req);
     
      res.getWriter().write( rdf) ;      
    } catch (ServiceException se ) {
      throw new ServletException( "Error getting values for activities " + se.toString(), se);
    }
  
  }

    protected static void _log(String s) { System.out.println("****** " + s);}
}
