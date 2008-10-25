package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.RODUtil;
import eionet.rod.dto.VersionDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/rodhistory")
public class RODHistoryActionBean extends AbstractRODActionBean {
	
	private List<VersionDTO> list;
	private String username;
	private String objectId;

	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		if(!RODUtil.isNullOrEmpty(username))
			list = RODServices.getDbService().getUndoDao().getRODHistoryByUser(username);
		else
			list = RODServices.getDbService().getUndoDao().getRODHistory(objectId);
		return new ForwardResolution("/pages/rodhistory.jsp");

	}

	public List<VersionDTO> getList() {
		return list;
	}

	public void setList(List<VersionDTO> list) {
		this.list = list;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}




}
