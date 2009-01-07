package eionet.rod.web.action;

import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.ROUser;
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
	@ValidateNestedProperties({
		@Validate(field = "url", on ={"edit","add"}, mask = "^((ht|f)tps?://).*")})
	private ClientDTO client;
	private String clientId;
	
	/** 
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		String forwardPage = "/pages/client.jsp";
		String pathInfo = getContext().getRequest().getPathInfo();
		if(!RODUtil.isNullOrEmpty(pathInfo)){
			StringTokenizer st = new StringTokenizer(pathInfo,"/");
			if(st.hasMoreElements())
				clientId = st.nextToken();
		}
		
		if(!RODUtil.isNullOrEmpty(clientId)){
			client = RODServices.getDbService().getClientDao().getClientFactsheet(clientId);
			if(client == null || !RODUtil.isNumber(clientId)){
				return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
			}
		} else {		
			clients = RODServices.getDbService().getClientDao().getAllClients();
			forwardPage = "/pages/clients.jsp";
		}
			
		return new ForwardResolution(forwardPage);
	}
	
    /**
     * 
     * @return
     * @throws DAOException
     */
    public Resolution edit() throws ServiceException {
    	
    	Resolution resolution = new ForwardResolution("/pages/editclient.jsp");
		if(ROUser.hasPermission(getUserName(),Constants.ACL_CLIENT_NAME,Constants.ACL_UPDATE_PERMISSION)){
			if (isPostRequest()){
				RODServices.getDbService().getClientDao().editClient(client);
				showMessage(getBundle().getString("update.success"));
				resolution = new ForwardResolution("/pages/client.jsp");
			}
			client = RODServices.getDbService().getClientDao().getClientFactsheet(new Integer(client.getClientId()).toString());
			clientId = new Integer(client.getClientId()).toString();
		}
		else
			handleRodException(getBundle().getString("not.permitted"), Constants.SEVERITY_WARNING);
		
        return resolution;
    }
    
    /**
     * 
     * @return
     * @throws DAOException
     */
    public Resolution add() throws ServiceException {
    	
    	Resolution resolution = new ForwardResolution("/pages/addclient.jsp");
		if(ROUser.hasPermission(getUserName(),Constants.ACL_CLIENT_NAME,Constants.ACL_INSERT_PERMISSION)){
			if (isPostRequest()){
				Integer cId = RODServices.getDbService().getClientDao().addClient(client);
				clientId = cId.toString();
				showMessage(getBundle().getString("insert.success"));
				resolution = new ForwardResolution("/pages/client.jsp");
			}
			client = RODServices.getDbService().getClientDao().getClientFactsheet(clientId);
		}
		else
			handleRodException(getBundle().getString("not.permitted"), Constants.SEVERITY_WARNING);
		
        return resolution;
    }
	
	public List<ClientDTO> getClients() {
		return clients;
	}

	public void setClients(List<ClientDTO> clients) {
		this.clients = clients;
	}

	public ClientDTO getClient() {
		return client;
	}

	public void setClient(ClientDTO client) {
		this.client = client;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
}
