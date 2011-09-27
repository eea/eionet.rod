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
 * The Original Code is "EINRC-5 / WebROD Project".
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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.ObligationRdfDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;


/**
 * Generates RDF for obligation(s)
 * @author altnyris
 * @version 1.2
 */
public class Activities extends RDFServletAC {

    private static final long serialVersionUID = 1L;

    public String getRdf(HttpServletRequest req) throws Exception {
        return getRdf(req, null);
    }

    public String getRdf(HttpServletRequest req, ObligationRdfDTO obligation) throws Exception {
        if (obligation != null) {
            return generateRDFSingleObligation(obligation);
        } else {
            return generateRDF(req);
        }
    }

    protected String generateRDFSingleObligation(ObligationRdfDTO obligation) throws ServiceException {
        StringBuffer s = new StringBuffer();
        s.append(RDF_HEADER);

        StringBuffer content = genereteElement(obligation);
        s.append(content);

        s.append(RDF_FOOTER);

        return s.toString();
    }

    protected String generateRDF(HttpServletRequest req) throws ServiceException {

        StringBuffer s = new StringBuffer();
        s.append(RDF_HEADER);

        List<ObligationRdfDTO> obligations = RODServices.getDbService().getObligationDao().getObligationsForRDF();

        for (ObligationRdfDTO obligation : obligations) {
            StringBuffer element = genereteElement(obligation);
            s.append(element);
        }
        s.append(RDF_FOOTER);

        return s.toString();

    }

    private StringBuffer genereteElement(ObligationRdfDTO obligation) throws ServiceException {

        int oblId = obligation.getObligationId();

        StringBuffer s = new StringBuffer();
        s.append("<Obligation rdf:about=\"obligations/").append(oblId).append("\">");
        s.append(RDFUtil.writeReference("instrument", "instruments/" + obligation.getSourceId()));
        s.append(RDFUtil.writeLiteral("dcterms:valid", obligation.getValidSince(), null, "date"));
        s.append(RDFUtil.writeLiteral("dcterms:title", obligation.getTitle()));
        s.append(RDFUtil.writeLiteral("guidelines", obligation.getReportingFormat()));
        s.append(RDFUtil.writeLiteral("comment", obligation.getComment()));
        s.append(RDFUtil.writeLiteral("responsibleRole", obligation.getResponsibleRole()));
        s.append(RDFUtil.writeLiteral("nextdeadline", obligation.getNextDeadline(), null, "date"));
        s.append(RDFUtil.writeLiteral("nextdeadline2", obligation.getNextDeadline2(), null, "date"));
        s.append(RDFUtil.writeLiteral("lastUpdate", obligation.getLastUpdate(), null, "date"));
        s.append(RDFUtil.writeLiteral("isTerminated", obligation.getTerminated()));
        s.append(RDFUtil.writeLiteral("validSince", obligation.getValidSince(), null, "date"));
        s.append(RDFUtil.writeLiteral("nextUpdate", obligation.getNextUpdate(), null, "date"));
        s.append(RDFUtil.writeLiteral("verified", obligation.getVerified(), null, "date"));
        s.append(RDFUtil.writeLiteral("verifiedBy", obligation.getVerifiedBy()));
        s.append(RDFUtil.writeLiteral("lastHarvested", obligation.getLastHarvested(), null, "dateTime"));
        s.append(RDFUtil.writeReference("requester", "clients/" + obligation.getRequester()));
        s.append(RDFUtil.writeLiteral("dcterms:abstract", obligation.getDescription()));
        s.append(RDFUtil.writeLiteral("coordinatorUrl", obligation.getCoordinatorUrl()));
        s.append(RDFUtil.writeLiteral("validatedBy", obligation.getValidatedBy()));
        s.append(RDFUtil.writeLiteral("isEEAPrimary", obligation.getEeaPrimary()));
        s.append(RDFUtil.writeLiteral("isEEACore", obligation.getEeaCore()));
        s.append(RDFUtil.writeLiteral("isFlagged", obligation.getFlagged()));
        s.append(RDFUtil.writeReference("dpsirCategory", obligation.getDpsirD()));
        s.append(RDFUtil.writeReference("dpsirCategory", obligation.getDpsirP()));
        s.append(RDFUtil.writeReference("dpsirCategory", obligation.getDpsirS()));
        s.append(RDFUtil.writeReference("dpsirCategory", obligation.getDpsirI()));
        s.append(RDFUtil.writeReference("dpsirCategory", obligation.getDpsirR()));
        s.append(RDFUtil.writeReference("dataUsedFor", obligation.getDataUsedForUrl()));
        s.append("</Obligation>");

        s.append("<rdf:Description rdf:about=\"obligations/").append(oblId).append("\">");
        List<ClientDTO> clients = RODServices.getDbService().getClientDao().getClients(new Integer(oblId).toString());
        if (clients != null && clients.size() > 0) {
            for (ClientDTO client : clients) {
                s.append(RDFUtil.writeReference("otherClient", "clients/" + client.getClientId()));
            }
        }

        List<Integer> mandatoryCountries = RODServices.getDbService().getSpatialDao().getObligationCountries(oblId, false);
        if (mandatoryCountries != null && mandatoryCountries.size() > 0) {
            for (Integer countryId : mandatoryCountries) {
                s.append(RDFUtil.writeReference("formalReporter", "spatial/" + countryId));
            }
        }

        List<Integer> voluntaryCountries = RODServices.getDbService().getSpatialDao().getObligationCountries(oblId, true);
        if (voluntaryCountries != null && voluntaryCountries.size() > 0) {
            for (Integer countryId : voluntaryCountries) {
                s.append(RDFUtil.writeReference("voluntaryReporter", "spatial/" + countryId));
            }
        }

        List<IssueDTO> issues = RODServices.getDbService().getIssueDao().getObligationIssuesList(new Integer(oblId).toString());
        if (issues != null && issues.size() > 0) {
            for (IssueDTO issue : issues) {
                s.append(RDFUtil.writeReference("issue", "issues/" + issue.getIssueId()));
            }
        }
        s.append("</rdf:Description>");

        return s;
    }
}
