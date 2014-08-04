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
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * Activities RSS
 */
public class IssueActivities extends RSSServletAC {

    private static final long serialVersionUID = 1L;

    protected String generateRDF(HttpServletRequest req, HttpServletResponse res) throws ServiceException {

        StringTokenizer issues = tokenizeParam(req.getParameter("issues"));
        StringTokenizer countries = tokenizeParam(req.getParameter("countries"));

        try {
            RDFUtil rdfOut = new RDFUtil(res.getWriter());
            rdfOut.addNamespace("ev", "http://purl.org/rss/1.0/modules/event/");
            rdfOut.setVocabulary("http://purl.org/rss/1.0/");
            rdfOut.writeRdfHeader();

            String actsUrl = props.getString(Constants.ROD_URL_ACTIVITIES);
            rdfOut.writeStartResource("channel", actsUrl);

            String[][] acts = RODServices.getDbService().getObligationDao().getIssueActivities(issues, countries);

            rdfOut.writeStartLiteral("items");
            rdfOut.writeStartResource("rdf:Seq");
            for (int i = 0; i < acts.length; i++) {
                String pk = acts[i][0];

                rdfOut.writeReference("rdf:li", obligationsNamespace + "/" + pk);
            }
            rdfOut.writeEndResource("rdf:Seq");
            rdfOut.writeEndLiteral("items");

            rdfOut.writeEndResource("channel");

            for (int i = 0; i < acts.length; i++) {
                String pk = acts[i][0];
                String title = acts[i][1];
                String date = acts[i][2];
                String link = getActivityUrl(pk, acts[i][3]);
                String description = acts[i][4];

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

}
