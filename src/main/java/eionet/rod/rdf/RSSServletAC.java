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
 * The Original Code is "ROD2"
 *
 * The Initial Developer of the Original Code is TietoEnator.
 * The Original Code code was developed for the European
 * Environment Agency (EEA) under the IDA/EINRC framework contract.
 *
 * Copyright (C) 2000-2014 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Kaido Laine (TietoEnator)
 * Contributor: Søren Roug
 */

package eionet.rod.rdf;

import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
import eionet.rod.services.ServiceException;

/**
 * Parent class for all RSS servlets.
 */
public abstract class RSSServletAC extends RDFServletAC implements Constants  {

    private static final long serialVersionUID = 1L;

    protected static final String EVENTS_NS = "http://purl.org/rss/1.0/modules/event/";
    protected static final String RSS_NS = "http://purl.org/rss/1.0/";

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("application/rss+xml;charset=UTF-8");
        try {
            generateRDF(req, res);
        } catch (ServiceException se) {
            throw new ServletException("Error getting values for events " + se.toString(), se);
        }

    }
    /**
     * Split param on comma and remove non-numeric values.
     *
     * @param param - The parameter provided by the user.
     * @return List of values
     */
    protected StringTokenizer tokenizeParam(String param) {
        if (param == null || param.length() == 0) {
            return null;
        }

        StringTokenizer uncleanTemp = new StringTokenizer(param, ",");
        StringBuffer cleanStr = new StringBuffer();
        if (uncleanTemp != null) {
            while (uncleanTemp.hasMoreTokens()) {
                String token = uncleanTemp.nextToken();
                if (isNumeric(token)) {
                    cleanStr.append(token);
                    cleanStr.append(" ");
                } else {
                    cleanStr.append("-1");
                    cleanStr.append(" ");
                }
            }
            if (cleanStr.toString() != null) {
                return new StringTokenizer(cleanStr.toString());
            }
        }
        return null;
    }

    /**
     * Checks if input string is a number.
     * FIXME: Should use RODUtil.isNumber() instead.
     */
    private boolean isNumeric(String inString) {
        CharacterIterator theIterator = new StringCharacterIterator(inString);

        for (char ch = theIterator.first(); ch != CharacterIterator.DONE; ch = theIterator.next()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }

}
