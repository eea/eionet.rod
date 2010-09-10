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
import eionet.rod.dto.ObligationDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/issues/{idissue}")
public class IssuesActionBean extends AbstractRODActionBean {
	
	private String idissue;
	private String name;
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
		
		if(idissue != null && idissue.length() > 0){
			name = RODServices.getDbService().getIssueDao().getIssueNameById(idissue);
		}
		if(idissue == null || idissue.length() == 0 || name == null || name.length() == 0){
			return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
		}
		obligations = RODServices.getDbService().getIssueDao().getIssueObligationsList(idissue);
		
		String acceptHeader = getContext().getRequest().getHeader("accept");
		String[] accept = null;
		if(acceptHeader != null && acceptHeader.length() > 0)
			accept = acceptHeader.split(",");
		
		if(accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")){

			StringBuffer out = new StringBuffer();
			out.append(header);
			out.append("<rod:Issue rdf:about=\"http://rod.eionet.europa.eu/issues/").append(idissue).append("\">\n");
			out.append("<rod:issueName>").append(RODUtil.replaceTags(name,true,true)).append("</rod:issueName>\n");
			for(ObligationDTO obligation : obligations){
				out.append("<rod:issueOf rdf:about=\"http://rod.eionet.europa.eu/obligations/").append(obligation.getObligationId()).append("\">");
				out.append(RODUtil.replaceTags(obligation.getTitle(),true,true));
				out.append("</rod:issueOf>\n");
			}
			out.append("</rod:Issue>");
			out.append(footer);
			
			return new StreamingResolution("application/rdf+xml;charset=UTF-8",out.toString());
		}
				
		return new ForwardResolution("/pages/issue.jsp");
	}

	public String getIdissue() {
		return idissue;
	}


	public void setIdissue(String idissue) {
		this.idissue = idissue;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public List<ObligationDTO> getObligations() {
		return obligations;
	}


	public void setObligations(List<ObligationDTO> obligations) {
		this.obligations = obligations;
	}



}
