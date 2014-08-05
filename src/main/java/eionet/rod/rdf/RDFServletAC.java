/*
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

import java.io.IOException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
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

public abstract class RDFServletAC extends HttpServlet implements Constants {

    private static final long serialVersionUID = 1L;

    protected static final String RDF_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
        + " <rdf:RDF xmlns:foaf=\"http://xmlns.com/foaf/0.1/\"\n"
        + " xmlns:owl=\"http://www.w3.org/2002/07/owl#\"\n"
        + " xmlns=\"http://rod.eionet.europa.eu/schema.rdf#\"\n"
        + " xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\"\n"
        + " xmlns:cc=\"http://creativecommons.org/ns#\"\n"
        + " xmlns:skos=\"http://www.w3.org/2004/02/skos/core#\"\n"
        + " xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"
        + " xmlns:xsd=\"http://www.w3.org/2001/XMLSchema#\"\n"
        + " xmlns:dcterms=\"http://purl.org/dc/terms/\"\n"
        + " xmlns:geo=\"http://www.w3.org/2003/01/geo/wgs84_pos#\"\n"
        + " xml:base=\"http://rod.eionet.europa.eu/\">\n";

    protected static final String RDF_FOOTER = "</rdf:RDF>";

    protected String activitiesNamespace;

    protected String obligationsNamespace;
    protected String instrumentsNamespace;
    protected String clientsNamespace;
    protected String rodSchemaNamespace;


    protected String issuesNamespace;
    protected String spatialNamespace;

    protected static ResourceBundle props;

    protected static final String rdfHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    protected static final String rdfNameSpace = "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" ";
    protected static final String rdfSNameSpace = "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\" ";

    protected static final String dcNs = " xmlns:dc=\"http://purl.org/dc/elements/1.1/\" ";

    @Override
    public void init(ServletConfig config) throws ServletException {

        try {
            props = ResourceBundle.getBundle(PROP_FILE);
        } catch (MissingResourceException mre) {
            throw new ServletException("Properties file " + PROP_FILE + ".properties not found");
        }

        if (activitiesNamespace == null) {
            activitiesNamespace = props.getString(ROD_URL_NS);
        }

        if (instrumentsNamespace == null) {
            try {
                instrumentsNamespace = props.getString(ROD_LI_NS);
            } catch (MissingResourceException mre) {
                //TODO: No defaulting. Let exception be thrown
                instrumentsNamespace = "http://rod.eionet.europa.eu/instruments/";
            }
        }

        if (clientsNamespace == null) {
            try {
                clientsNamespace = props.getString(ROD_CL_NS);
            } catch (MissingResourceException mre) {
                //TODO: No defaulting. Let exception be thrown
                clientsNamespace = "http://rod.eionet.europa.eu/clients/";
            }
        }

        if (issuesNamespace == null) {
            try {
                issuesNamespace = props.getString(ROD_ISSUES_NS);
            } catch (MissingResourceException mre) {
                //TODO: No defaulting. Let exception be thrown
                issuesNamespace = "http://rod.eionet.europa.eu/issues/";
            }
        }

        if (spatialNamespace == null) {
            try {
                spatialNamespace = props.getString("spatial.namespace");
            } catch (MissingResourceException mre) {
                //TODO: No defaulting. Let exception be thrown
                issuesNamespace = "http://rod.eionet.europa.eu/spatial/";
            }
        }


        if (obligationsNamespace == null) {
            obligationsNamespace = props.getString(ROD_URL_RO_NS);
        }

        if (rodSchemaNamespace == null) {
            try {
                rodSchemaNamespace=props.getString("schema.namespace");
                //quite likely it will not change
            } catch (MissingResourceException mre) {
                rodSchemaNamespace = "http://rod.eionet.europa.eu/schema.rdf";
            }
        }
    }

    protected abstract String generateRDF(HttpServletRequest req, HttpServletResponse res) throws ServiceException;

    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        res.setContentType("application/rdf+xml;charset=UTF-8");
        try {
            String rdf = generateRDF(req, res);
            res.getWriter().write(rdf) ;
        } catch (ServiceException se) {
            throw new ServletException("Error getting values for activities " + se.toString(), se);
        }
    }

    protected String getActivityUrl(String id, String aid) {
        String url = props.getString(ROD_URL_DOMAIN) + URL_SERVLET + "/" + id;
        return url;

    }

}
