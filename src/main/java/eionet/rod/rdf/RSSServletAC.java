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
 * The Original Code is "NaMod project".
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
import eionet.rod.services.ServiceException;

/**
 * Parent class for all RSS servlets
 */
public abstract class RSSServletAC extends RDFServletAC implements Constants  {

    private static final long serialVersionUID = 1L;

    protected static final String eventsNs = "http://purl.org/rss/1.0/modules/event/";
    protected static final String rssNs = "http://purl.org/rss/1.0/"  ;

    public void doGet(HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
        res.setContentType("application/rss+xml;charset=UTF-8");
        try {
            generateRDF(req, res);
        } catch (ServiceException se ) {
            throw new ServletException( "Error getting values for events " + se.toString(), se);
        }

    }
}
