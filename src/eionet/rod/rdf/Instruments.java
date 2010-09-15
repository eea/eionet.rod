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

import eionet.rod.RODUtil;
import eionet.rod.dto.InstrumentFactsheetDTO;
import eionet.rod.dto.SourceLinksDTO;

import java.util.Hashtable;
import java.util.Vector;
import eionet.rod.services.ServiceException;
import eionet.rod.services.RODServices;

/**
 * <P>Servlet URL: <CODE>rdf</CODE></P>
 *
 * <P>Database tables involved: T_ACTIVITY</P>
 *
 * <P>XSL file used: <CODE>index.xsl</CODE><BR>
 * Query file used: <CODE>index.xrs</CODE></P>
 *
 * @author  Kaido Laine
 * @version 1.0
 */
public class Instruments extends RDFServletAC {

  private static final String  actPropName = "activity";

  private static String allNameSpaces =  rdfNameSpace +  rdfSNameSpace +
    dcNs + "xmlns:dcterms=\"http://purl.org/dc/terms/\"";
  
  public String getRdf(HttpServletRequest req) throws ServiceException {
	  return getRdf(req, null);
  }
  
  public String getRdf(HttpServletRequest req, InstrumentFactsheetDTO instrument) throws ServiceException {
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
		
		if(instrument != null)
			return generateRDFSingleInstrument(instrument);
		else
			return generateRDF(req);
	}

  protected String generateRDFSingleInstrument(InstrumentFactsheetDTO instrument) throws ServiceException {
	  StringBuffer s = new StringBuffer();
	  s.append(rdfHeader);
	  s.append("<rdf:RDF ")
	  .append(" xmlns:rod=\"").append(rodSchemaNamespace).append("#\"")
	  .append(" ").append(allNameSpaces).append(">");
	  
	  Integer pk = instrument.getSourceId();
      String source_code = instrument.getSourceCode();
      Integer client_id = instrument.getClientId();
      String title = instrument.getSourceAlias();
      String legalName = instrument.getSourceTitle();
      String lastUpdate = instrument.getSourceLastUpdate();
      String url = instrument.getSourceUrl();      
      String abstr = instrument.getSourceAbstract();      
      String issuedBy = instrument.getClientName();      
      String celexRef = instrument.getSourceCelexRef();    

      //show legal name if short name is empty
      title = (RODUtil.nullString(title) ? legalName : title);

      s.append("<rod:Instrument rdf:about=\"" + instrumentsNamespace + pk + "\">")
      .append("<dcterms:alternative>").append(RODUtil.replaceTags(title,true,true)).append("</dcterms:alternative>")
      .append("<rdfs:label>").append(RODUtil.replaceTags(title,true,true)).append("</rdfs:label>")        
      .append("<dc:title>").append(RODUtil.replaceTags(legalName,true,true)).append("</dc:title>")        
      .append("<dcterms:modified>").append(lastUpdate).append("</dcterms:modified>")
      .append("<rod:celexref>").append(RODUtil.replaceTags(celexRef,true,true)).append("</rod:celexref>")
      .append("<dc:identifier>").append(source_code).append("</dc:identifier>")
      .append("<rod:issuer rdf:resource=\"http://rod.eionet.europa.eu/clients/").append(client_id).append("\"/>");
      
      if (!RODUtil.nullString(abstr))
    	  s.append("<dcterms:abstract>").append(abstr).append("</dcterms:abstract>");
        
      if (!RODUtil.nullString(url))
    	  s.append("<rod:instrumentURL>"+url+"</rod:instrumentURL>");

      if (!RODUtil.nullString(issuedBy))
    	  s.append("<dc:creator>").append(issuedBy).append("</dc:creator>");
          
      s.append("</rod:Instrument>").append("</rdf:RDF>");
	  
	  return s.toString();
  }

  protected String generateRDF(HttpServletRequest req) throws ServiceException {

    StringBuffer s = new StringBuffer();
    s.append(rdfHeader);
    s.append("<rdf:RDF ")
    //.append(" xmlns=\"").append(activitiesNamespace).append("#\"")
    .append(" xmlns:rod=\"").append(rodSchemaNamespace).append("#\"")
    .append(" ")
    .append(allNameSpaces)
    .append(">");


    //WebRODService wSrv = new WebRODService();
    Vector instruments = RODServices.getDbService().getSourceDao().getInstruments();
    
    for (int i= 0; i< instruments.size(); i++){
      Hashtable li=(Hashtable)instruments.elementAt(i);
      String pk=(String)li.get("PK_SOURCE_ID");
      String source_code=(String)li.get("SOURCE_CODE");
      String client_id=(String)li.get("FK_CLIENT_ID");
      String title=(String)li.get("ALIAS");
      String legalName=(String)li.get("TITLE");
      String lastUpdate=(String)li.get("LAST_UPDATE");
      String url=(String)li.get("URL");      
      String abstr=(String)li.get("ABSTRACT");      
      String issuedBy=(String)li.get("ISSUED_BY");      
      String celexRef=(String)li.get("CELEX_REF");    

      //show legal name if short name is empty
      title=(RODUtil.nullString(title) ? legalName : title);

      s.append("<rod:Instrument rdf:about=\"" + instrumentsNamespace + pk + "\">")
        .append("<dcterms:alternative>").append(RODUtil.replaceTags(title,true,true)).append("</dcterms:alternative>")
        .append("<rdfs:label>").append(RODUtil.replaceTags(title,true,true)).append("</rdfs:label>")        
        .append("<dc:title>").append(RODUtil.replaceTags(legalName,true,true)).append("</dc:title>")        
        .append("<dcterms:modified>").append(lastUpdate).append("</dcterms:modified>")
        .append("<rod:celexref>").append(RODUtil.replaceTags(celexRef,true,true)).append("</rod:celexref>")
        .append("<dc:identifier>").append(source_code).append("</dc:identifier>")
        .append("<rod:issuer rdf:resource=\"http://rod.eionet.europa.eu/clients/").append(client_id).append("\"/>");
      
        if (!RODUtil.nullString(abstr))
          s.append("<dcterms:abstract>").append(abstr).append("</dcterms:abstract>");
        
        if (!RODUtil.nullString(url))
        	s.append("<rod:instrumentURL>"+url+"</rod:instrumentURL>");

        if (!RODUtil.nullString(issuedBy))
          s.append("<dc:creator>").append(issuedBy).append("</dc:creator>");
          
        s.append("</rod:Instrument>");        
    }
    
    List<SourceLinksDTO> links = RODServices.getDbService().getSourceDao().getSourceLinks();
    for(Iterator<SourceLinksDTO> it = links.iterator(); it.hasNext();){
    	SourceLinksDTO sourceLink = it.next();
    	if(sourceLink != null){
	    	s.append("<rdf:Description rdf:about=\"http://rod.eionet.europa.eu/instruments/").append(sourceLink.getChildId()).append("\">")
	    		.append("<rod:parentInstrument rdf:resource=\"http://rod.eionet.europa.eu/instruments/")
	    			.append(sourceLink.getParentId()).append("\"/>")
	    	.append("</rdf:Description>");
    	}
    }
    
    s.append("</rdf:RDF>");

    return s.toString();

  }  

 
}
