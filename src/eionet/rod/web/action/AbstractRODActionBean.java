package eionet.rod.web.action;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import eionet.rod.ROUser;
import eionet.rod.web.context.RODActionBeanContext;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;


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
		ROUser roUser = getContext().getROUser();
		return roUser.getUserName();
	}
	
	/**
	 * Method checks whether user is logged in or not.
	 * 
	 * @return true if user is logged in.
	 */
	public final boolean isUserLoggedIn() {
		return getROUser()!=null;
	}
	
	/**
	 * 
	 * @return
	 */
	protected ROUser getROUser(){
		return getContext().getROUser();
	}
	
}
