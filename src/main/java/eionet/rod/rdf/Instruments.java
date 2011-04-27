package eionet.rod.rdf;

import javax.servlet.http.HttpServletRequest;

import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.MissingResourceException;

import eionet.rod.RODUtil;
import eionet.rod.dto.InstrumentObligationDTO;
import eionet.rod.dto.InstrumentRdfDTO;
import eionet.rod.dto.SourceLinksDTO;
import eionet.rod.services.ServiceException;
import eionet.rod.services.RODServices;

/**
 * <P>Servlet URL: <CODE>rdf</CODE></P>
 *
 * <P>Database tables involved: T_ACTIVITY</P>
 *
 * <P>XSL file used: <CODE>index.xsl</CODE><BR>
 * Query file used: <CODE>index.xrs</CODE></P>
 *
 * @author  Kaido Laine
 * @version 1.0
 */
public class Instruments extends RDFServletAC {

    private static final long serialVersionUID = -403250971215465050L;

    private static String allNameSpaces =  rdfNameSpace +  rdfSNameSpace + "xmlns:dcterms=\"http://purl.org/dc/terms/\"";

    public String getRdf(HttpServletRequest req) throws ServiceException {
        return getRdf(req, null);
    }

    public String getRdf(HttpServletRequest req, InstrumentRdfDTO instrument) throws ServiceException {
        try {
            props = ResourceBundle.getBundle(PROP_FILE);
        } catch (MissingResourceException mre) {
            mre.printStackTrace();
        }

        if (activitiesNamespace == null)
            activitiesNamespace = props.getString(ROD_URL_NS);

        if (instrumentsNamespace == null)
            try {
                instrumentsNamespace = props.getString(ROD_LI_NS);
            } catch (MissingResourceException mre ) {
                instrumentsNamespace="http://rod.eionet.europa.eu/instruments/";
            }

        if (issuesNamespace == null)
            try {
                issuesNamespace = props.getString(ROD_ISSUES_NS);
            } catch (MissingResourceException mre ) {
                issuesNamespace="http://rod.eionet.europa.eu/issues/";
            }

        if (spatialNamespace == null)
            try {
                spatialNamespace = props.getString("spatial.namespace");
            } catch (MissingResourceException mre ) {
                issuesNamespace="http://rod.eionet.europa.eu/spatial/";
            }


        if (obligationsNamespace == null)
            obligationsNamespace = props.getString(ROD_URL_RO_NS);

        if (rodSchemaNamespace == null)
            try {
                rodSchemaNamespace=props.getString("schema.namespace");
                //quite likely it will not change
            } catch (MissingResourceException mre ) {
                rodSchemaNamespace="http://rod.eionet.europa.eu/schema.rdf";
            }

        if (instrument != null)
            return generateRDFSingleInstrument(instrument);
        else
            return generateRDF(req);
    }

    protected String generateRDFSingleInstrument(InstrumentRdfDTO instrument) throws ServiceException {
        StringBuffer s = new StringBuffer();
        s.append(rdfHeader);
        s.append("<rdf:RDF ")
        .append(" xmlns:rod=\"").append(rodSchemaNamespace).append("#\"")
        .append(" ").append(allNameSpaces).append(">");

        StringBuffer content = genereteElement(instrument);
        s.append(content);

        s.append("</rdf:RDF>");

        return s.toString();
    }

    protected String generateRDF(HttpServletRequest req) throws ServiceException {

        StringBuffer s = new StringBuffer();
        s.append(rdfHeader);
        s.append("<rdf:RDF ")
        .append(" xmlns:rod=\"").append(rodSchemaNamespace).append("#\"")
        .append(" ")
        .append(allNameSpaces)
        .append(">");

        List<InstrumentRdfDTO> instruments = RODServices.getDbService().getSourceDao().getInstrumentsForRDF();

        for (InstrumentRdfDTO instrument : instruments) {
            StringBuffer element = genereteElement(instrument);
            s.append(element);
        }

        List<SourceLinksDTO> links = RODServices.getDbService().getSourceDao().getSourceLinks();
        for (Iterator<SourceLinksDTO> it = links.iterator(); it.hasNext();) {
            SourceLinksDTO sourceLink = it.next();
            if (sourceLink != null) {
                s.append("<rdf:Description rdf:about=\"http://rod.eionet.europa.eu/instruments/").append(sourceLink.getChildId()).append("\">")
                .append("<rod:parentInstrument rdf:resource=\"http://rod.eionet.europa.eu/instruments/")
                .append(sourceLink.getParentId()).append("\"/>")
                .append("</rdf:Description>");
            }
        }

        s.append("</rdf:RDF>");

        return s.toString();

    }

    private StringBuffer genereteElement(InstrumentRdfDTO instrument) throws ServiceException {

        Integer pk = instrument.getSourceId();
        String source_code = instrument.getSourceCode();
        Integer client_id = instrument.getClientId();
        String alias = instrument.getSourceAlias();
        String legalName = instrument.getSourceTitle();
        String url = instrument.getSourceUrl();
        String abstr = instrument.getSourceAbstract();
        String issuedBy = instrument.getClientName();
        String celexRef = instrument.getSourceCelexRef();
        String validFrom = instrument.getSourceValidFrom();
        String comment = instrument.getSourceComment();

        //show legal name if short name is empty
        String title = (RODUtil.nullString(alias) ? legalName : alias);

        StringBuffer s = new StringBuffer();
        s.append("<rod:Instrument rdf:about=\"" + instrumentsNamespace + pk + "\">");

        if (!RODUtil.isNullOrEmpty(alias))
            s.append("<dcterms:alternative>").append(RODUtil.replaceTags(title,true,true)).append("</dcterms:alternative>");

        s.append("<rdfs:label>").append(RODUtil.replaceTags(title,true,true)).append("</rdfs:label>")
        .append("<dcterms:title>").append(RODUtil.replaceTags(legalName,true,true)).append("</dcterms:title>")
        .append("<rod:celexref>").append(RODUtil.replaceTags(celexRef,true,true)).append("</rod:celexref>");

        if (!RODUtil.isNullOrEmpty(source_code))
            s.append("<dcterms:identifier>").append(source_code).append("</dcterms:identifier>");

        if (!RODUtil.nullString(abstr))
            s.append("<dcterms:abstract>").append(RODUtil.replaceTags(abstr, true, true)).append("</dcterms:abstract>");

        if (!RODUtil.nullString(url))
            s.append("<rod:instrumentURL rdf:resource=\""+RODUtil.replaceTags(url, true, true)+"\"/>");

        if (!RODUtil.nullString(issuedBy))
            s.append("<dcterms:creator rdf:resource=\"").append("/clients/"+client_id).append("\"/>");

        if (!RODUtil.isNullOrEmpty(validFrom)) {
            s.append("<dcterms:valid>").append(validFrom).append("</dcterms:valid>");
        }

        if (!RODUtil.isNullOrEmpty(comment))
            s.append("<rdfs:comment>").append(RODUtil.replaceTags(comment, true, true)).append("</rdfs:comment>");

        List<InstrumentObligationDTO> obligations = instrument.getObligations();
        for (InstrumentObligationDTO obligation : obligations) {
            s.append("<rod:hasObligation rdf:resource=\"/obligations/").append(obligation.getObligationId()).append("\"/>");
        }

        s.append("</rod:Instrument>");

        return s;
    }
}
