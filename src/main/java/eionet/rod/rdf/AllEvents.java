/*
 * Created on 2.03.2006
 */
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

import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import java.io.IOException;

public class AllEvents extends RSSServletAC {

    private static final long serialVersionUID = 1L;

    protected String generateRDF(final HttpServletRequest req, final HttpServletResponse res) throws ServiceException {

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

        try {
            RDFUtil rdfOut = new RDFUtil(res.getWriter());
            rdfOut.addNamespace("ev", "http://purl.org/rss/1.0/modules/event/");
            rdfOut.setVocabulary("http://purl.org/rss/1.0/");
            rdfOut.writeRdfHeader();

            String eventsUrl = props.getString(Constants.ROD_URL_EVENTS);
            rdfOut.writeStartResource("channel", eventsUrl);

            String[][] events = RODServices.getDbService().getObligationDao().getAllActivityDeadlines(issues, countries);

            rdfOut.writeStartLiteral("items");
            rdfOut.writeStartResource("rdf:Seq");
            for (int i = 0; i < events.length; i++) {
                String pk = events[i][0];

                rdfOut.writeReference("rdf:li", obligationsNamespace + "/" + pk);
            }
            rdfOut.writeEndResource("rdf:Seq");
            rdfOut.writeEndLiteral("items");

            rdfOut.writeEndResource("channel");
            for (int i = 0; i < events.length; i++) {
                String pk = events[i][0];
                String title = "Deadline for Reporting Obligation: " + events[i][1];
                String date = events[i][2];
                String link = getActivityUrl(pk, events[i][3]);
                String description = events[i][4];

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
    public static boolean isNumeric(final String inString) {
        CharacterIterator theIterator = new StringCharacterIterator(inString);

        for (char ch = theIterator.first(); ch != CharacterIterator.DONE; ch = theIterator.next()) {
            if (!Character.isDigit(ch)) {
                return false;
            }
        }

        return true;
    }

}
