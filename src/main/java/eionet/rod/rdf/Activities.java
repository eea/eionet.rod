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

import java.util.List;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import eionet.rod.RODUtil;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.ObligationRdfDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;


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

  private static String allNameSpaces =  rdfNameSpace +  rdfSNameSpace +
    "xmlns:dcterms='http://purl.org/dc/terms/'";
  
  public String getRdf(HttpServletRequest req) throws ServiceException {
	  return getRdf(req, null);
  }
  
  public String getRdf(HttpServletRequest req, ObligationRdfDTO obligation) throws ServiceException {
	  	try {
	  		props = ResourceBundle.getBundle(PROP_FILE);
	  	} catch (MissingResourceException mre) {
	  		mre.printStackTrace();
	  	}

		if (activitiesNamespace == null)
			activitiesNamespace = props.getString(ROD_URL_NS);
	
		if (instrumentsNamespace == null)
			try {
				instrumentsNamespace = props.getString(ROD_LI_NS);
			} catch (MissingResourceException mre ) {
				instrumentsNamespace="http://rod.eionet.europa.eu/instruments/";
			}
	
		if (issuesNamespace == null)
			try {
				issuesNamespace = props.getString(ROD_ISSUES_NS);
			} catch (MissingResourceException mre ) {
				issuesNamespace="http://rod.eionet.europa.eu/issues/";
			}
	
		if (spatialNamespace == null)
			try {
				spatialNamespace = props.getString("spatial.namespace");
			} catch (MissingResourceException mre ) {
				issuesNamespace="http://rod.eionet.europa.eu/spatial/";
			}
	
	
		if (obligationsNamespace == null)
			obligationsNamespace = props.getString(ROD_URL_RO_NS);

		if (rodSchemaNamespace == null)
			try {
				rodSchemaNamespace=props.getString("schema.namespace");
		        //quite likely it will not change
			} catch (MissingResourceException mre ) {
		        rodSchemaNamespace="http://rod.eionet.europa.eu/schema.rdf";
			}
		
			if(obligation != null)
				return generateRDFSingleObligation(obligation);
			else
				return generateRDF(req);
  	}
  
  	protected String generateRDFSingleObligation(ObligationRdfDTO obligation) throws ServiceException {
  		StringBuffer s = new StringBuffer();
	    s.append(rdfHeader);
	    s.append("<rdf:RDF ").append(" xmlns:rod=\"").append(rodSchemaNamespace).append("#\"")
	    .append(" ").append(allNameSpaces).append(">");

		StringBuffer content = genereteElement(obligation);
		s.append(content);

		s.append("</rdf:RDF>");

		return s.toString();
	}
  
 	protected String generateRDF(HttpServletRequest req) throws ServiceException {

 		StringBuffer s = new StringBuffer();
 		s.append(rdfHeader);
 		s.append("<rdf:RDF ").append(" xmlns:rod=\"").append(rodSchemaNamespace).append("#\"")
 		.append(" ").append(allNameSpaces).append(">");
    
 		List<ObligationRdfDTO> obligations = RODServices.getDbService().getObligationDao().getObligationsForRDF();

 		for(ObligationRdfDTO obligation : obligations){
 			StringBuffer element = genereteElement(obligation);
 			s.append(element);
 		}	

 		String[][] raIds=RODServices.getDbService().getObligationDao().getObligationIds();
 		
 		//issues list
 		String [][] issues = RODServices.getDbService().getIssueDao().getIssueIdPairs();
 		for (int i= 0; i< issues.length; i++) {
 			String pk = issues[i][0];
 			String name = issues[i][1];
 			s.append("<rod:Issue rdf:about=\"").append(issuesNamespace).append(pk).append("\">")
 			.append("<rdfs:label>").append(name).append("</rdfs:label>")
 			.append("</rod:Issue>");
      
 		}

 		for (int i= 0; i< raIds.length; i++) {
   
 			s.append("<rdf:Description rdf:about=\"").append(obligationsNamespace).append("/").append(raIds[i][0]).append("\">");
 			String[][] iIds = RODServices.getDbService().getIssueDao().getIssues(Integer.valueOf(raIds[i][0]));
 			for (int j=0; j<iIds.length; j++) 
 				s.append("<rod:issue rdf:resource=\"" + issuesNamespace + iIds[j][0] + "\"/>");
 				s.append("</rdf:Description>");         
 		}
    
 		s.append("</rdf:RDF>");

 		return s.toString();

 	}
  
  	private StringBuffer genereteElement(ObligationRdfDTO obligation) throws ServiceException {

  		int pk = obligation.getObligationId();
	    String title = obligation.getTitle();
	    int sourceId = obligation.getSourceId();
	    int clientId = obligation.getClientId();
	      
	    String validSince = obligation.getValidSince();
	    String terminated = obligation.getTerminated();
	    String comment = obligation.getComment();

	    String respRole = obligation.getResponsibleRole();
	    String nextDeadline = obligation.getNextDeadline();
	    String nextDeadline2 = obligation.getNextDeadline2();

	    String repFormat = obligation.getReportingFormat();
	    String formatName = obligation.getFormatName();
	    String repFormatUrl = obligation.getReportFormatUrl();      

	    String description = obligation.getDescription();  
	    String eea_primary = obligation.getEeaPrimary();
	    String dataUsedForUrl = obligation.getDataUsedForUrl();  

	    if ((formatName == null || formatName.equals("")) && (repFormatUrl != null && !repFormatUrl.equals("")))
	        formatName = repFormatUrl;
	    
	    if(terminated != null && terminated.equals("Y"))
	    	terminated = "1";
	    else
	    	terminated = "0";

	    String detailsUrl = "http://rod.eionet.europa.eu/obligations/"+pk;
	      
	    title = RODUtil.replaceTags(title, true, true);
	    description = RODUtil.replaceTags(description, true, true);
	    comment = RODUtil.replaceTags(comment, true, true);
	    respRole = RODUtil.replaceTags(respRole, true, true);
	    repFormat = RODUtil.replaceTags(repFormat, true, true);
	    formatName = RODUtil.replaceTags(formatName, true, true);
	    repFormatUrl = RODUtil.replaceTags(repFormatUrl, true, true);
	    detailsUrl = RODUtil.replaceTags(detailsUrl, true, true);
	    dataUsedForUrl = RODUtil.replaceTags(dataUsedForUrl, true, true);
	    
	    StringBuffer s = new StringBuffer();

	    s.append("<rod:Obligation rdf:about=\"").append(obligationsNamespace).append("/").append(pk).append("\">")
	    .append("<dcterms:title>").append(title).append("</dcterms:title>")
	    .append("<dcterms:abstract>").append(description).append("</dcterms:abstract>");
	    if(!RODUtil.nullString(validSince))
	    	s.append("<dcterms:valid>").append(validSince).append("</dcterms:valid>");        
	    s.append("<rod:terminated>").append(terminated).append("</rod:terminated>")                
	    .append("<rod:eea_primary>").append(eea_primary).append("</rod:eea_primary>");  
	    if(!RODUtil.isNullOrEmpty(comment))
	       	s.append("<rod:comment>").append(comment).append("</rod:comment>");
	    
	    if(!RODUtil.isNullOrEmpty(respRole))
	    	s.append("<rod:responsiblerole>").append(respRole).append("</rod:responsiblerole>");
	    
	    s.append("<rod:nextdeadline rdf:datatype=\"http://www.w3.org/2001/XMLSchema#date\">").append(nextDeadline).append("</rod:nextdeadline>")                        
	    .append("<rod:nextdeadline2 rdf:datatype=\"http://www.w3.org/2001/XMLSchema#date\">").append(nextDeadline2).append("</rod:nextdeadline2>");                

	    if(!RODUtil.isNullOrEmpty(repFormat))
	    	s.append("<rod:guidelines>").append(repFormat).append("</rod:guidelines>");
	    
	    s.append("<rod:instrument rdf:resource=\"" + instrumentsNamespace + sourceId + "\"/>");
	        
	    if (!repFormatUrl.equals(""))
	    	s.append("<rod:guidelines_url rdf:resource=\"" + repFormatUrl + "\"/>");
	    
	    s.append("<rod:requester rdf:resource=\"/clients/" + clientId + "\"/>");
	    List<ClientDTO> clients = RODServices.getDbService().getClientDao().getClients(new Integer(pk).toString());
	    if(clients != null){
	    	for(ClientDTO client : clients){
	    		s.append("<rod:otherClient rdf:resource=\"/clients/" + client.getClientId() + "\"/>");
	    	}
	    }
	    if(!RODUtil.isNullOrEmpty(dataUsedForUrl))
	    	s.append("<rod:dataUsedFor rdf:resource=\"" + dataUsedForUrl + "\"/>");
	    
	    List<Integer> voluntaryCountries = RODServices.getDbService().getSpatialDao().getObligationCountries(pk, true);
	    if(voluntaryCountries != null){
	    	for(Integer countryId : voluntaryCountries){
	    		s.append("<rod:voluntaryReporter rdf:resource=\"/spatial/" + countryId + "\"/>");
	    	}
	    }
	    
	    List<Integer> mandatoryCountries = RODServices.getDbService().getSpatialDao().getObligationCountries(pk, false);
	    if(mandatoryCountries != null){
	    	for(Integer countryId : mandatoryCountries){
	    		s.append("<rod:formalReporter rdf:resource=\"/spatial/" + countryId + "\"/>");
	    	}
	    }
	    
	    s.append("</rod:Obligation>");

		return s;
	}
}
