/**
 * 
 */
package eionet.rod.web.context;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import eionet.rod.Attrs;
import eionet.rod.ROUser;

import net.sourceforge.stripes.action.ActionBeanContext;


/**
 * Extension of stripes ActionBeanContext.
 * 
 * @author altnyris
 *
 */
public class RODActionBeanContext extends ActionBeanContext {
	
	/**
	 * Wrapper method for {@link ServletRequest#getParameter(String)}.
	 * <p>
	 * The wrapper allows to avoid direct usage of {@link HttpServletRequest}.
	 * @param parameterName parameter name.
	 * @return corresponding parameter value from {@link HttpServletRequest}.
	 */
	public String getRequestParameter(String parameterName) {
		return getRequest().getParameter(parameterName);
	}
	
	/**
	 * Wrapper method for {@link HttpSession#setAttribute(String, Object)}.
	 * <p>
	 * The wrapper allows to avoid direct usage of {@link HttpSession}.
	 * @param name session attribute name.
	 * @param value session attribute value.
	 */
	public void setSessionAttribute(String name, Object value) {
		getRequest().getSession().setAttribute(name, value);
	}
	
	/**
	 * Method returns {@link ROUser} from session.
	 * 
	 * @return {@link ROUser} from session or null if user is not logged in.
	 */
	public ROUser getROUser() {
		String appName = getServletContext().getInitParameter(Attrs.APPPARAM);
		return (ROUser) getRequest().getSession().getAttribute(Attrs.USERPREFIX + appName);
	}
	
	
}