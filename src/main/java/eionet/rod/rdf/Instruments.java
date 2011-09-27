package eionet.rod.rdf;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import eionet.rod.RODUtil;
import eionet.rod.dto.InstrumentObligationDTO;
import eionet.rod.dto.InstrumentRdfDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * Generates RDF for instrument(s)
 * @author  altnyris
 * @version 1.1
 */
public class Instruments extends RDFServletAC {

    private static final long serialVersionUID = -403250971215465050L;

    public String getRdf(HttpServletRequest req) throws ServiceException {
        return getRdf(req, null);
    }

    public String getRdf(HttpServletRequest req, InstrumentRdfDTO instrument) throws ServiceException {
        if (instrument != null) {
            return generateRDFSingleInstrument(instrument);
        } else {
            return generateRDF(req);
        }
    }

    protected String generateRDFSingleInstrument(InstrumentRdfDTO instrument) throws ServiceException {
        StringBuffer s = new StringBuffer();
        s.append(RDF_HEADER);

        StringBuffer content = genereteElement(instrument);
        s.append(content);

        s.append(RDF_FOOTER);

        return s.toString();
    }

    protected String generateRDF(HttpServletRequest req) throws ServiceException {

        StringBuffer s = new StringBuffer();
        s.append(RDF_HEADER);

        List<InstrumentRdfDTO> instruments = RODServices.getDbService().getSourceDao().getInstrumentsForRDF();
        for (InstrumentRdfDTO instrument : instruments) {
            StringBuffer element = genereteElement(instrument);
            s.append(element);
        }

        s.append(RDF_FOOTER);

        return s.toString();

    }

    private StringBuffer genereteElement(InstrumentRdfDTO instrument) throws ServiceException {

        Integer pk = instrument.getSourceId();

        StringBuffer s = new StringBuffer();
        s.append("<Instrument rdf:about=\"instruments/").append(pk).append("\">");
        s.append(RDFUtil.writeLiteral("dcterms:identifier", instrument.getSourceCode()));
        s.append(RDFUtil.writeLiteral("dcterms:title", instrument.getSourceTitle()));
        s.append(RDFUtil.writeLiteral("celexref", instrument.getSourceCelexRef()));
        s.append(RDFUtil.writeReference("instrumentURL", instrument.getSourceUrl()));
        s.append(RDFUtil.writeLiteral("dcterms:abstract", instrument.getSourceAbstract()));
        s.append(RDFUtil.writeLiteral("ecAccession", instrument.getSourceEcAccession()));
        s.append(RDFUtil.writeLiteral("ecEntryIntoForce", instrument.getSourceEcEntryIntoForce()));
        s.append(RDFUtil.writeLiteral("isDraft", instrument.isSourceIsDraft()));
        s.append(RDFUtil.writeLiteral("comment", instrument.getSourceComment()));
        s.append(RDFUtil.writeLiteral("lastModified", instrument.getSourceLastModified(), null, "date"));
        s.append(RDFUtil.writeLiteral("issuedBy", instrument.getSourceIssuedBy()));
        s.append(RDFUtil.writeLiteral("dcterms:valid", instrument.getSourceValidFrom(), null, "date"));
        s.append(RDFUtil.writeLiteral("dcterms:alternative",
                RODUtil.nullString(instrument.getSourceAlias()) ? instrument.getSourceTitle() : instrument.getSourceAlias()));
        s.append(RDFUtil.writeLiteral("rdfs:label", instrument.getSourceAlias()));
        s.append(RDFUtil.writeReference("dcterms:creator", "clients/" + instrument.getClientId()));
        s.append(RDFUtil.writeLiteral("lastUpdate", instrument.getSourceLastUpdate(), null, "dateTime"));
        s.append(RDFUtil.writeLiteral("nextUpdate", instrument.getSourceNextUpdate(), null, "date"));
        s.append(RDFUtil.writeLiteral("verified", instrument.getSourceVerified(), null, "date"));
        s.append(RDFUtil.writeLiteral("verifiedBy", instrument.getSourceVerifiedBy()));
        s.append(RDFUtil.writeLiteral("validatedBy", instrument.getSourceValidatedBy()));
        s.append(RDFUtil.writeLiteral("geographicScope", instrument.getSourceGeographicScope()));
        s.append("</Instrument>");

        s.append("<rdf:Description rdf:about=\"instruments/").append(pk).append("\">");
        List<InstrumentObligationDTO> obligations = instrument.getObligations();
        for (InstrumentObligationDTO obligation : obligations) {
            s.append(RDFUtil.writeReference("hasObligation", "obligations/" + obligation.getObligationId()));
        }
        String parentInstId = RODServices.getDbService().getSourceDao().getParentInstrumentId(new Integer(pk).toString());
        if (parentInstId != null && parentInstId.length() > 0) {
            s.append(RDFUtil.writeReference("parentInstrument", "instruments/" + parentInstId));
        }
        s.append("</rdf:Description>");

        return s;
    }
}
