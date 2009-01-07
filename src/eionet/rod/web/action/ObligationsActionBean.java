package eionet.rod.web.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import com.tee.uit.client.ServiceClientIF;
import com.tee.uit.client.ServiceClients;
import com.tee.xmlserver.GeneralException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.DDParamDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.LookupDTO;
import eionet.rod.dto.ObligationCountryDTO;
import eionet.rod.dto.ObligationFactsheetDTO;
import eionet.rod.dto.ObligationsListDTO;
import eionet.rod.dto.SiblingObligationDTO;
import eionet.rod.dto.VersionDTO;
import eionet.rod.rdf.Activities;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/obligations")
public class ObligationsActionBean extends AbstractRODActionBean {
	
	private ObligationFactsheetDTO obligation;
	private String id;
	private List<ClientDTO> clients;
	private List<LookupDTO> infoTypeList;
	private String tab;
	private List<CountryDeliveryDTO> deliveries;
	private String spatialId;
	private List<VersionDTO> versions;
	private List<SiblingObligationDTO> siblingObligations;
	private List<ObligationCountryDTO> countries;
	private List<IssueDTO> issues; 
	private List<DDParamDTO> ddparameters;
	
	private String anmode;
	private List<ObligationsListDTO> obligations;
	private List<ObligationsListDTO> indirectObligations;
	private List<CountryDTO> formCountries;
	private List<IssueDTO> formIssues;
	private List<ClientDTO> formClients;
	private String country;
	private String issue;
	private String client;
	private String terminated;
	private String countryName;
	private String issueName;
	private String clientName;
	
	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		String forwardPage = "/pages/obligation.jsp";
		String pathInfo = getContext().getRequest().getPathInfo();
		if(!RODUtil.isNullOrEmpty(pathInfo)){
			StringTokenizer st = new StringTokenizer(pathInfo,"/");
			if(st.hasMoreElements())
				id = st.nextToken();
			if(st.hasMoreElements())
				tab = st.nextToken();		
		}
		
		if(!RODUtil.isNullOrEmpty(id)){
			obligation = RODServices.getDbService().getObligationDao().getObligationFactsheet(id); 
			if(obligation == null || !RODUtil.isNumber(id)){
				return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
			}
			if(RODUtil.isNullOrEmpty(tab) || tab.equals("overview")){ 
				clients = RODServices.getDbService().getClientDao().getClients(id);
				infoTypeList = RODServices.getDbService().getObligationDao().getLookupList(id);
			} else if(tab.equals("legislation")){
				siblingObligations = RODServices.getDbService().getObligationDao().getSiblingObligations(id);
				countries = RODServices.getDbService().getSpatialDao().getObligationCountriesList(id);
				issues = RODServices.getDbService().getIssueDao().getObligationIssuesList(id);
			} else if(tab.equals("deliveries")){
				deliveries = RODServices.getDbService().getDeliveryDao().getCountyDeliveriesList(id, spatialId);
			} else if(tab.equals("history")){
				versions = RODServices.getDbService().getUndoDao().getPreviousActionsReportSpecific(id, "T_OBLIGATION", "PK_RA_ID");
			} else if(tab.equals("parameters")){
				ddparameters = getDDParams(); 
			}
		} else if(RODUtil.isNullOrEmpty(id)){
			
			String acceptHeader = getContext().getRequest().getHeader("accept");
			String[] accept = acceptHeader.split(",");
			if(accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")){

				return new StreamingResolution("application/rdf+xml;charset=UTF-8") {
				    public void stream(HttpServletResponse response) throws Exception {
				    	Activities act = new Activities();
				    	String rdf = act.getRdf(getContext().getRequest());
				    	response.getWriter().write(rdf);
				    }
				}.setFilename("obligations.rdf");

			}
			
			formCountries = RODServices.getDbService().getSpatialDao().getCountriesList();
			formIssues = RODServices.getDbService().getIssueDao().getIssuesList();
			formClients = RODServices.getDbService().getClientDao().getClientsList();
			
			obligations = RODServices.getDbService().getObligationDao().getObligationsList(anmode, null, null, null, null, false);
			forwardPage = "/pages/obligations.jsp";
		} else {
			handleRodException("Obligation ID has to be a number!", Constants.SEVERITY_WARNING);
		}
		
		return new ForwardResolution(forwardPage);
	}
	
	/**
	 * 
	 * @return
	 */
	public Resolution filter() throws ServiceException {
		
		formCountries = RODServices.getDbService().getSpatialDao().getCountriesList();
		formIssues = RODServices.getDbService().getIssueDao().getIssuesList();
		formClients = RODServices.getDbService().getClientDao().getClientsList();
		
		if(!RODUtil.isNullOrEmpty(country) && !country.equals("-1"))
			countryName = RODServices.getDbService().getSpatialDao().getCountryById(new Integer(country).intValue());
		if(!RODUtil.isNullOrEmpty(client) && !client.equals("-1"))
			clientName = RODServices.getDbService().getClientDao().getOrganisationNameByID(client);
		if(!RODUtil.isNullOrEmpty(issue) && !issue.equals("-1"))
			issueName = RODServices.getDbService().getIssueDao().getIssueNameById(issue);		
		
		obligations = RODServices.getDbService().getObligationDao().getObligationsList(anmode, country, issue, client, terminated, false);
		if(!RODUtil.isNullOrEmpty(client) && !client.equals("-1"))
			indirectObligations = RODServices.getDbService().getObligationDao().getObligationsList(anmode, country, issue, client, terminated, true);
		
		return new ForwardResolution("/pages/obligations.jsp");
	}
	
	private List<DDParamDTO> getDDParams(){
		
		List<DDParamDTO> retList = new ArrayList<DDParamDTO>(); 
		
		try {
			String serviceName  = "DataDictService";
			String rpcRouterUrl = RODServices.getFileService().getStringProperty("dd.service.url");
			String methodName   = "getParametersByActivityID";
			
			ServiceClientIF client = ServiceClients.getServiceClient(serviceName, rpcRouterUrl);
			
			Vector params = new Vector();
			params.add(id);
			
			Vector result = (Vector)client.getValue(methodName, params);
			for(int i = 0; result != null && i < result.size(); i++){
				Hashtable hash = (Hashtable)result.get(i);
				
				DDParamDTO elem = new DDParamDTO();
				elem.setElementName((String)hash.get("elm-name")); // element name
				elem.setElementUrl((String)hash.get("elm-url"));   // details url
				elem.setTableName((String)hash.get("tbl-name"));   // table name
				elem.setDatasetName((String)hash.get("dst-name")); // dataset name
				
				retList.add(elem);
			}
		} catch (Exception e){
			throw new GeneralException(null, e.toString());
		}
		return retList;
	}
	
	/**
     * 
     * @return
     * @throws DAOException
     */
    public Resolution edit() throws ServiceException {
    	Resolution resolution = new ForwardResolution("/pages/editobligation.jsp");
    	obligation = RODServices.getDbService().getObligationDao().getObligationFactsheet(id);
    	return resolution;
    }


	public ObligationFactsheetDTO getObligation() {
		return obligation;
	}

	public void setObligation(ObligationFactsheetDTO obligation) {
		this.obligation = obligation;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<ClientDTO> getClients() {
		return clients;
	}

	public void setClients(List<ClientDTO> clients) {
		this.clients = clients;
	}

	public List<LookupDTO> getInfoTypeList() {
		return infoTypeList;
	}

	public void setInfoTypeList(List<LookupDTO> infoTypeList) {
		this.infoTypeList = infoTypeList;
	}

	public String getTab() {
		return tab;
	}

	public void setTab(String tab) {
		this.tab = tab;
	}


	public List<CountryDeliveryDTO> getDeliveries() {
		return deliveries;
	}


	public void setDeliveries(List<CountryDeliveryDTO> deliveries) {
		this.deliveries = deliveries;
	}


	public String getSpatialId() {
		return spatialId;
	}


	public void setSpatialId(String spatialId) {
		this.spatialId = spatialId;
	}


	public List<VersionDTO> getVersions() {
		return versions;
	}


	public void setVersions(List<VersionDTO> versions) {
		this.versions = versions;
	}


	public List<SiblingObligationDTO> getSiblingObligations() {
		return siblingObligations;
	}


	public void setSiblingObligations(List<SiblingObligationDTO> siblingObligations) {
		this.siblingObligations = siblingObligations;
	}


	public List<ObligationCountryDTO> getCountries() {
		return countries;
	}


	public void setCountries(List<ObligationCountryDTO> countries) {
		this.countries = countries;
	}


	public List<IssueDTO> getIssues() {
		return issues;
	}


	public void setIssues(List<IssueDTO> issues) {
		this.issues = issues;
	}

	public List<DDParamDTO> getDdparameters() {
		return ddparameters;
	}

	public void setDdparameters(List<DDParamDTO> ddparameters) {
		this.ddparameters = ddparameters;
	}

	public List<ObligationsListDTO> getObligations() {
		return obligations;
	}

	public void setObligations(List<ObligationsListDTO> obligations) {
		this.obligations = obligations;
	}

	public String getAnmode() {
		return anmode;
	}

	public void setAnmode(String anmode) {
		this.anmode = anmode;
	}

	public List<CountryDTO> getFormCountries() {
		return formCountries;
	}

	public void setFormCountries(List<CountryDTO> formCountries) {
		this.formCountries = formCountries;
	}

	public List<IssueDTO> getFormIssues() {
		return formIssues;
	}

	public void setFormIssues(List<IssueDTO> formIssues) {
		this.formIssues = formIssues;
	}

	public List<ClientDTO> getFormClients() {
		return formClients;
	}

	public void setFormClients(List<ClientDTO> formClients) {
		this.formClients = formClients;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getTerminated() {
		return terminated;
	}

	public void setTerminated(String terminated) {
		this.terminated = terminated;
	}

	public List<ObligationsListDTO> getIndirectObligations() {
		return indirectObligations;
	}

	public void setIndirectObligations(List<ObligationsListDTO> indirectObligations) {
		this.indirectObligations = indirectObligations;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getIssueName() {
		return issueName;
	}

	public void setIssueName(String issueName) {
		this.issueName = issueName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
	}


}
