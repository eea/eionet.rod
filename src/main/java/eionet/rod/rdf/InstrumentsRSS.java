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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * Activities RSS.
 */
public class InstrumentsRSS extends RSSServletAC {

    private static final long serialVersionUID = 1L;

    @Override
    protected void generateRDF(HttpServletRequest req, HttpServletResponse res) throws ServiceException, IOException {

        RDFUtil rdfOut = new RDFUtil(res.getWriter());
        rdfOut.addNamespace("ev", eventsNs);
        rdfOut.setVocabulary(rssNs);
        rdfOut.writeRdfHeader();

        String lisUrl = "http://rod.eionet.europa.eu/instruments.rss";
        try {
            lisUrl = props.getString(Constants.ROD_URL_INSTRUMENTS);
        } catch (Exception e) {
            // use default
        }
        rdfOut.writeStartResource("channel", lisUrl);

        String[][] lis = RODServices.getDbService().getSourceDao().getInstrumentsRSS();

        rdfOut.writeStartLiteral("items");
        rdfOut.writeStartResource("rdf:Seq");
        for (int i = 0; i < lis.length; i++) {
            String pk = lis[i][0];

            rdfOut.writeReference("rdf:li", instrumentsNamespace + pk);
        }
        rdfOut.writeEndResource("rdf:Seq");
        rdfOut.writeEndLiteral("items");

        rdfOut.writeEndResource("channel");
        for (int i = 0; i < lis.length; i++) {
            String pk = lis[i][0];
            String title = lis[i][1];
            String link = lis[i][2];
            String description = lis[i][3];

            rdfOut.writeStartResource("item", instrumentsNamespace + pk);
            rdfOut.writeLiteral("title", title);
            rdfOut.writeLiteral("link", link);
            rdfOut.writeLiteral("description", description);

            rdfOut.writeEndResource("item");
        }

        rdfOut.writeRdfFooter();
    }

}
