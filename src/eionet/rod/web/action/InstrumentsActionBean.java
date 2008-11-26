package eionet.rod.web.action;

import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.action.UrlBinding;
import eionet.rod.RODUtil;
import eionet.rod.dto.HierarchyInstrumentDTO;
import eionet.rod.dto.InstrumentFactsheetDTO;
import eionet.rod.dto.InstrumentsListDTO;
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
	private InstrumentsListDTO hierarchyInstrument;
	private List<HierarchyInstrumentDTO> hierarchyInstruments;
	private String instId;
	private String id;
	private String dgenv;
	private String hierarchyTree;
	
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
				instId = st.nextToken();
		}
		
		String acceptHeader = getContext().getRequest().getHeader("accept");
		String[] accept = acceptHeader.split(",");
		
		if(!RODUtil.isNullOrEmpty(instId) && RODUtil.isNumber(instId)){
			instrument = RODServices.getDbService().getSourceDao().getInstrumentFactsheet(instId);
			dgenv = RODServices.getDbService().getSourceDao().getDGEnvNameByInstrumentId(instId);
		} else if(RODUtil.isNullOrEmpty(instId) && accept != null && accept.length > 0 && accept[0].equals("application/rdf+xml")){
			return new StreamingResolution("application/rdf+xml;charset=UTF-8") {
			    public void stream(HttpServletResponse response) throws Exception {
			    	Instruments inst = new Instruments();
			    	String rdf = inst.getRdf(getContext().getRequest());
			    	response.getWriter().write(rdf);
			    }
			}.setFilename("instruments.rdf");
		} else {
			if(RODUtil.isNullOrEmpty(id))
				id = "1";
			hierarchyInstrument = RODServices.getDbService().getSourceDao().getHierarchyInstrument(id);
			String parentId = hierarchyInstrument.getParentId();
			boolean hasParent = true;
			if(RODUtil.isNullOrEmpty(parentId))				
				hasParent = false;
			
			hierarchyTree = RODServices.getDbService().getSourceDao().getHierarchy(id, hasParent);
			
			hierarchyInstruments = RODServices.getDbService().getSourceDao().getHierarchyInstruments(id);
			
			forwardPage = "/pages/instruments.jsp";
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

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public InstrumentsListDTO getHierarchyInstrument() {
		return hierarchyInstrument;
	}

	public void setHierarchyInstrument(InstrumentsListDTO hierarchyInstrument) {
		this.hierarchyInstrument = hierarchyInstrument;
	}

	public String getHierarchyTree() {
		return hierarchyTree;
	}

	public void setHierarchyTree(String hierarchyTree) {
		this.hierarchyTree = hierarchyTree;
	}

	public List<HierarchyInstrumentDTO> getHierarchyInstruments() {
		return hierarchyInstruments;
	}

	public void setHierarchyInstruments(
			List<HierarchyInstrumentDTO> hierarchyInstruments) {
		this.hierarchyInstruments = hierarchyInstruments;
	}
	
	

}
