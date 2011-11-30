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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

public class Events extends RSSServletAC {

    private static final long serialVersionUID = 1L;

    protected String generateRDF(HttpServletRequest req) throws ServiceException {

        String issuesParam = req.getParameter("issues");
        StringTokenizer issuesTemp = null;
        StringTokenizer issues = null;

        String countriesParam = req.getParameter("countries");
        StringTokenizer countriesTemp = null;
        StringTokenizer countries = null;

        if (issuesParam != null && issuesParam.length() > 0)
            issuesTemp = new StringTokenizer(issuesParam, ",");

        if (countriesParam != null && countriesParam.length() > 0)
            countriesTemp = new StringTokenizer(countriesParam, ",");

        StringBuffer strIssues = new StringBuffer();
        if (issuesTemp != null) {
            while (issuesTemp.hasMoreTokens()) {
                String token = issuesTemp.nextToken();
                if (isNumeric(token)) {
                    strIssues.append(token);
                    strIssues.append(" ");
                } else {
                    strIssues.append("-1");
                    strIssues.append(" ");
                }
            }
            if (strIssues.toString() != null)
                issues = new StringTokenizer(strIssues.toString());
        }

        StringBuffer strCountries = new StringBuffer();
        if (countriesTemp != null) {
            while (countriesTemp.hasMoreTokens()) {
                String token = countriesTemp.nextToken();
                if (isNumeric(token)) {
                    strCountries.append(token);
                    strCountries.append(" ");
                } else {
                    strCountries.append("-1");
                    strCountries.append(" ");
                }
            }
            if (strCountries.toString() != null)
                countries = new StringTokenizer(strCountries.toString());
        }

        StringBuffer s = new StringBuffer();
        s.append(rdfHeader);

        s.append("<rdf:RDF ").append(rdfNameSpace).append(eventsNs).append(rssNs).append(">");

        String eventsUrl = props.getString(Constants.ROD_URL_EVENTS);
        addChannelTag(s, eventsUrl);

        String[][] events = RODServices.getDbService().getObligationDao().getActivityDeadlines(issues, countries);
        Vector eventsVec = new Vector();
        Date currentDate = new Date();
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

        s.append("<items><rdf:Seq>");
        for (int i = 0; i < upcomingEvents.length; i++) {
            String pk = upcomingEvents[i][0];

            s.append("<rdf:li rdf:resource=\"").append(obligationsNamespace).append("/").append(pk).append("\"/>");

        }
        s.append("</rdf:Seq></items>");
        addChannelEnd(s);
        for (int i = 0; i < upcomingEvents.length; i++) {
            String pk = upcomingEvents[i][0];
            String title = "Deadline for Reporting Obligation: " + upcomingEvents[i][1];
            String date = upcomingEvents[i][2];
            String link = getActivityUrl(pk, upcomingEvents[i][4]);
            String description = upcomingEvents[i][5];

            s.append("<item rdf:about=\"").append(obligationsNamespace).append("/").append(pk).append("\">").append("<title>")
            .append(RODUtil.replaceTags(title, true, true)).append("</title>").append("<link>")
            .append(RODUtil.replaceTags(link, true, true)).append("</link>").append("<description>")
            .append(RODUtil.replaceTags(description, true, true)).append("</description>").append("<ev:startdate>")
            .append(date).append("</ev:startdate>");

            s.append("</item>");
        }

        s.append("</rdf:RDF>");

        return s.toString();
    }

    public static boolean isNumeric(String inString) {
        CharacterIterator theIterator = new StringCharacterIterator(inString);

        for (char ch = theIterator.first(); ch != CharacterIterator.DONE; ch = theIterator.next()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }

    public static boolean isUpcomingEvent(String nd, String freq, Date date) {
        int year = Integer.parseInt(nd.substring(0, 4)) - 1900;
        int month = Integer.parseInt(nd.substring(5, 7)) - 1;
        int day = Integer.parseInt(nd.substring(8, 10));
        Date nextDeadline = new Date(year, month, day);

        long nextDeadlineMillis = nextDeadline.getTime();

        int f = Integer.parseInt(freq);
        int period = new Double(3.0 * f).intValue();
        long periodMillis =
            (new Long(period).longValue() * new Long(24).longValue() * new Long(3600).longValue() * new Long(1000).longValue());
        Date periodStartDate = new Date(nextDeadlineMillis - periodMillis);

        if (nextDeadline.after(date) && periodStartDate.before(date)) {
            return true;
        }
        return false;
    }

}
