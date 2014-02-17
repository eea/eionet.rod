package eionet.rod.web.action;

import javax.servlet.http.HttpSession;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import org.apache.commons.lang.StringUtils;

import edu.yale.its.tp.cas.client.filter.CASFilter;
import eionet.rod.Attrs;
import eionet.rod.ROUser;

/**
 * An action bean for invalidating the user session and redirecting to the SSO logout page.
 *
 * @author Jaanus
 */
@UrlBinding("/logout")
public class LogoutActionBean extends AbstractRODActionBean {

    /**
     * The default action.
     * Does session invalidation and redirecting to the SSO logout page.
     *
     * @return The resolution to go to.
     */
    @DefaultHandler
    public Resolution defaultHandler() {

        HttpSession session = getContext().getRequest().getSession();
        String appName = session.getServletContext().getInitParameter(Attrs.APPPARAM);
        if (appName == null) {
            appName = StringUtils.EMPTY;
        }

        String userSessionAttrName = Attrs.USERPREFIX + appName;
        ROUser userObject = (ROUser) session.getAttribute(userSessionAttrName);
        if (userObject != null) {
            try {
                userObject.invalidate();
            } catch (Exception e) {
                // Ignore any invalidation exception as we shall in any case nullify the session's user attribute.
            }
        }

        session.setAttribute(userSessionAttrName, null);
        session.setAttribute(CASFilter.CAS_FILTER_USER, null);
        session.invalidate();
        return new RedirectResolution(getContext().getCASLogoutURL(), false);
    }
}
