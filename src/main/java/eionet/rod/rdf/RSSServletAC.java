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

    protected static final String eventsNs = " xmlns:ev=\"http://purl.org/rss/1.0/modules/event/\" ";
    protected static final String rssNs = " xmlns=\"http://purl.org/rss/1.0/\" "  ;

    protected void addChannelTag(StringBuffer s, String ns ) {
        s.append("<channel rdf:about=\"").append(ns).append("\">");
    }

    protected void addChannelEnd(StringBuffer s) {
        s.append("</channel>");
    }


    /* protected String getObligationUrl(String id, String aid) {
    String url = props.getString(ROD_URL_DOMAIN) + URL_SERVLET + id;
      URL_ACTIVITY_RMODE;
    return url;
} */
    /* protected String getActivityUrl(String id, String aid) {
    String url = props.getString(ROD_URL_DOMAIN) + URL_SERVLET + id;
    return url;

 } */


    public void doGet( HttpServletRequest req, HttpServletResponse res ) throws ServletException, IOException {
        res.setContentType("application/rss+xml;charset=UTF-8");
        try {

            String rss = generateRDF(req, res);

            res.getWriter().write( rss) ;

        } catch (ServiceException se ) {
            throw new ServletException( "Error getting values for events " + se.toString(), se);
        }

    }
}
