package eionet.rod.web.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.displaytag.decorator.TableDecorator;

import eionet.rod.Attrs;
import eionet.rod.ROUser;
import eionet.rod.dto.VersionDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 *
 */
public class VersionsTableDecorator extends TableDecorator{
	
	/** 
	 * 
	 * @return
	 */
	public String getTime(){
		
		VersionDTO search = (VersionDTO) getCurrentRowObject();
		
		DateFormat df = new SimpleDateFormat ("yyyy-MM-dd' 'HH:mm:ss");
		Date date = new Date(new Long(search.getUndoTime()).longValue());
		String d = df.format(date);
		
		return d;
	}
	
	/** 
	 * 
	 * @return
	 */
	public String getObject(){
		
		StringBuilder ret = new StringBuilder();
		VersionDTO ver = (VersionDTO) getCurrentRowObject();
		String user = null;
		
		try{
			user = RODServices.getDbService().getUndoDao().getUndoUser(Long.valueOf(ver.getUndoTime()).longValue(),ver.getTab());
		} catch(ServiceException e) {
			e.printStackTrace();
		}

		HttpServletRequest req = (HttpServletRequest) getPageContext().getRequest();
		String path = req.getContextPath();
		
		ret.append("<a href='").append(path).append("/undoinfo.jsp?ts=").append(ver.getUndoTime()).append("&amp;tab=").append(ver.getTab());
		ret.append("&amp;op=").append(ver.getOperation()).append("&amp;id=").append(ver.getValue()).append("&amp;user=").append(user).append("'>");
		if(ver.getTab().equals("T_OBLIGATION"))
			ret.append("/obligations/").append(ver.getValue());
		else if(ver.getTab().equals("T_SOURCE"))
			ret.append("/instruments/").append(ver.getValue());
		ret.append("</a>");
			
		return ret.toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String getOper(){
		
		String ret = "";
		VersionDTO ver = (VersionDTO) getCurrentRowObject();
		
		if(ver.getOperation().equals("D") || ver.getOperation().equals("K")){
			ret = "DELETE";
		} else if(ver.getOperation().equals("I")){
			ret = "INSERT";
		} else if(ver.getOperation().equals("U")){
			ret = "UPDATE";
		} else if(ver.getOperation().equals("UN") || ver.getOperation().equals("UD")){
			ret = "UNDO";
		}
			
		return ret;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUser(){
		
		VersionDTO ver = (VersionDTO) getCurrentRowObject();
		String user = "";
		
		try{
			user = RODServices.getDbService().getUndoDao().getUndoUser(Long.valueOf(ver.getUndoTime()).longValue(),ver.getTab());
		} catch(ServiceException e) {
			e.printStackTrace();
		}
		
		return user;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getRadio(){
		
		StringBuilder ret = new StringBuilder();
		VersionDTO ver = (VersionDTO) getCurrentRowObject();
		
		try{
			HttpServletRequest req = (HttpServletRequest) getPageContext().getRequest();
			String appName = getPageContext().getServletContext().getInitParameter(Attrs.APPPARAM);
			ROUser user = (ROUser) req.getSession().getAttribute(Attrs.USERPREFIX + appName);
			
			if(ver.getOperation().equals("D") || (!ver.getOperation().equals("D") && !RODServices.getDbService().getGenericlDao().isIdAvailable(ver.getValue(),ver.getTab()))){
				String aclPath = "/obligations/" + ver.getValue();
				if(ver.getTab().equals("T_SOURCE"))
					aclPath = "/instruments/" + ver.getValue();
				if((!ver.getOperation().equals("D") && user != null && ROUser.hasPermission(user.getUserName(), aclPath, "u")) || (ver.getOperation().equals("D") && user != null && user.getUserName().equals(getUser()))){
					ret.append("<input type='radio' name='group' value='");
					ret.append(ver.getUndoTime()).append(",").append(ver.getTab()).append(",").append(ver.getOperation()).append(",").append(ver.getValue());
					ret.append("'/>");
				}
			}
		} catch(ServiceException e) {
			e.printStackTrace();
		}
		
		return ret.toString();
	}

}
