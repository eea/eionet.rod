package eionet.rod.web.action;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import com.tee.uit.client.ServiceClientIF;
import com.tee.uit.client.ServiceClients;
import com.tee.xmlserver.GeneralException;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.dto.ClientDTO;
import eionet.rod.dto.CountryDTO;
import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.DDParamDTO;
import eionet.rod.dto.IssueDTO;
import eionet.rod.dto.LookupDTO;
import eionet.rod.dto.ObligationFactsheetDTO;
import eionet.rod.dto.SiblingObligationDTO;
import eionet.rod.dto.VersionDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/obligation")
public class ObligationFactsheetActionBean extends AbstractRODActionBean {
	
	private ObligationFactsheetDTO obligation;
	private String id;
	private List<ClientDTO> clients;
	private List<LookupDTO> infoTypeList;
	private String tab;
	private List<CountryDeliveryDTO> deliveries;
	private String spatialId;
	private List<VersionDTO> versions;
	private List<SiblingObligationDTO> siblingObligations;
	private List<CountryDTO> countries;
	private List<IssueDTO> issues;
	private List<DDParamDTO> ddparameters;
	
	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution overview() throws ServiceException {
		
		String pathInfo = getContext().getRequest().getPathInfo();
		if(!RODUtil.isNullOrEmpty(pathInfo)){
			StringTokenizer st = new StringTokenizer(pathInfo,"/");
			id = st.nextToken();
			if(st.hasMoreElements())
				tab = st.nextToken();		
		}
		
		if(!RODUtil.isNullOrEmpty(id) && RODUtil.isNumber(id)){
			obligation = RODServices.getDbService().getObligationDao().getObligationFactsheet(id);
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
		} else {
			handleRodException("Obligation ID is not defined or is not a number!", Constants.SEVERITY_WARNING);
		}
		
		return new ForwardResolution("/pages/obligation.jsp");
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

	public List<DDParamDTO> getDdparameters() {
		return ddparameters;
	}

	public void setDdparameters(List<DDParamDTO> ddparameters) {
		this.ddparameters = ddparameters;
	}


}
