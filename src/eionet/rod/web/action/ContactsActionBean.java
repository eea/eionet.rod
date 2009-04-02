package eionet.rod.web.action;

import java.util.Hashtable;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.directory.DirServiceException;
import eionet.directory.DirectoryService;
import eionet.directory.dto.RoleDTO;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/contacts")
public class ContactsActionBean extends AbstractRODActionBean {
	
	private String roleId;
	private String parentRoleId;
	private String roleDescription;
	private String roleUrl;
	private List<RoleDTO> subroles;
	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		try{
			
			int index = roleId.lastIndexOf("-");
			if(index != -1)
				parentRoleId = roleId.substring(0, index);
			
			Hashtable role = DirectoryService.getRole(roleId);
			if(role != null){
				roleDescription = (String)role.get("DESCRIPTION");
				roleUrl = (String)role.get("URL_MEMBERS");
			}
			
			subroles = DirectoryService.getSubroles(roleId);
			
		} catch(DirServiceException e){
			e.printStackTrace();
		}
		
		return new ForwardResolution("/pages/contacts.jsp");
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getParentRoleId() {
		return parentRoleId;
	}

	public void setParentRoleId(String parentRoleId) {
		this.parentRoleId = parentRoleId;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public List<RoleDTO> getSubroles() {
		return subroles;
	}

	public void setSubroles(List<RoleDTO> subroles) {
		this.subroles = subroles;
	}

	public String getRoleUrl() {
		return roleUrl;
	}

	public void setRoleUrl(String roleUrl) {
		this.roleUrl = roleUrl;
	} 

}
