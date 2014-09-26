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

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import java.io.IOException;

/**
 * Write RSS for events.
 */
public class Events extends RSSServletAC {

    private static final long serialVersionUID = 1L;

    private static final long TEN_PCT_OF_MONTH_IN_DAYS = 3L;
    private static final long DAY_IN_MILLISECONDS = 86400000L;

    @Override
    protected void generateRDF(HttpServletRequest req, HttpServletResponse res) throws ServiceException, IOException {

        StringTokenizer issues = tokenizeParam(req.getParameter("issues"));
        StringTokenizer countries = tokenizeParam(req.getParameter("countries"));

        RDFUtil rdfOut = new RDFUtil(res.getWriter());
        rdfOut.addNamespace("ev", EVENTS_NS);
        rdfOut.setVocabulary(RSS_NS);
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
    }

    /**
     * Calculates if next deadline for an obligation should be listed in the RSS output.
     *
     * @param nd - next deadline as a string date
     * @param freq - Frequency - every X months.
     * @param date - misnamed - next deadline must be after this cut-off date.
     * @return true if nextdeadline is within 10 % of reporting period in the future.
     */
    public static boolean isUpcomingEvent(String nd, String freq, Date date) {
        int year = Integer.parseInt(nd.substring(0, 4)) - 1900;
        int month = Integer.parseInt(nd.substring(5, 7)) - 1;
        int day = Integer.parseInt(nd.substring(8, 10));
        Date nextDeadline = new Date(year, month, day);

        long nextDeadlineMillis = nextDeadline.getTime();

        int f = Integer.parseInt(freq);
        long tenPctMillis = Integer.parseInt(freq) * TEN_PCT_OF_MONTH_IN_DAYS * DAY_IN_MILLISECONDS;
        Date deadlineMinus10Pct = new Date(nextDeadlineMillis - tenPctMillis);

        if (nextDeadline.after(date) && deadlineMinus10Pct.before(date)) {
            return true;
        }
        return false;
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
