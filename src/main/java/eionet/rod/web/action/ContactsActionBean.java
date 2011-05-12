package eionet.rod.web.action;

import org.apache.commons.lang.StringUtils;

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
    private RoleDTO role;

    /**
     * 
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        try {

            if (!StringUtils.isBlank(roleId)) {
                int index = roleId.lastIndexOf("-");
                if (index != -1)
                    parentRoleId = roleId.substring(0, index);
    
                role = DirectoryService.getRoleDTO(roleId);
            }

        } catch (DirServiceException e) {
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

    public RoleDTO getRole() {
        return role;
    }

    public void setRole(RoleDTO role) {
        this.role = role;
    }

}
