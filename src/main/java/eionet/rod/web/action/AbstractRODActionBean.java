package eionet.rod.web.action;

import java.util.ResourceBundle;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.controller.AnnotatedClassActionResolver;
import net.sourceforge.stripes.validation.SimpleError;

import eionet.rod.Constants;
import eionet.rod.ROUser;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;
import eionet.rod.web.context.RODActionBeanContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base class for ROD's Stripes action beans.
 *
 * @author altnyris
 *
 */
public abstract class AbstractRODActionBean implements ActionBean {

    /** Static logger for this class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractRODActionBean.class);

    /** ROD's extension to the Stripes action bean context. */
    private RODActionBeanContext context;

    /*
     * (non-Javadoc)
     *
     * @see net.sourceforge.stripes.action.ActionBean#getContext()
     */
    @Override
    public RODActionBeanContext getContext() {
        return this.context;
    }

    /*
     * (non-Javadoc)
     *
     * @see net.sourceforge.stripes.action.ActionBean#setContext(net.sourceforge.stripes.action.ActionBeanContext)
     */
    @Override
    public void setContext(ActionBeanContext context) {
        this.context = (RODActionBeanContext) context;
    }

    /**
     * Gets the current session's user name.
     *
     * @return logged in user name or default value for not logged in users.
     */
    public String getUserName() {
        String ret = null;
        ROUser roUser = getContext().getROUser();
        if (roUser != null) {
            ret = roUser.getUserName();
        }

        return ret;
    }

    /**
     * Method checks whether user is logged in or not.
     *
     * @return true if user is logged in.
     */
    public boolean getIsUserLoggedIn() {
        return getROUser() != null;
    }

    /**
     * Handles the given exception message.
     *
     * @param String The exception message to handle.
     * @param String The exception's severity indicator.
     */
    void handleRodException(String exception, int severity) {
        LOGGER.error(exception);
        getContext().setSeverity(severity);
        getContext().getMessages().add(new SimpleError(exception));
    }

    /**
     * Returns the current session's {@link ROUser} object.
     *
     * @return The user object.
     */
    protected ROUser getROUser() {
        return getContext().getROUser();
    }

    /**
     * Return login URL for the current Stripes request execution context.
     *
     * @return The login URL.
     */
    public String getLoginURL() {
        return getContext().getCASLoginURL();
    }

    /**
     * Return logout URL for the current Stripes request execution context.
     *
     * @return The logout URL.
     */
    public String getLogoutURL() {

        String contextPath = getContext().getRequest().getContextPath();
        return new StringBuilder(contextPath).append(new AnnotatedClassActionResolver().getUrlBinding(LogoutActionBean.class))
                .toString();
    }

    /**
     * Returns the last update date of any ROD database contents.
     *
     * @return The date formatted as string.
     */
    public String getLastUpdate() {
        String ret = "";
        try {
            ret = RODServices.getDbService().getGenericlDao().getLastUpdate();
        } catch (ServiceException e) {
            LOGGER.error(e.toString(), e);
        }
        return ret;
    }

    /**
     * Returns current Stripes resource bundle.
     *
     * @return ResourceBundle The bundle.
     */
    public ResourceBundle getBundle() {
        ResourceBundle bundle = ResourceBundle.getBundle("/StripesResources");
        return bundle;
    }

    /**
     * Returns true if the current request is a POST request.
     *
     * @return boolean True/false.
     */
    public boolean isPostRequest() {
        return getContext().getRequest().getMethod().equalsIgnoreCase("POST");
    }

    /**
     * Adds the given string message into current Stripes request execution messages, using severity level INFO.
     *
     * @param msg The message.
     */
    void showMessage(String msg) {
        getContext().setSeverity(Constants.SEVERITY_INFO);
        getContext().getMessages().add(new SimpleMessage(msg));
    }

    /**
     * Returns context path of the currently executed request.
     *
     * @return Full context url.
     */
    public String getContextPath() {
        return getContext().getRequest().getContextPath();
    }
}
