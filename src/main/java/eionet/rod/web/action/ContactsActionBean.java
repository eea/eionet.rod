package eionet.rod.web.action;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang.StringUtils;

import eionet.directory.DirServiceException;
import eionet.directory.DirectoryService;
import eionet.directory.dto.MemberDTO;
import eionet.directory.dto.RoleDTO;
import eionet.rod.services.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 */
@UrlBinding("/contacts")
public class ContactsActionBean extends AbstractRODActionBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(ContactsActionBean.class);

    /** */
    private String roleId;
    private String parentRoleId;
    private RoleDTO dirRole;

    /** */
    private boolean isDirectoryError = false;

    /**
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        try {

            if (!StringUtils.isBlank(roleId)) {
                int index = roleId.lastIndexOf("-");
                if (index != -1) {
                    parentRoleId = roleId.substring(0, index);
                }

                dirRole = DirectoryService.getRoleDTO(roleId);
            }

        } catch (DirServiceException e) {
            isDirectoryError = true;
            LOGGER.error(e.getMessage(), e);
        }

        return new ForwardResolution("/pages/contacts.jsp");
    }

    /**
     * @return
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    /**
     * @return
     */
    public String getParentRoleId() {
        return parentRoleId;
    }

    /**
     * @return
     */
    public RoleDTO getDirRole() {
        return dirRole;
    }

    public boolean isDirectoryRoleFound() {

        if (dirRole == null) {
            return false;
        }

        String description = dirRole.getDescription();
        List<MemberDTO> members = dirRole.getMembers();
        String membersUrl = dirRole.getMembersUrl();
        String name = dirRole.getName();
        List<RoleDTO> subRoles = dirRole.getSubroles();
        return false;
    }

    /**
     * @return the directoryError
     */
    public boolean isDirectoryError() {
        return isDirectoryError;
    }
}
