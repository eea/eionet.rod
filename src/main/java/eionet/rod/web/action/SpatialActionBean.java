package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.RODUtil;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.web.util.SeeOtherRedirectResolution;

/**
 *
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/spatial/{idspatial}")
public class SpatialActionBean extends AbstractRODActionBean {

    private CountryDTO spatial;
    private String idspatial;
    private List<ObligationDTO> obligations;


    private final String header = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<rdf:RDF  xmlns:rod=\"http://rod.eionet.europa.eu/schema.rdf#\" \n" +
            "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" \n" +
            "xmlns:rdfs=\"http://www.w3.org/2000/01/rdf-schema#\">\n";

    private static final String footer = "\n</rdf:RDF>";

    /**
     *
     * @return
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        String acceptHeader = getContext().getRequest().getHeader("accept");
        String[] accept = null;
        if (acceptHeader != null && acceptHeader.length() > 0)
            accept = acceptHeader.split(",");

        if (!RODUtil.isNullOrEmpty(idspatial)) {
            spatial = RODServices.getDbService().getSpatialDao().getCountry(idspatial);
            obligations = RODServices.getDbService().getSpatialDao().getCountryObligationsList(idspatial);

            if (accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")) {
                return new SeeOtherRedirectResolution("/spatial");
            }
        } else {
            List<CountryDTO> spatials = RODServices.getDbService().getSpatialDao().getSpatialsList();

            StringBuffer out = new StringBuffer();
            out.append(header);
            for(CountryDTO spatial : spatials) {
                out.append("<rod:Locality rdf:about=\"http://rod.eionet.europa.eu/spatial/").append(spatial.getCountryId()).append("\">\n");
                out.append("<rod:localityName>").append(RODUtil.replaceTags(spatial.getName(),true,true)).append("</rod:localityName>\n");
                out.append("<rdfs:label>").append(RODUtil.replaceTags(spatial.getName(),true,true)).append("</rdfs:label>\n");
                if (spatial.getType() != null && !spatial.getType().equals("") && !spatial.getType().equals("null"))
                    out.append("<rod:localityType>").append(spatial.getType()).append("</rod:localityType>\n");
                if (spatial.getTwoletter() != null && !spatial.getTwoletter().equals("") && !spatial.getTwoletter().equals("null"))
                    out.append("<rod:loccode>").append(spatial.getTwoletter()).append("</rod:loccode>\n");
                if (spatial.getIsMember() != null && !spatial.getIsMember().equals("") && !spatial.getIsMember().equals("null"))
                    out.append("<rod:isEEAMember>").append(spatial.getIsMember()).append("</rod:isEEAMember>\n");
                List<ObligationDTO> obligations = RODServices.getDbService().getSpatialDao().getCountryObligationsList(spatial.getCountryId().toString());
                for(ObligationDTO obligation : obligations) {
                    out.append("<rod:providerFor rdf:resource=\"http://rod.eionet.europa.eu/obligations/").append(obligation.getObligationId()).append("\"/>\n");
                }
                out.append("</rod:Locality>");
            }
            out.append(footer);
            return new StreamingResolution("application/rdf+xml;charset=UTF-8",out.toString());
    }

        return new ForwardResolution("/pages/spatial.jsp");
    }


    public List<ObligationDTO> getObligations() {
        return obligations;
    }


    public void setObligations(List<ObligationDTO> obligations) {
        this.obligations = obligations;
    }


    public CountryDTO getSpatial() {
        return spatial;
    }


    public void setSpatial(CountryDTO spatial) {
        this.spatial = spatial;
    }


    public String getIdspatial() {
        return idspatial;
    }


    public void setIdspatial(String idspatial) {
        this.idspatial = idspatial;
    }



}
