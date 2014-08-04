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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import java.io.IOException;

/**
 * Write RSS for events.
 */
public class Events extends RSSServletAC {

    private static final long serialVersionUID = 1L;

    protected String generateRDF(HttpServletRequest req, HttpServletResponse res) throws ServiceException {

        StringTokenizer issues = tokenizeParam(req.getParameter("issues"));
        StringTokenizer countries = tokenizeParam(req.getParameter("countries"));

        try {
            RDFUtil rdfOut = new RDFUtil(res.getWriter());
            rdfOut.addNamespace("ev", "http://purl.org/rss/1.0/modules/event/");
            rdfOut.setVocabulary("http://purl.org/rss/1.0/");
            rdfOut.writeRdfHeader();

            String eventsUrl = props.getString(Constants.ROD_URL_EVENTS);
            rdfOut.writeStartResource("channel", eventsUrl);

            String[][] events = RODServices.getDbService().getObligationDao().getActivityDeadlines(issues, countries);

            Vector eventsVec = new Vector();
            Date currentDate = getTodaysDate();
            for (int i = 0; i < events.length; i++) {
                String nd = events[i][2];
                String freq = events[i][3];
                boolean isUpcoming = isUpcomingEvent(nd, freq, currentDate);
                if (isUpcoming) {
                    Hashtable hash = new Hashtable();
                    hash.put("pk", events[i][0]);
                    hash.put("title", events[i][1]);
                    hash.put("next_deadline", nd);
                    hash.put("freq", freq);
                    hash.put("link", events[i][4]);
                    hash.put("desc", events[i][5]);
                    eventsVec.add(hash);
                }
            }

            String[][] upcomingEvents = new String[eventsVec.size()][6];
            int cnt = 0;
            for (Enumeration en = eventsVec.elements(); en.hasMoreElements();) {
                Hashtable h = (Hashtable) en.nextElement();
                upcomingEvents[cnt][0] = (String) h.get("pk");
                upcomingEvents[cnt][1] = (String) h.get("title");
                upcomingEvents[cnt][2] = (String) h.get("next_deadline");
                upcomingEvents[cnt][3] = (String) h.get("freq");
                upcomingEvents[cnt][4] = (String) h.get("link");
                upcomingEvents[cnt][5] = (String) h.get("desc");

                cnt++;
            }

            rdfOut.writeStartLiteral("items");
            rdfOut.writeStartResource("rdf:Seq");
            for (int i = 0; i < upcomingEvents.length; i++) {
                String pk = upcomingEvents[i][0];

                rdfOut.writeReference("rdf:li", obligationsNamespace + "/" + pk);
            }
            rdfOut.writeEndResource("rdf:Seq");
            rdfOut.writeEndLiteral("items");
            rdfOut.writeEndResource("channel");
            for (int i = 0; i < upcomingEvents.length; i++) {
                String pk = upcomingEvents[i][0];
                String title = "Deadline for Reporting Obligation: " + upcomingEvents[i][1];
                String date = upcomingEvents[i][2];
                String link = getActivityUrl(pk, upcomingEvents[i][4]);
                String description = upcomingEvents[i][5];

                rdfOut.writeStartResource("item", obligationsNamespace + "/" + pk);
                rdfOut.writeLiteral("title", title);
                rdfOut.writeLiteral("link", link);
                rdfOut.writeLiteral("description", description);
                rdfOut.writeLiteral("ev:startdate", date);

                rdfOut.writeEndResource("item");
            }

            rdfOut.writeRdfFooter();
        } catch (IOException e) {
        }

        return "";
    }

    /**
     * Checks if input string is a number.
     * FIXME: Should use RODUtil.isNumber() instead.
     */
    public static boolean isNumeric(String inString) {
        CharacterIterator theIterator = new StringCharacterIterator(inString);

        for (char ch = theIterator.first(); ch != CharacterIterator.DONE; ch = theIterator.next()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Calculates if next deadline for an obligation should be listed in the RSS output.
     *
     * @param date - misnamed - next deadline must be after this cut-off date.
     */
    public static boolean isUpcomingEvent(String nd, String freq, Date date) {
        int year = Integer.parseInt(nd.substring(0, 4)) - 1900;
        int month = Integer.parseInt(nd.substring(5, 7)) - 1;
        int day = Integer.parseInt(nd.substring(8, 10));
        Date nextDeadline = new Date(year, month, day);

        long nextDeadlineMillis = nextDeadline.getTime();

        int f = Integer.parseInt(freq);
        int period = new Double(3.0 * f).intValue();
        //TODO: long period = 3L * Integer.parseInt(freq);
        long periodMillis =
            (new Long(period).longValue() * new Long(24).longValue() * new Long(3600).longValue() * new Long(1000).longValue());
        //TODO: long periodMillis = period * 24L * 3600L * 1000L;
        Date periodStartDate = new Date(nextDeadlineMillis - periodMillis);

        if (nextDeadline.after(date) && periodStartDate.before(date)) {
            return true;
        }
        return false;
    }

    /**
     * Split param on comma and remove non-numeric values.
     *
     * @param param - The parameter provided by the user.
     * @return List of values
     */ 
    private StringTokenizer tokenizeParam(String param) {
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
     * Get the date of today from the computer.
     *
     * @return today's date
     */
    protected Date getTodaysDate() {
        return new Date();
    }
}
