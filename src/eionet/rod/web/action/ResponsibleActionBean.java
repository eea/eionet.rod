package eionet.rod.web.action;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.dto.ResponsibleRoleDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/responsible")
public class ResponsibleActionBean extends AbstractRODActionBean {
	
	private String role;
	private String spatial;
	private String member;
	
	private ResponsibleRoleDTO responsibleRole;
	
	
	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		String rt = "";
		String id = "";
		if(member != null){
			if(member.equalsIgnoreCase("Y")){
				rt = "mc";
			} else if(member.equalsIgnoreCase("N")){
				rt = "cc";
			}
		}
		if(role != null && spatial != null){
			id = role + "-" + rt + "-" + spatial;
			id = id.toLowerCase();
		}
		
		if(role != null && !role.equals("") && spatial != null && !spatial.equals("") && member != null && !member.equals("")){
			responsibleRole = RODServices.getDbService().getRoleDao().getRoleDesc(id,role);
		} else {
			return new ErrorResolution(HttpServletResponse.SC_NOT_FOUND);
		}
		
		return new ForwardResolution("/pages/responsible.jsp");
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}


	public String getSpatial() {
		return spatial;
	}


	public void setSpatial(String spatial) {
		this.spatial = spatial;
	}


	public String getMember() {
		return member;
	}


	public void setMember(String member) {
		this.member = member;
	}


	public ResponsibleRoleDTO getResponsibleRole() {
		return responsibleRole;
	}


	public void setResponsibleRole(ResponsibleRoleDTO responsibleRole) {
		this.responsibleRole = responsibleRole;
	}
}
