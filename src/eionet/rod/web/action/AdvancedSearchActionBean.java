package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 *
 */
@UrlBinding("/cssearch")
public class AdvancedSearchActionBean extends AbstractRODActionBean {

	private List<CountryDTO> countries;
	private List<IssueDTO> issues;
	private List<ClientDTO> clients;
	
	/** 
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		countries = RODServices.getDbService().getSpatialDao().getCountriesList();
		issues = RODServices.getDbService().getIssueDao().getIssuesList();
		clients = RODServices.getDbService().getClientDao().getClientsList();
			
		return new ForwardResolution("/pages/cssearch.jsp");
	}
	
	public List<CountryDTO> getCountries() {
		return countries;
	}

	public void setCountries(List<CountryDTO> countries) {
		this.countries = countries;
	}

	public List<IssueDTO> getIssues() {
		return issues;
	}

	public void setIssues(List<IssueDTO> issues) {
		this.issues = issues;
	}

	public List<ClientDTO> getClients() {
		return clients;
	}

	public void setClients(List<ClientDTO> clients) {
		this.clients = clients;
	}
	
}
