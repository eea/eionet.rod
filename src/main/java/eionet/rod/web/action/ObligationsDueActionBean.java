package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.ObligationsDueDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 *
 */
@UrlBinding("/obligationsdue")
public class ObligationsDueActionBean extends AbstractRODActionBean {

	private List<ObligationsDueDTO> obligations;
	
	/** 
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		obligations = RODServices.getDbService().getObligationDao().getObligationsDue();
			
		return new ForwardResolution("/pages/obligationsdue.jsp");
	}

	public List<ObligationsDueDTO> getObligations() {
		return obligations;
	}

	public void setObligations(List<ObligationsDueDTO> obligations) {
		this.obligations = obligations;
	}
	

	
}
