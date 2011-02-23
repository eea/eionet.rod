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
@UrlBinding("/updatehistory")
public class UpdateHistoryActionBean extends AbstractRODActionBean {
	
	private List<VersionDTO> list;
	private String username;
	private String id;
	private String object;
	private String type;

	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		if(!RODUtil.isNullOrEmpty(username))
			list = RODServices.getDbService().getUndoDao().getUpdateHistoryByUser(username);
		if(!RODUtil.isNullOrEmpty(type))
			list = RODServices.getDbService().getUndoDao().getDeleted(type);
		else
			list = RODServices.getDbService().getUndoDao().getUpdateHistory(id, object);
		return new ForwardResolution("/pages/updatehistory.jsp");

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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}


}
