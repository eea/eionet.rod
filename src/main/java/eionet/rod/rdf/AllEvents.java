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
 * Copyright (C) 2000-2014 by European Environment Agency.  All
 * Rights Reserved.
 *
 * Original Code: Kaido Laine (TietoEnator)
 * Contributor: Søren Roug
 */

package eionet.rod.rdf;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import java.io.IOException;

public class AllEvents extends RSSServletAC {

    private static final long serialVersionUID = 1L;

    @Override
    protected void generateRDF(final HttpServletRequest req, final HttpServletResponse res) throws ServiceException, IOException {

        StringTokenizer issues = tokenizeParam(req.getParameter("issues"));
        StringTokenizer countries = tokenizeParam(req.getParameter("countries"));

        RDFUtil rdfOut = new RDFUtil(res.getWriter());
        rdfOut.addNamespace("ev", EVENTS_NS);
        rdfOut.setVocabulary(RSS_NS);
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
    }

}
