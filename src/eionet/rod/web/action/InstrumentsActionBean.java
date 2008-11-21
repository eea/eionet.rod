package eionet.rod.web.action;

import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.Constants;
import eionet.rod.RODUtil;
import eionet.rod.dto.InstrumentFactsheetDTO;
import eionet.rod.rdf.Activities;
import eionet.rod.rdf.Instruments;
import eionet.rod.services.RODServices;
import eionet.rod.services.ServiceException;

/**
 * 
 * @author <a href="mailto:risto.alt@tietoenator.com">Risto Alt</a>
 *
 */
@UrlBinding("/instruments")
public class InstrumentsActionBean extends AbstractRODActionBean {
	
	private InstrumentFactsheetDTO instrument;
	private String id;
	private String dgenv;
	
	/**
	 * 
	 * @return
	 */
	@DefaultHandler
	public Resolution init() throws ServiceException {
		
		String forwardPage = "/pages/instrument.jsp";
		String pathInfo = getContext().getRequest().getPathInfo();
		if(!RODUtil.isNullOrEmpty(pathInfo)){
			StringTokenizer st = new StringTokenizer(pathInfo,"/");
			if(st.hasMoreElements())
				id = st.nextToken();
		}
		
		String acceptHeader = getContext().getRequest().getHeader("accept");
		String[] accept = acceptHeader.split(",");
		
		if(!RODUtil.isNullOrEmpty(id) && RODUtil.isNumber(id)){
			instrument = RODServices.getDbService().getSourceDao().getInstrumentFactsheet(id);
			dgenv = RODServices.getDbService().getSourceDao().getDGEnvNameByInstrumentId(id);
		} else if(RODUtil.isNullOrEmpty(id) && accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")){
			return new StreamingResolution("application/rdf+xml;charset=UTF-8") {
			    public void stream(HttpServletResponse response) throws Exception {
			    	Instruments inst = new Instruments();
			    	String rdf = inst.getRdf(getContext().getRequest());
			    	response.getWriter().write(rdf);
			    }
			}.setFilename("instruments.rdf");
		} else {
			handleRodException("Legislative instrument ID is missing or is not a number!", Constants.SEVERITY_WARNING);
		}
		
		return new ForwardResolution(forwardPage);
	}

	public InstrumentFactsheetDTO getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentFactsheetDTO instrument) {
		this.instrument = instrument;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDgenv() {
		return dgenv;
	}

	public void setDgenv(String dgenv) {
		this.dgenv = dgenv;
	}
	
	

}
