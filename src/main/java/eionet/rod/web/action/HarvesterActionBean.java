package eionet.rod.web.action;

import eionet.acl.AccessControlListIF;
import eionet.acl.AccessController;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.countrysrv.Extractor;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 * 
 */
@UrlBinding("/harvester")
public class HarvesterActionBean extends AbstractRODActionBean {

    private String mode;

    /**
     * 
     * @return Resolution
     * @throws ServiceException
     */
    @DefaultHandler
    public Resolution init() throws ServiceException {

        String forwardPage = "/pages/harvester.jsp";

        return new ForwardResolution(forwardPage);
    }

    public Resolution harvest() throws ServiceException {
        String forwardPage = "/pages/harvester.jsp";

        String userName = getUserName();
        if (RODUtil.isNullOrEmpty(userName)) {
            handleRodException("You are not logged in!", Constants.SEVERITY_WARNING);
            return new ForwardResolution(forwardPage);
        }

        try {
            AccessControlListIF acl = AccessController.getAcl(Constants.ACL_HARVEST_NAME);
            boolean perm = acl.checkPermission(userName, Constants.ACL_INSERT_PERMISSION);
            if (!perm) {
                handleRodException("Insufficient permissions", Constants.SEVERITY_WARNING);
                return new ForwardResolution(forwardPage);
            }

            int m = Integer.parseInt(mode);
            Extractor ext = new Extractor();

            ext.harvest(m, getUserName());
            showMessage("Harvested! See log for details");
        } catch (Exception e) {
            handleRodException(e.toString(), Constants.SEVERITY_WARNING);
        }

        return new ForwardResolution(forwardPage);
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

}
