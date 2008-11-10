package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import eionet.rod.dto.InstrumentsDueDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 *
 */
@UrlBinding("/instrumentsdue")
public class InstrumentsDueActionBean extends AbstractRODActionBean {

	private List<InstrumentsDueDTO> instruments;
	
	/** 
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		instruments = RODServices.getDbService().getSourceDao().getInstrumentsDue();
			
		return new ForwardResolution("/pages/instrumentsdue.jsp");
	}

	public List<InstrumentsDueDTO> getInstruments() {
		return instruments;
	}

	public void setInstruments(List<InstrumentsDueDTO> instruments) {
		this.instruments = instruments;
	}
	
	
}
