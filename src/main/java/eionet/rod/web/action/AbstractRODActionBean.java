package eionet.rod.web.action;

import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import eionet.rod.Constants;
import eionet.rod.ROUser;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.web.context.RODActionBeanContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;


/**
 * Root class for all CR ActionBeans.
 *
 * @author altnyris
 *
 */
public abstract class AbstractRODActionBean implements ActionBean {

    /** */
    private static Log logger = LogFactory.getLog(AbstractRODActionBean.class);

    /** */
    private RODActionBeanContext context;

    /*
     * (non-Javadoc)
     * @see net.sourceforge.stripes.action.ActionBean#getContext()
     */
    public RODActionBeanContext getContext() {
        return this.context;
    }

    /*
     * (non-Javadoc)
     * @see net.sourceforge.stripes.action.ActionBean#setContext(net.sourceforge.stripes.action.ActionBeanContext)
     */
    public void setContext(ActionBeanContext context) {
        this.context = (RODActionBeanContext) context;
    }

    /**
     * @return logged in user name or default value for not logged in users.
     */
    public final String getUserName() {
        String ret = null;
        ROUser roUser = getContext().getROUser();
        if (roUser != null)
            ret = roUser.getUserName();

        return ret;
    }

    /**
     * Method checks whether user is logged in or not.
     *
     * @return true if user is logged in.
     */
    public final boolean getIsUserLoggedIn() {
        return getROUser() != null;
    }

    /**
     *
     * @param String exception to handle.
     */
    void handleRodException(String exception, int severity) {
        logger.error(exception);
        getContext().setSeverity(severity);
        getContext().getMessages().add(new SimpleError(exception));
    }

    /**
     *
     * @return
     */
    protected ROUser getROUser() {
        return getContext().getROUser();
    }

    public final String getLoginURL() {
        return getContext().getCASLoginURL();
    }

    public final String getLogoutURL() {
        return getContext().getCASLogoutURL();
    }

    public final String getLastUpdate() {
        String ret = "";
        try {
            ret = RODServices.getDbService().getGenericlDao().getLastUpdate();
        } catch (ServiceException e) {
            logger.error(e.toString(), e);
        }
        return ret;
    }

    /**
     *
     * @return
     */
    public ResourceBundle getBundle() {
        ResourceBundle bundle = ResourceBundle.getBundle("/StripesResources");
        return bundle;
    }

    /**
     *
     * @return
     */
    public boolean isPostRequest() {
        return getContext().getRequest().getMethod().equalsIgnoreCase("POST");
    }

    /**
     *
     * @param String message
     */
    void showMessage(String msg) {
        getContext().setSeverity(Constants.SEVERITY_INFO);
        getContext().getMessages().add(new SimpleMessage(msg));
    }

}
