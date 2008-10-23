package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import eionet.rod.dto.ClientDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 *
 */
@UrlBinding("/clients")
public class ClientsActionBean extends AbstractRODActionBean {

	private List<ClientDTO> clients;
	
	/** 
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		clients = RODServices.getDbService().getClientDao().getAllClients();
			
		return new ForwardResolution("/pages/clients.jsp");
	}
	
	public List<ClientDTO> getClients() {
		return clients;
	}

	public void setClients(List<ClientDTO> clients) {
		this.clients = clients;
	}
	
}
