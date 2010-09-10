package eionet.rod.web.action;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.RODUtil;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

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
		
		if(idspatial != null && idspatial.length() > 0){
			spatial = RODServices.getDbService().getSpatialDao().getCountry(idspatial);
		}
		if(spatial == null){
			return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
		}
		obligations = RODServices.getDbService().getSpatialDao().getCountryObligationsList(idspatial);
		
		String acceptHeader = getContext().getRequest().getHeader("accept");
		String[] accept = null;
		if(acceptHeader != null && acceptHeader.length() > 0)
			accept = acceptHeader.split(",");
		
		if(accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")){

			StringBuffer out = new StringBuffer();
			out.append(header);
			out.append("<rod:Spatial rdf:about=\"http://rod.eionet.europa.eu/spatial/").append(idspatial).append("\">\n");
			out.append("<rod:spatialName>").append(RODUtil.replaceTags(spatial.getName(),true,true)).append("</rod:spatialName>\n");
			out.append("<rod:spatialType>").append(spatial.getType()).append("</rod:spatialType>\n");
			out.append("<rod:spatialTwoletter>").append(spatial.getTwoletter()).append("</rod:spatialTwoletter>\n");
			out.append("<rod:spatialIsMemberCountry>").append(spatial.getIsMember()).append("</rod:spatialIsMemberCountry>\n");
			for(ObligationDTO obligation : obligations){
				out.append("<rod:providerFor rdf:about=\"http://rod.eionet.europa.eu/obligations/").append(obligation.getObligationId()).append("\">");
				out.append(RODUtil.replaceTags(obligation.getTitle(),true,true));
				out.append("</rod:providerFor>\n");
			}
			out.append("</rod:Spatial>");
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
