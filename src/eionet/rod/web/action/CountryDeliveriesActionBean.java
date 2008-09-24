package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.dto.CountryDeliveryDTO;
import eionet.rod.dto.CountryDeliveryDataDTO;
import eionet.rod.dto.ClientDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/countrydeliveries")
public class CountryDeliveriesActionBean extends AbstractRODActionBean {
	
	private List<CountryDeliveryDTO> deliveries;
	private CountryDeliveryDataDTO deliveryData;
	private List<ClientDTO> clients;
	private String actDetailsId;
	private String spatialId;
	private String spatialName;
	private String reportFreq;
	private boolean allCountries;
	
	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		if(!RODUtil.isNullOrEmpty(actDetailsId) && RODUtil.isNumber(actDetailsId)){
			if(!RODUtil.isNullOrEmpty(spatialId) && !RODUtil.isNumber(spatialId))
				handleRodException("spatialId has to be a number!", Constants.SEVERITY_WARNING);
			else{
				deliveries = RODServices.getDbService().getDeliveryDao().getCountyDeliveriesList(actDetailsId, spatialId);
				deliveryData = RODServices.getDbService().getDeliveryDao().getDeliveryData(actDetailsId);
				reportFreq = reportFreqMsg(deliveryData);
				clients = RODServices.getDbService().getClientDao().getDeliveryClients(actDetailsId);
				
				if(!RODUtil.isNullOrEmpty(spatialId)){
					spatialName = RODServices.getDbService().getSpatialDao().getCountryById(new Integer(spatialId).intValue());
					allCountries = false;
				} else {
					spatialName = "all countries";
					allCountries = true;
				}
			}
		} else {
			handleRodException("actDetailsId is not defined or is not a number", Constants.SEVERITY_WARNING);
		}
				
		return new ForwardResolution("/pages/countrydeliveries.jsp");
	}
	
	private String reportFreqMsg(CountryDeliveryDataDTO deliveryData) {
		
		String ret = "";
		
		if(deliveryData != null){
			String freq = deliveryData.getObligationReportFreqMonths();
			if(deliveryData.getObligationTerminate().equals("N")){
				if(freq.equals("0"))
					ret = "One time only";
				else if(freq.equals("1"))
					ret = "Monthly";
				else if(freq.equals("12"))
					ret = "Annually";
				else if(deliveryData.getObligationNextDeadline().length() == 0)
					ret = "&#160;";
				else
					ret = "Every " + freq + "months";
			} else {
				ret = "<span class=\"warning\">terminated</span>";
			}
		}
		
		return ret;
	}

	public List<CountryDeliveryDTO> getDeliveries() {
		return deliveries;
	}

	public void setDeliveries(List<CountryDeliveryDTO> deliveries) {
		this.deliveries = deliveries;
	}

	public String getActDetailsId() {
		return actDetailsId;
	}

	public void setActDetailsId(String actDetailsId) {
		this.actDetailsId = actDetailsId;
	}

	public String getSpatialId() {
		return spatialId;
	}

	public void setSpatialId(String spatialId) {
		this.spatialId = spatialId;
	}

	public String getSpatialName() {
		return spatialName;
	}

	public void setSpatialName(String spatialName) {
		this.spatialName = spatialName;
	}

	public CountryDeliveryDataDTO getDeliveryData() {
		return deliveryData;
	}

	public void setDeliveryData(CountryDeliveryDataDTO deliveryData) {
		this.deliveryData = deliveryData;
	}

	public List<ClientDTO> getClients() {
		return clients;
	}

	public void setClients(List<ClientDTO> clients) {
		this.clients = clients;
	}

	public String getReportFreq() {
		return reportFreq;
	}

	public void setReportFreq(String reportFreq) {
		this.reportFreq = reportFreq;
	}

	public boolean isAllCountries() {
		return allCountries;
	}

	public void setAllCountries(boolean allCountries) {
		this.allCountries = allCountries;
	}

}
