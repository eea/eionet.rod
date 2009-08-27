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



import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import eionet.rod.dto.ClientDTO;

import eionet.rod.services.ServiceException;
import eionet.rod.services.RODServices;

/**
 * <P>Servlet URL: <CODE>rdf</CODE></P>
 *
 * <P>Database tables involved: T_CLIENT</P>
 *
 * <P>XSL file used: <CODE>index.xsl</CODE><BR>
 * Query file used: <CODE>index.xrs</CODE></P>
 *
 * @author  Risto Alt
 * @version 1.0
 */
public class Clients extends RDFServletAC {

  private static String allNameSpaces =  rdfNameSpace +  rdfSNameSpace +
    dcNs + "xmlns:dcterms=\"http://purl.org/dc/terms/\"";
  
  public String getRdf(HttpServletRequest req) throws ServiceException {
	  	try {
	  		props = ResourceBundle.getBundle(PROP_FILE);
	  	} catch (MissingResourceException mre) {
	  		mre.printStackTrace();
	  	}

		if (activitiesNamespace == null)
			activitiesNamespace = props.getString(ROD_URL_NS);
	
		if (clientsNamespace == null)
			try {
				clientsNamespace = props.getString(ROD_LI_NS);
			} catch (MissingResourceException mre ) {
				clientsNamespace="http://rod.eionet.europa.eu/clients/";
			}

		if (rodSchemaNamespace == null)
			try {
				rodSchemaNamespace=props.getString("schema.namespace");
		        //quite likely it will not change
			} catch (MissingResourceException mre ) {
		        rodSchemaNamespace="http://rod.eionet.eu.int/schema.rdf";
			}
		
		return generateRDF(req);
	}


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
    List<ClientDTO> clients = RODServices.getDbService().getClientDao().getAllClients();
    
    for(Iterator<ClientDTO> it = clients.iterator(); it.hasNext();){
    	
    	ClientDTO client = it.next();
    	
    	s.append("<rod:Client rdf:about=\"" + clientsNamespace + client.getClientId() + "\">")
        	.append("<rod:clientName>").append(client.getName()).append("</rod:clientName>")
        	.append("<rod:clientAcronym>").append(client.getAcronym()).append("</rod:clientAcronym>")        
        	.append("<rod:clientUrl>").append(client.getUrl()).append("</rod:clientUrl>")        
        	.append("<rod:clientAddress>").append(client.getAddress()).append("</rod:clientAddress>")
        	.append("<rod:clientEmail>").append(client.getEmail()).append("</rod:clientEmail>")
        	.append("<rod:clientDescription>").append(client.getDescription()).append("</rod:clientDescription>")
        	.append("<rod:clientPostalCode>").append(client.getPostalCode()).append("</rod:clientPostalCode>")
        	.append("<rod:clientCity>").append(client.getCity()).append("</rod:clientCity>")
        	.append("<rod:clientCountry>").append(client.getCountry()).append("</rod:clientCountry>")
        	.append("<rod:clientShortName>").append(client.getShortName()).append("</rod:clientShortName>")
    	.append("</rod:Client>");        
    }
        
    s.append("</rdf:RDF>");

    return s.toString();

  }  

 
}
