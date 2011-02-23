package eionet.rod.web.action;

import java.util.List;
import java.util.StringTokenizer;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;

import eionet.rod.Constants;
import eionet.rod.RODUtil;

import eionet.rod.dto.HelpDTO;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author altnyris
 *
 */
@UrlBinding("/help")
public class HelpActionBean extends AbstractRODActionBean {
	
	private String helpId;
	private HelpDTO help;
	private List<HelpDTO> helpList;
	/** 
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		String forwardPage = "/pages/help.jsp";
		boolean list = false;
		boolean edit = false;
		
		String pathInfo = getContext().getRequest().getPathInfo();
		if(!RODUtil.isNullOrEmpty(pathInfo)){
			StringTokenizer st = new StringTokenizer(pathInfo,"/");
			if(st.hasMoreElements())
				helpId = st.nextToken();
			if(st.hasMoreElements()){
				String token = st.nextToken(); 
				if(token.equals("list"))
					list = true;
				else if(token.equals("edit"))
					edit = true;
			}
		}
		
		if(!RODUtil.isNullOrEmpty(helpId) && list){
			helpList = RODServices.getDbService().getHelpDao().getHelpList(helpId);
			forwardPage = "/pages/helplist.jsp";
		} else if(!RODUtil.isNullOrEmpty(helpId) && !list) {
			help = RODServices.getDbService().getHelpDao().getHelp(helpId);
			if(edit)
				forwardPage = "/pages/edithelp.jsp";
		} else {		
			handleRodException("Help ID is missing!", Constants.SEVERITY_WARNING);
			return new ForwardResolution("/pages/help.jsp");
		}
			
		return new ForwardResolution(forwardPage);
	}
	
	public Resolution edit() throws ServiceException {
		RODServices.getDbService().getHelpDao().editHelp(help);
		return new ForwardResolution("/pages/help.jsp");
	}
	
	public String getHelpId() {
		return helpId;
	}
	public void setHelpId(String helpId) {
		this.helpId = helpId;
	}
	public HelpDTO getHelp() {
		return help;
	}
	public void setHelp(HelpDTO help) {
		this.help = help;
	}
	public List<HelpDTO> getHelpList() {
		return helpList;
	}
	public void setHelpList(List<HelpDTO> helpList) {
		this.helpList = helpList;
	}
	
}
